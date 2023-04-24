package com.vin.WareHouse.repository;

import com.vin.WareHouse.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    InventoryRepo inventoryRepo;
    public void addItem(Item item){
        inventoryRepo.save(item);
    }

    public List<Item> getItems(){
        return inventoryRepo.findAll();
    }


    public void deleteItem(long itemId) {
        inventoryRepo.deleteById(itemId);
    }
}
