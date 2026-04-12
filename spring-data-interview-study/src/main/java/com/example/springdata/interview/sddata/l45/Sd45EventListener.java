package com.example.springdata.interview.sddata.l45;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Sd45EventListener {

    private volatile String last;

    @EventListener
    public void onPlaced(Sd45PlacedEvent event) {
        this.last = "received Sd45PlacedEvent orderId=" + event.orderId();
    }

    public String getLast() {
        return last;
    }
}
