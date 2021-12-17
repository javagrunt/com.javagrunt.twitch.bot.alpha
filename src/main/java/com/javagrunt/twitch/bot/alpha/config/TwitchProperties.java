package com.javagrunt.twitch.bot.alpha.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "twitch")
@Data
public class TwitchProperties {
    public static final String TWITCH = "twitch";
    @NotBlank
    private String adminToken;
    @NotBlank
    private String username;
    @NotBlank
    private String oauthToken;
    @NotEmpty
    private String[] channels;
    @NotBlank
    private String clientId;
    @NotBlank
    private String clientSecret;
    private Integer authenticationTimeoutSeconds = 10;
    private Boolean debug;
}