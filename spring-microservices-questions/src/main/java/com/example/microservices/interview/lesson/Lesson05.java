package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MinimalJacksonWebConfig;
import com.example.microservices.interview.support.SimpleWebHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** REST resources, status codes, idempotent PUT/DELETE semantics. */
public final class Lesson05 extends AbstractMicroLesson {

    public Lesson05() {
        super(5, "REST API: 201 on create semantics, 404 missing resource, idempotent PUT/DELETE.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (SimpleWebHarness h = new SimpleWebHarness(MinimalJacksonWebConfig.class, WebConfig.class)) {
            var mvc = h.mockMvc();
            mvc.perform(put("/items/1").contentType("application/json").content("{\"name\":\"a\"}"))
                    .andExpect(status().isCreated());
            mvc.perform(put("/items/1").contentType("application/json").content("{\"name\":\"a\"}"))
                    .andExpect(status().isOk());
            mvc.perform(get("/items/99")).andExpect(status().isNotFound());
            mvc.perform(delete("/items/1")).andExpect(status().isNoContent());
            mvc.perform(delete("/items/1")).andExpect(status().isNoContent());
        }
        ctx.log("Talking point: PUT to a known id is upsert/idempotent; DELETE twice should be safe (404 vs 204—pick team convention).");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        ItemApi itemApi() {
            return new ItemApi();
        }
    }

    @RestController
    static class ItemApi {
        private final Map<Long, String> store = new ConcurrentHashMap<>();

        @PutMapping("/items/{id}")
        ResponseEntity<Void> put(@PathVariable("id") long id, @RequestBody Map<String, String> body) {
            boolean created = !store.containsKey(id);
            store.put(id, body.get("name"));
            return created ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.ok().build();
        }

        @GetMapping("/items/{id}")
        ResponseEntity<String> get(@PathVariable("id") long id) {
            return store.containsKey(id)
                    ? ResponseEntity.ok(store.get(id))
                    : ResponseEntity.notFound().build();
        }

        @DeleteMapping("/items/{id}")
        ResponseEntity<Void> delete(@PathVariable("id") long id) {
            store.remove(id);
            return ResponseEntity.noContent().build();
        }
    }
}
