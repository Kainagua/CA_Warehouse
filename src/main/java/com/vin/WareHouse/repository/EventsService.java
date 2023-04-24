package com.vin.WareHouse.repository;

import com.vin.WareHouse.models.LogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsService{
    @Autowired
    EventsRepo repo;

    public void logEvent(LogEvent event) {
        repo.save(event);
    }


    public List<LogEvent> getLogs() {
        return repo.findAll();
    }


    public List<LogEvent> getContextLogs(String context) {
        return repo.findByContext(context);
    }
}
