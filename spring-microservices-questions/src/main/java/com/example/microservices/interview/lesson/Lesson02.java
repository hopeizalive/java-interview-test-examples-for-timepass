package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Lesson 2 demonstrates bounded contexts through isolated Spring configurations.
 *
 * <p>Inventory and shipping are modeled as separate context roots to emphasize domain language and
 * ownership boundaries.
 */
public final class Lesson02 extends AbstractMicroLesson {

    public Lesson02() {
        super(2, "Bounded contexts: inventory and shipping as isolated AnnotationConfigApplicationContext beans.");
    }

    /**
     * Lesson 2: bounded context separation.
     *
     * <p><b>Purpose:</b> Show that distinct domains should own independent policy models.
     * <p><b>Role:</b> Reinforces service boundary thinking after lesson 1 trade-offs.
     * <p><b>Demonstration:</b> Boots two contexts and logs each domain policy bean.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: load inventory and shipping in separate containers.
        try (var inv = new AnnotationConfigApplicationContext(InventoryBoundary.class);
                var ship = new AnnotationConfigApplicationContext(ShippingBoundary.class)) {
            ctx.log("Inventory policy bean: " + inv.getBean(InventoryPolicy.class).rule());
            ctx.log("Shipping policy bean: " + ship.getBean(ShippingPolicy.class).rule());
        }
        // Story takeaway: boundaries should follow business language, not technical convenience.
        ctx.log("Talking point: align service boundaries with domain language (DDD), not only technical layers.");
    }

    @Configuration
    static class InventoryBoundary {
        @Bean
        InventoryPolicy inventoryPolicy() {
            return () -> "reserve-stock";
        }
    }

    @Configuration
    static class ShippingBoundary {
        @Bean
        ShippingPolicy shippingPolicy() {
            return () -> "route-parcel";
        }
    }

    @FunctionalInterface
    interface InventoryPolicy {
        String rule();
    }

    @FunctionalInterface
    interface ShippingPolicy {
        String rule();
    }
}
