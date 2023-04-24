package com.vin.WareHouse.grpc;

import com.vin.WareHouse.*;
import com.vin.WareHouse.interfaces.Constants;
import com.vin.WareHouse.models.OrderItem;
import com.vin.WareHouse.repository.OrdersService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@GrpcService
public class OrdersImpl extends OrdersGrpc.OrdersImplBase {
    @Autowired
    OrdersService ordersService;
    @Override
    public void postOrder(PostOrderRequest request, StreamObserver<StatusResponse> responseObserver) {
        List<String> orderItems=new ArrayList<>();
        request.getOrderItemsList().forEach(item -> {
            var i=item.getItemId()+","+item.getItemName()+","+item.getItemPrice()+","+item.getItemQuantity();
            orderItems.add(i);
        });
       var orderItem=new OrderItem();
       orderItem.setItems(String.join("=>",orderItems));
       orderItem.setStatus(Constants.PENDING);
        ordersService.addOrder(orderItem);
        responseObserver.onNext(StatusResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Order Posted")
                .build());
        responseObserver.onCompleted();

    }

    @Override
    public void getOrders(GetOrdersRequest request, StreamObserver<OrderList> responseObserver) {
        var orders=ordersService.getItems();
        OrderList.Builder builder=OrderList.newBuilder();
        orders.forEach(orderItem -> {
            var i= Arrays.stream(orderItem.getItems().split("=>")).toList();
            PostOrderRequest.Builder builder1=PostOrderRequest.newBuilder();
            i.forEach(x->{
                var id=x.split(",")[0];
                var name=x.split(",")[1];
                var price=x.split(",")[2];
                var q=x.split(",")[3];
                builder1.addOrderItems(Item.newBuilder()
                                .setItemId(Long.parseLong(id))
                                .setItemName(name)
                                .setItemQuantity(Integer.parseInt(q))
                                .setItemPrice(Double.parseDouble(price))
                        .build());
                builder1.setOrderStatus(orderItem.getStatus());
                builder1.setOrderId(orderItem.getId());

            });

           builder.addOrderItems(builder1.build());
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void cancelOrders(CancelOrderRequest request, StreamObserver<StatusResponse> responseObserver) {
        ordersService.cancelOrder(request.getOrderId());
        responseObserver.onNext(StatusResponse.newBuilder()
                        .setMessage("Order cancelled")
                        .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }
}
