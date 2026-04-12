package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** CQRS-style read model updated from domain events (in-memory demo). */
public final class Lesson37 extends AbstractMicroLesson {

    public Lesson37() {
        super(37, "CQRS sketch: command updates write model; event projects into read model map.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
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
