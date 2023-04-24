package com.vin.WareHouse.controller;

import com.google.gson.Gson;
import com.vin.WareHouse.*;
import com.vin.WareHouse.interfaces.Constants;
import com.vin.WareHouse.models.Item;
import com.vin.WareHouse.models.LogEvent;
import com.vin.WareHouse.models.OrderItem;
import com.vin.WareHouse.repository.EventsService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
public class WareHouseApi {

    @GrpcClient("inventoryClient")
    InventoryGrpc.InventoryBlockingStub inventoryBlockingStub;

    @GrpcClient("inventoryClient")
    InventoryGrpc.InventoryStub inventoryStub;
    @GrpcClient("inventoryClient")
    InventoryGrpc.InventoryFutureStub inventoryFutureStub;

    @GrpcClient("ordersClient")
    OrdersGrpc.OrdersBlockingStub ordersBlockingStub;

    @GrpcClient("deliveries")
    DeliveriesGrpc.DeliveriesBlockingStub deliveriesBlockingStub;


    @Autowired
    EventsService eventsService;

    @PostMapping("/events")
    public List<LogEvent> getEvents(){
        return eventsService.getLogs();
    }
    volatile boolean isDone=false;

    @PostMapping("/getItems")
    public String getItems(){
        List<Item> items=new ArrayList<>();

      var rsp= inventoryStub.listItems(new StreamObserver<>() {
            @Override
            public void onNext(ItemsList value) {
                value.getItemsList().forEach(item -> items.add(new Item(item.getItemId(), item.getItemName(), item.getItemPrice(), item.getItemQuantity())));
            isDone=true;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        });
      rsp.onNext(ItemsRequest.newBuilder().build());

        while (!isDone) {
            Thread.onSpinWait();
        }
       isDone=false;
       return new Gson().toJson(items);

    }

    @PostMapping("/addItem")
    public String addItem(@RequestBody Item item){

    var response=inventoryBlockingStub.addItem(AddItemRequest.newBuilder()
                .setItem(com.vin.WareHouse.Item.newBuilder()
                        .setItemName(item.getItemName())
                        .setItemPrice(item.getItemPrice())
                        .setItemQuantity(item.getQuantity())
                        .build())
        .build()).next();
        eventsService.logEvent(new LogEvent(0L,response.getMessage(), Constants.CLIENT_CONTEXT)
        );
       return "success";

    }
    @PostMapping("/makeAnOrder")
    public String makeAnOrder(@RequestBody List<Map<String,Object>> orderItems){
        PostOrderRequest.Builder builder =PostOrderRequest.newBuilder();

        orderItems.forEach(item -> {
            var i= com.vin.WareHouse.Item.newBuilder()
                    .setItemId(Long.valueOf(item.get("id").toString()))
                    .setItemName((String) item.get("itemName"))
                    .setItemPrice(Double.valueOf(item.get("itemPrice").toString()))
                    .setItemQuantity(Integer.valueOf(item.get("quantity").toString()));
            builder.addOrderItems(i);
        });
        var response= ordersBlockingStub.postOrder(builder.build());
        eventsService.logEvent(new LogEvent(0L,"Order Posted", Constants.CLIENT_CONTEXT));
        return "success";

    }

    @PostMapping("/getOrders")
    public String getOrders(){
       OrderList orderList= ordersBlockingStub.getOrders(GetOrdersRequest.getDefaultInstance()).next();
       var response=new ArrayList<OrderItem>();
       orderList.getOrderItemsList().forEach(postOrderRequest -> {
           var orderItem=new OrderItem();
           List<String> x=new ArrayList<>();
           postOrderRequest.getOrderItemsList().forEach(item -> {
               var i=item.getItemId()+","+item.getItemName()+","+item.getItemPrice()+","+item.getItemQuantity();
               x.add(i);
           });
           orderItem.setItems(String.join("=>",x));
           orderItem.setStatus(postOrderRequest.getOrderStatus());
           orderItem.setId(postOrderRequest.getOrderId());
           response.add(orderItem);
       });
    return new Gson().toJson(response);
    }

    @PostMapping("/changeStatus")
    public String changeStatus(@RequestBody Map<String,Object> data){
        var status=data.get("status").toString();
        if (Objects.equals(status, "Cancelled")){
           StatusResponse response=ordersBlockingStub.cancelOrders(CancelOrderRequest.newBuilder()
                    .setOrderId(Long.parseLong(data.get("orderId").toString())).build())
                    ;
            eventsService.logEvent(new LogEvent(0L,"Order Cancelled", Constants.CLIENT_CONTEXT));
            return "success";
        }
       StatusResponse response= deliveriesBlockingStub.changeStatus(ChangeStatusRequest.newBuilder()
                        .setStatus(status)
                        .setOrderId(Long.parseLong(data.get("orderId").toString()))
                .build());
        eventsService.logEvent(new LogEvent(0L,"Order Changed To ", Constants.CLIENT_CONTEXT));
        return "success";

    }

    @PostMapping("/deleteItem")
    public void deleteItem(@RequestBody Map<String,Object> data){
        inventoryBlockingStub.deleteItem(DeleteItemRequest.newBuilder()
                .setItemId(Long.parseLong(data.get("itemId").toString())).build());
        eventsService.logEvent(new LogEvent(0L,"Item Deleted", Constants.CLIENT_CONTEXT));
    }

}
