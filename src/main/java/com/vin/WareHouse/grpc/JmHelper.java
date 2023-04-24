package com.vin.WareHouse.grpc;

import com.vin.WareHouse.DeliveriesGrpc;
import com.vin.WareHouse.InventoryGrpc;
import com.vin.WareHouse.OrdersGrpc;
import com.vin.WareHouse.models.LogEvent;
import com.vin.WareHouse.repository.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;

@Component
public class JmHelper {
    @Autowired
    EventsService eventsService;
    public JmHelper() throws IOException {
        JmDNS jmDNS=JmDNS.create(InetAddress.getLocalHost());
        ServiceInfo inventoryService=ServiceInfo.create("_http._tcp.local.", InventoryGrpc.SERVICE_NAME,9090,"Inventory Service");
        ServiceInfo deliveryService=ServiceInfo.create("_http._tcp.local.", DeliveriesGrpc.SERVICE_NAME,9090,"Delivery Service");
        ServiceInfo ordersService=ServiceInfo.create("_http._tcp.local.", OrdersGrpc.SERVICE_NAME,9090,"Orders Service");
        jmDNS.registerService(inventoryService);
        jmDNS.registerService(deliveryService);
        jmDNS.registerService(ordersService);
        jmDNS.addServiceListener("_http._tcp.local.", new ServiceListener() {
            @Override
            public void serviceAdded(ServiceEvent event) {
                eventsService.logEvent(new LogEvent(0L,event.getInfo().toString(),"Service"));
            }

            @Override
            public void serviceRemoved(ServiceEvent event) {
                eventsService.logEvent(new LogEvent(0L,event.getInfo().toString(),"Service"));

            }

            @Override
            public void serviceResolved(ServiceEvent event) {
                eventsService.logEvent(new LogEvent(0L,event.getInfo().toString(),"Service"));

            }
        });
    }

}
