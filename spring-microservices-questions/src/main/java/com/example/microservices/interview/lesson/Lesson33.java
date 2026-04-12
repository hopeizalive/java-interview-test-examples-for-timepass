package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

/** RabbitMQ + Spring AMQP: declare queue, send, receive (DLQ pattern described in log). */
public final class Lesson33 extends AbstractMicroLesson {

    public Lesson33() {
        super(33, "RabbitMQ Testcontainer: RabbitTemplate send/receive; DLQ via dead-letter exchange in real systems.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        if (!ctx.dockerAvailable()) {
            ctx.log("Skip: Docker not available for RabbitMQ Testcontainer.");
            return;
        }
        try (RabbitMQContainer rabbit = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-alpine"))) {
            rabbit.start();
            var cf = new CachingConnectionFactory(rabbit.getHost(), rabbit.getAmqpPort());
            cf.setUsername(rabbit.getAdminUsername());
            cf.setPassword(rabbit.getAdminPassword());
            var admin = new RabbitAdmin(cf);
            admin.declareQueue(new Queue("lesson33", true));
            var tpl = new RabbitTemplate(cf);
            tpl.convertAndSend("lesson33", "hello-amqp");
            Object got = tpl.receiveAndConvert("lesson33", 5000);
            ctx.log("Received: " + got);
        } catch (Throwable t) {
            ctx.log("RabbitMQ container failed: " + t.getMessage());
        }
        ctx.log("Talking point: bind DLX + DLQ for poison messages; Spring supports listener error handlers.");
    }
}
