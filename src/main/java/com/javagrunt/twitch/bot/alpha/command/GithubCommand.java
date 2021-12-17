package com.javagrunt.twitch.bot.alpha.command;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class GithubCommand {

    private static final String COMMAND = "!github";

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public GithubCommand(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    public void onChannelMessage(ChannelMessageEvent channelMessageEvent) {
        if(Objects.equals(channelMessageEvent.getMessage(), COMMAND)) {
            String message = "Github: https://github.com/dashaun";
            channelMessageEvent.getTwitchChat().sendMessage(channelMessageEvent.getChannel().getName(), message);
        }
    }
}