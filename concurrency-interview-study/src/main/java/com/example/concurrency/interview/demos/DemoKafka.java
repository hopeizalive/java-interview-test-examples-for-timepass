package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.demos.support.KafkaEmbedded;
import com.example.concurrency.interview.study.StudyContext;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class DemoKafka {

    private DemoKafka() {}

    public static void l59(StudyContext ctx) throws Exception {
        ctx.log("Kafka producer send(callback): async; metadata ack runs on sender thread — offload heavy work.");
        String topic = "study.l59.async";
        try (KafkaEmbedded kafka = KafkaEmbedded.start(2, topic)) {
            String bs = kafka.bootstrapServers();
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bs);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.ACKS_CONFIG, "all");
            var ackLatch = new CountDownLatch(5);
            try (var producer = new KafkaProducer<String, String>(props)) {
                for (int i = 0; i < 5; i++) {
                    int n = i;
                    producer.send(
                            new ProducerRecord<>(topic, "k" + n, "v" + n),
                            (meta, err) -> {
                                if (err != null) {
                                    ctx.log("  send failed: " + err);
                                } else {
                                    ctx.log("  ack p=" + meta.partition() + " off=" + meta.offset());
                                }
                                ackLatch.countDown();
                            });
                }
                producer.flush();
                ackLatch.await(15, TimeUnit.SECONDS);
            }
            ctx.log("Consumer group reads the same topic (one partition may be idle with one consumer).");
            Properties cp = consumerProps(bs, "g59-" + UUID.randomUUID());
            try (var consumer = new KafkaConsumer<String, String>(cp)) {
                consumer.subscribe(Collections.singletonList(topic));
                int seen = 0;
                long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
                while (seen < 5 && System.nanoTime() < deadline) {
                    ConsumerRecords<String, String> recs = consumer.poll(Duration.ofMillis(300));
                    seen += recs.count();
                }
                ctx.log("  consumer saw records: " + seen);
            }
        }
    }

    public static void l60(StudyContext ctx) throws Exception {
        ctx.log("Consumer concurrency: same group.id, multiple threads each with their own KafkaConsumer.");
        ctx.log("Batch vs per-record: poll() returns ConsumerRecords (a batch); Spring @KafkaListener can map both styles.");
        String topic = "study.l60.batch";
        try (KafkaEmbedded kafka = KafkaEmbedded.start(4, topic)) {
            String bs = kafka.bootstrapServers();
            produceBatch(bs, topic, ctx);
            String groupId = "g60-" + UUID.randomUUID();
            AtomicInteger remaining = new AtomicInteger(12);
            try (ExecutorService workers = Executors.newFixedThreadPool(2)) {
                Future<?> f1 = workers.submit(() -> runConsumer(bs, groupId, "client-a", topic, ctx, remaining));
                Future<?> f2 = workers.submit(() -> runConsumer(bs, groupId, "client-b", topic, ctx, remaining));
                f1.get(20, TimeUnit.SECONDS);
                f2.get(20, TimeUnit.SECONDS);
            }
            ctx.log("Capstone: bounded queue + worker pool ≈ topic partitions + consumer threads + handler logic.");
            ctx.log("  Prefer idempotent handlers and explicit retry/DLQ for production parity.");
        }
    }

    private static void produceBatch(String bs, String topic, StudyContext ctx) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bs);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        try (var producer = new KafkaProducer<String, String>(props)) {
            for (int i = 0; i < 12; i++) {
                producer.send(new ProducerRecord<>(topic, "p" + (i % 4), "m" + i));
            }
            producer.flush();
        }
        ctx.log("  produced 12 messages across 4 partition keys");
    }

    private static void runConsumer(
            String bs, String groupId, String clientId, String topic, StudyContext ctx, AtomicInteger remaining) {
        try {
            Properties cp = consumerProps(bs, groupId);
            cp.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
            try (var consumer = new KafkaConsumer<String, String>(cp)) {
                consumer.subscribe(Collections.singletonList(topic));
                long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(18);
                while (remaining.get() > 0 && System.nanoTime() < deadline) {
                    ConsumerRecords<String, String> batch = consumer.poll(Duration.ofMillis(400));
                    if (batch.isEmpty()) {
                        continue;
                    }
                    ctx.log("  [" + clientId + "] polled batch count=" + batch.count());
                    batch.forEach(r -> ctx.log("    [" + clientId + "] record key=" + r.key() + " val=" + r.value()));
                    remaining.addAndGet(-batch.count());
                }
            }
        } catch (Exception e) {
            ctx.log("  [" + clientId + "] error: " + e.getMessage());
        }
    }

    private static Properties consumerProps(String bs, String groupId) {
        Properties cp = new Properties();
        cp.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bs);
        cp.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        cp.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        cp.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        cp.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        cp.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        return cp;
    }
}
