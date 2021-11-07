package com.javagrunt.twitch.bot.alpha.commands;

import com.javagrunt.twitch.bot.alpha.TwitchMessage;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Dice extends BotCommand {
    private static final String DICE = "dice";

    private Random rand;

    public Dice() {
        super();
        this.rand = new Random();
    }

    @Override
    public String execute(String data, TwitchMessage tMessage) {
        return "@" + tMessage.getSentBy() + " You rolled: " + (rand.nextInt(6) + 1);
    }

    @Override
    public String commandName() {
        return DICE;
    }
}