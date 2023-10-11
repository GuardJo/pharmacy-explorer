package io.github.guardjo.pharmacyexplorer;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;

public abstract class AbstractTestContainerTest {
    static final MariaDBContainer MARIA_DB_CONTAINER;
    static final GenericContainer REDIS_CONTAINER;

    static {
        MARIA_DB_CONTAINER = new MariaDBContainer();
        REDIS_CONTAINER = new GenericContainer("redis:7")
                .withExposedPorts(6379);

        MARIA_DB_CONTAINER.start();
        REDIS_CONTAINER.start();
    }
}
