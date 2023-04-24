package com.vin.WareHouse.grpc;

import com.vin.WareHouse.models.LogEvent;
import com.vin.WareHouse.repository.EventsService;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcAdvice
public class ExceptionHandler {
    @Autowired
    EventsService eventsService;
    @GrpcExceptionHandler
    public Status handleError(Exception exception){
        eventsService.logEvent(new LogEvent(0L,"Grpc Error :"+exception.getMessage(),""));
        return Status.UNKNOWN.withCause(exception.getCause());
    }
}
