package com.javagrunt.twitch.bot.alpha.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = "twitch")
@Data
public class TwitchProperties {
    public static final String TWITCH = "twitch";
    @NotBlank
    private String username;
    @NotBlank
    private String oauthToken;
    @NotBlank
    private String channel;
    @NotBlank
    private String clientId;
    @NotBlank
    private String clientSecret;
    private Integer authenticationTimeoutSeconds = 10;
}