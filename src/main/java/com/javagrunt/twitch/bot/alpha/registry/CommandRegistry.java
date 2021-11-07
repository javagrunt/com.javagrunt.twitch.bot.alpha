package com.javagrunt.twitch.bot.alpha.registry;

import com.javagrunt.twitch.bot.alpha.commands.BotCommand;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CommandRegistry extends HashMap<String, BotCommand> {
}
