package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lesson 37 demonstrates CQRS-style read-model projection.
 *
 * <p>Domain events are replayed into a query model map to decouple write and read concerns.
 */
public final class Lesson37 extends AbstractMicroLesson {

    public Lesson37() {
        super(34, "CQRS sketch: command updates write model; event projects into read model map.");
    }

    /**
     * Lesson 34/37: event projection into read model.
     *
     * <p><b>Purpose:</b> Show how read models can be built from event streams.
     * <p><b>Role:</b> Connects messaging lessons to query-optimized views.
     * <p><b>Demonstration:</b> Applies NameChanged events and logs final projection state.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story action: project event sequence into materialized read map.
        Map<String, String> readModel = new ConcurrentHashMap<>();
        record NameChanged(String id, String name) {}
        List<NameChanged> events = List.of(new NameChanged("1", "ada"), new NameChanged("1", "ada-lovelace"));
        for (NameChanged e : events) {
            readModel.put(e.id(), e.name());
        }
        ctx.log("Read model: " + readModel);
        ctx.log("Talking point: Spring can host separate read stacks (projections) fed by Kafka/Rabbit.");
    }
}
