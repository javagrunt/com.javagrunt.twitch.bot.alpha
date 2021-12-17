package com.javagrunt.twitch.bot.alpha.command;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class JavagruntCommand {

    private static final String COMMAND = "!javagrunt";

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public JavagruntCommand(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    public void onChannelMessage(ChannelMessageEvent channelMessageEvent) {
        if(Objects.equals(channelMessageEvent.getMessage(), COMMAND)) {
            String message = "Allow myself to introduce myself: https://youtu.be/Vkd7g9BCNLs";
            channelMessageEvent.getTwitchChat().sendMessage(channelMessageEvent.getChannel().getName(), message);
        }
    }
}