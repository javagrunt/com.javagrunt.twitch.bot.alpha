package com.javagrunt.twitch.bot.alpha.command;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class LurkCommand {

    private static final String COMMAND = "!lurk";

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public LurkCommand(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    public void onChannelMessage(ChannelMessageEvent channelMessageEvent) {
        if(Objects.equals(channelMessageEvent.getMessage(), COMMAND)) {
            String message = "Let's all let " + channelMessageEvent.getUser().getName() + " lurk in peace.";
            channelMessageEvent.getTwitchChat().sendMessage(channelMessageEvent.getChannel().getName(), message);
        }
    }
}