package com.vin.WareHouse.repository;

import com.vin.WareHouse.interfaces.Constants;
import com.vin.WareHouse.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrdersService {
    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    InventoryRepo inventoryRepo;

    public void addOrder(OrderItem item){
        ordersRepository.save(item);
    }

    public List<OrderItem> getItems(){
        return ordersRepository.findAll();
    }

    public void cancelOrder(Long orderId){
        OrderItem item=ordersRepository.findById(orderId).get();
        List<String> orderItems= Arrays.stream(item.getItems().split("=>")).toList();
        orderItems.forEach(order->{
            var id=order.split(",")[0];
            var quantity=order.split(",")[3];
            inventoryRepo.updateQuantityById(Integer.valueOf(quantity),Long.valueOf(id));
        });
        ordersRepository.updateStatusById(Constants.CANCELLED,orderId);
    }

    public void changeStatus(String status,Long id){
        ordersRepository.updateStatusById(status,id);
    }


}
