package com.javagrunt.twitch.bot.alpha.commands;

import com.javagrunt.twitch.bot.alpha.TwitchMessage;
import org.springframework.stereotype.Component;

@Component
public class Counter extends BotCommand {
    private static final String COUNTER = "counter";
    private int counter;

    public Counter() {
        super();
        this.counter = 0;
    }

    @Override
    public String execute(String data, TwitchMessage tMessage) {
        this.counter++;
        return "The counter is currently at " + this.counter;
    }

    @Override
    public String commandName() {
        return COUNTER;
    }
}