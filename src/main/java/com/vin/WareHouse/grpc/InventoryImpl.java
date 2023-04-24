package com.vin.WareHouse.grpc;

import com.google.gson.Gson;
import com.vin.WareHouse.*;
import com.vin.WareHouse.interfaces.Constants;
import com.vin.WareHouse.models.Item;
import com.vin.WareHouse.models.LogEvent;
import com.vin.WareHouse.repository.EventsService;
import com.vin.WareHouse.repository.InventoryService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class InventoryImpl extends InventoryGrpc.InventoryImplBase {
    @Autowired
    InventoryService inventoryService;

    @Autowired
    EventsService eventsService;

    @Override
    public void addItem(AddItemRequest request, StreamObserver<StatusResponse> responseObserver) {
        var item=new Item();
        item.setItemName(request.getItem().getItemName());
        item.setItemPrice(request.getItem().getItemPrice());
        item.setQuantity(request.getItem().getItemQuantity());
        inventoryService.addItem(item);
        var lEvent=new LogEvent();
        lEvent.setMessage("Add Item Requested");
        lEvent.setContext(Constants.SERVER_CONTEXT);
        eventsService.logEvent(lEvent);
        responseObserver.onNext(StatusResponse.newBuilder()
                .setMessage("Item Added")
                .setSuccess(true).build());
        responseObserver.onCompleted();

    }

    @Override
    public StreamObserver<ItemsRequest> listItems(StreamObserver<ItemsList> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(ItemsRequest value) {
                var items = inventoryService.getItems();
                ItemsList.Builder builder = ItemsList.newBuilder();
                items.forEach(item ->{
                        System.out.println(new Gson().toJson(item));
                        builder.addItems(com.vin.WareHouse.Item.newBuilder()
                        .setItemQuantity(item.getQuantity())
                        .setItemPrice(item.getItemPrice())
                        .setItemName(item.getItemName())
                        .setItemId(item.getId())
                        .build());});

                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void deleteItem(DeleteItemRequest request, StreamObserver<StatusResponse> responseObserver) {
        inventoryService.deleteItem(request.getItemId());
        responseObserver.onNext(StatusResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
