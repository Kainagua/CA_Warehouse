package com.vin.WareHouse.repository;

import com.vin.WareHouse.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrdersRepository extends JpaRepository<OrderItem,Long> {
    @Transactional
    @Modifying
    @Query("update OrderItem o set o.status = ?1 where o.id = ?2")
    int updateStatusById(String status, Long id);
}
