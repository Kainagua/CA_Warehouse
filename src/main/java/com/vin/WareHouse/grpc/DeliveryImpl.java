package com.vin.WareHouse.grpc;

import com.vin.WareHouse.ChangeStatusRequest;
import com.vin.WareHouse.DeliveriesGrpc;
import com.vin.WareHouse.StatusResponse;
import com.vin.WareHouse.repository.OrdersService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class DeliveryImpl extends DeliveriesGrpc.DeliveriesImplBase {
    @Autowired
    OrdersService ordersService;

    @Override
    public void changeStatus(ChangeStatusRequest request, StreamObserver<StatusResponse> responseObserver) {
       ordersService.changeStatus(request.getStatus(),request.getOrderId());
       responseObserver.onNext(StatusResponse.newBuilder().build());
       responseObserver.onCompleted();
    }
}
