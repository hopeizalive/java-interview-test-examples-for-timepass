package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l14.L14Application;
import com.example.microservices.interview.msdata.l14.L14NameOnly;
import com.example.microservices.interview.msdata.l14.L14Person;
import com.example.microservices.interview.msdata.l14.L14PersonRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/**
 * Lesson 14 demonstrates interface projections for read-optimized responses.
 *
 * <p>It highlights fetching only required columns instead of loading full entities for simple views.
 */
public final class Lesson14 extends AbstractMicroLesson {

    public Lesson14() {
        super(14, "Spring Data interface projections: fetch only columns needed for a read model.");
    }

    /**
     * Lesson 14: projection-based query shaping.
     *
     * <p><b>Purpose:</b> Show over-fetch reduction with interface projections.
     * <p><b>Role:</b> Supports API payload minimization and query efficiency.
     * <p><b>Demonstration:</b> Saves one person and reads projection containing name only.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L14Application.class, "ms14")) {
            L14PersonRepository repo = c.getBean(L14PersonRepository.class);
            L14Person p = new L14Person();
            p.setName("public");
            p.setSecretNote("hidden");
            repo.save(p);
            // Story observation: projection hides fields not exposed on interface contract.
            for (L14NameOnly row : repo.findAllBy()) {
                ctx.log("Projection name=" + row.getName() + " (secret not mapped on interface)");
            }
        }
        ctx.log("Talking point: projections reduce over-fetching compared to returning full entities.");
    }
}
