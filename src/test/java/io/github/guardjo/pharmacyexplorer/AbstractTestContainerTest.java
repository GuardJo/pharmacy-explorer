package io.github.guardjo.pharmacyexplorer;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;

public abstract class AbstractTestContainerTest {
    static final MariaDBContainer MARIA_DB_CONTAINER;
    static final GenericContainer REDIS_CONTAINER;

    static {
        MARIA_DB_CONTAINER = new MariaDBContainer();
        REDIS_CONTAINER = new GenericContainer("redis:7")
                .withAccessToHost(true)
                .withExposedPorts(6379);

        MARIA_DB_CONTAINER.start();
        REDIS_CONTAINER.start();

        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }
}
