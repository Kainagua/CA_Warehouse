package com.vin.WareHouse.repository;

import com.vin.WareHouse.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface InventoryRepo extends JpaRepository<Item,Long> {
    @Transactional
    @Modifying
    @Query("update Item i set i.quantity = i.quantity + ?1 where i.id = ?2")
    int updateQuantityById(Integer quantity, Long id);

}
