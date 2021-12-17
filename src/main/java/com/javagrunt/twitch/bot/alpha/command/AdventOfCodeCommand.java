package com.javagrunt.twitch.bot.alpha.command;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class AdventOfCodeCommand {

    private static final String COMMAND = "!aoc";

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public AdventOfCodeCommand(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    public void onChannelMessage(ChannelMessageEvent channelMessageEvent) {
        if(Objects.equals(channelMessageEvent.getMessage(), COMMAND)) {
            String message = "Advent of code is a series of puzzles which come out daily as an advent calendar in December -- for more information visit: https://adventofcode.com/";
            channelMessageEvent.getTwitchChat().sendMessage(channelMessageEvent.getChannel().getName(), message);
        }
    }
}