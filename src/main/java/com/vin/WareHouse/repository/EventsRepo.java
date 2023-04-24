package com.vin.WareHouse.repository;

import com.vin.WareHouse.models.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepo extends JpaRepository<LogEvent,Long> {
    @Query("select l from LogEvent l where l.context = ?1")
    List<LogEvent> findByContext(String context);
}
