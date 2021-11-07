package com.javagrunt.twitch.bot.alpha.commands;

import com.javagrunt.twitch.bot.alpha.TwitchMessage;

public abstract class BotCommand {

    public abstract String commandName();

    public BotCommand() {
    }

    public String execute(String data, TwitchMessage tMessage) {
        return "Method Not Implemented";
    }
}