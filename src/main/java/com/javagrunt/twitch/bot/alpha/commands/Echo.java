package com.javagrunt.twitch.bot.alpha.commands;

import com.javagrunt.twitch.bot.alpha.TwitchMessage;
import org.springframework.stereotype.Component;

@Component
public class Echo extends BotCommand {
    private static final String ECHO = "echo";

    @Override
    public String execute(String data, TwitchMessage tMessage) {
        return "@" + tMessage.getSentBy() + " " + data;
    }

    @Override
    public String commandName() {
        return ECHO;
    }
}