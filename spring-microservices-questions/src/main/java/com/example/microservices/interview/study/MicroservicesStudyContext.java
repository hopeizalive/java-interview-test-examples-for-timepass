package com.example.microservices.interview.study;

import org.testcontainers.DockerClientFactory;

/** Shared resources for microservices CLI lessons (e.g. run-all). */
public final class MicroservicesStudyContext {

    public void log(String message) {
        System.out.println(message);
    }

    public boolean dockerAvailable() {
        try {
            return DockerClientFactory.instance().isDockerAvailable();
        } catch (Throwable t) {
            return false;
        }
    }
}
