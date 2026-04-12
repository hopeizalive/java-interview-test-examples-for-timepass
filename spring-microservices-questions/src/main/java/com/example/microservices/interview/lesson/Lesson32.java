package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Kafka via Testcontainers: producer + consumer read one record. */
public final class Lesson32 extends AbstractMicroLesson {

    public Lesson32() {
        super(32, "Kafka + Testcontainers: producer sends to topic; consumer with groupId reads one poll.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        if (!ctx.dockerAvailable()) {
            ctx.log("Skip: Docker not available for Kafka Testcontainer.");
            return;
        }
        try (KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"))) {
            kafka.start();
            String bootstrap = kafka.getBootstrapServers();
            String topic = "lesson32";
            Map<String, Object> prodCfg = Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap,
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()
            );
            try (KafkaProducer<String, String> producer = new KafkaProducer<>(prodCfg)) {
                producer.send(new ProducerRecord<>(topic, "k1", "payload")).get();
            }
            Map<String, Object> consCfg = Map.of(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap,
                    ConsumerConfig.GROUP_ID_CONFIG, "g-" + UUID.randomUUID(),
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()
            );
            try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consCfg)) {
                consumer.subscribe(List.of(topic));
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
                ctx.log("Consumed records: " + records.count());
            }
        } catch (Throwable t) {
            ctx.log("Kafka container run failed (image pull or Docker): " + t.getMessage());
        }
        ctx.log("Talking point: Spring Kafka @KafkaListener wraps this; ackMode BATCH/MANUAL affects redelivery.");
    }
}
