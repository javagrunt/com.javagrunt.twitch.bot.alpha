package com.javagrunt.twitch.bot.alpha;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.BitsLeaderboard;
import com.github.twitch4j.helix.domain.FollowList;
import com.github.twitch4j.helix.domain.ModeratorList;
import com.javagrunt.twitch.bot.alpha.command.*;
import com.javagrunt.twitch.bot.alpha.config.TwitchProperties;
import com.javagrunt.twitch.bot.alpha.notification.channel.ChannelOnDonation;
import com.javagrunt.twitch.bot.alpha.notification.channel.ChannelOnFollow;
import com.javagrunt.twitch.bot.alpha.notification.channel.ChannelOnSubscription;
import com.javagrunt.twitch.bot.alpha.notification.console.ConsoleOnMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@EnableConfigurationProperties(TwitchProperties.class)
@Slf4j
public class AlphaBot implements InitializingBean {

    private final TwitchClient twitchClient;
    private final TwitchProperties twitchProperties;


    public AlphaBot(TwitchProperties twitchProperties) {
        twitchClient = TwitchClientBuilder.builder()
                .withClientId(twitchProperties.getClientId())
                .withClientSecret(twitchProperties.getClientSecret())
                .withEnableHelix(true)
                .withChatAccount(new OAuth2Credential(TwitchProperties.TWITCH, twitchProperties.getOauthToken()))
                .withEnableChat(true)
                .withEnableGraphQL(false)
                .withEnableKraken(true)
                .build();
        this.twitchProperties = twitchProperties;
    }

    @Override
    public void afterPropertiesSet() {
        registerFeatures();
        start();
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        for (String channel : twitchProperties.getChannels()) {
            twitchClient.getChat().sendMessage(channel, "Bye!");
        }
    }

    private void registerFeatures() {
        SimpleEventHandler eventHandler = twitchClient
                .getEventManager()
                .getEventHandler(SimpleEventHandler.class);

        // TODO This is dirty and wrong
        new ChannelOnDonation(eventHandler);
        new ChannelOnFollow(eventHandler);
        new ChannelOnSubscription(eventHandler);
        new ConsoleOnMessage(eventHandler);
        new TimeCommand(eventHandler);
        new DiscordCommand(eventHandler);
        new TwitterCommand(eventHandler);
        new GithubCommand(eventHandler);
        new GitlabCommand(eventHandler);
        new LurkCommand(eventHandler);
        new AdventOfCodeCommand(eventHandler);
        new JavagruntCommand(eventHandler);
    }

    private void start() {
        for (String channel : twitchProperties.getChannels()) {
            twitchClient.getChat().joinChannel(channel);
            twitchClient.getClientHelper().enableStreamEventListener(channel);
            twitchClient.getClientHelper().enableFollowEventListener(channel);
            twitchClient.getChat().sendMessage(channel, "Yo!");
        }

    }

    private void printBitsLeaderboard(String channel) {
        BitsLeaderboard resultList = twitchClient.getHelix().getBitsLeaderboard(twitchProperties.getAdminToken(), 10, "all", null, null).execute();
        resultList.getEntries().forEach(entry -> log.info(entry.getRank() + ": " + entry.getUserId()));
    }

    private void printModerators(String channel){
        ModeratorList resultList = twitchClient.getHelix().getModerators(twitchProperties.getAdminToken(), channel, null, null, 10).execute();
        resultList.getModerators().forEach(moderator -> log.info(moderator.getUserName()));
    }

    private void printFollowers(String channel) {
        FollowList resultList = twitchClient.getHelix().getFollowers(twitchProperties.getAdminToken(), null, null, null, 100).execute();

        resultList.getFollows().forEach(follow -> {
            log.info(follow.getFromName() + " is following " + follow.getToName());
        });
    }

}