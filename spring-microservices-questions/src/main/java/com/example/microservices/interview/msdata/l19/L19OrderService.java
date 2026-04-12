package com.example.microservices.interview.msdata.l19;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class L19OrderService {

    private final L19OrderRepository orders;
    private final L19OutboxRepository outbox;

    public L19OrderService(L19OrderRepository orders, L19OutboxRepository outbox) {
        this.orders = orders;
        this.outbox = outbox;
    }

    @Transactional
    public void placeOrder(String ref, String eventJson) {
        L19OrderLedger o = new L19OrderLedger();
        o.setOrderRef(ref);
        orders.save(o);
        L19OutboxEvent e = new L19OutboxEvent();
        e.setPayload(eventJson);
        e.setPublished(false);
        outbox.save(e);
    }
}
