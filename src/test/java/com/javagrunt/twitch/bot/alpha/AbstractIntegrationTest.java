package com.javagrunt.twitch.bot.alpha;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractIntegrationTest {

    static {
        GenericContainer<?> ircd = new GenericContainer<>("inspircd/inspircd-docker")
                .withExposedPorts(6667);
        ircd.start();
        System.setProperty("irc.host", ircd.getContainerIpAddress());
        System.setProperty("irc.port", ircd.getFirstMappedPort() + "");
    }
}