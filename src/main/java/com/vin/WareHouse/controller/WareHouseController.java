package com.vin.WareHouse.controller;

import com.vin.WareHouse.repository.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WareHouseController {

    @Autowired
    InventoryService inventoryService;
    @GetMapping("/")
    public String hello(Model model){
        return "index";
    }

    @GetMapping("/inventory")
    public String inventory(Model model){
        return "inventory";
    }

    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("items",inventoryService.getItems());
        return "orders";
    }
    @GetMapping("/deliveries")
    public String deliveries(Model model){

        return "deliveries";
    }



}
