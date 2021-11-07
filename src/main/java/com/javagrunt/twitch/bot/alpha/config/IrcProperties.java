package com.javagrunt.twitch.bot.alpha.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "irc")
@Data
public class IrcProperties {
    private String host = "irc.chat.twitch.tv";
    private Integer port = 6667;
}
