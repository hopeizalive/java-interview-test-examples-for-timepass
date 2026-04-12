package com.example.concurrency.interview.demos.support;

import org.springframework.kafka.test.EmbeddedKafkaBroker;

/** Starts an in-process Kafka broker for CLI lessons (no external cluster). */
public final class KafkaEmbedded implements AutoCloseable {

    private final EmbeddedKafkaBroker broker;

    private KafkaEmbedded(EmbeddedKafkaBroker broker) {
        this.broker = broker;
    }

    public static KafkaEmbedded start(int partitions, String... topics) throws Exception {
        EmbeddedKafkaBroker b = new EmbeddedKafkaBroker(1, true, partitions, topics);
        b.afterPropertiesSet();
        return new KafkaEmbedded(b);
    }

    public String bootstrapServers() {
        return broker.getBrokersAsString();
    }

    @Override
    public void close() {
        broker.destroy();
    }
}
