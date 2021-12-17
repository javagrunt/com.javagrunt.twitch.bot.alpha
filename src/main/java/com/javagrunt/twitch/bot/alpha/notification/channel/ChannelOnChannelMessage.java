package com.javagrunt.twitch.bot.alpha.notification.channel;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class ChannelOnChannelMessage {
    public ChannelOnChannelMessage(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    /**
     * Subscribe to the Donation Event
     */
    private void onChannelMessage(ChannelMessageEvent channelMessageEvent) {
        if (!channelMessageEvent.getMessage().startsWith("!")) {
            if (!channelMessageEvent.getUser().getName().equals("streamelements")) {
                String message = String.format(
                        "ECHO :: Channel [%s] - User [%s] - Message [%s]%n",
                        channelMessageEvent.getChannel().getName(),
                        channelMessageEvent.getUser().getName(),
                        channelMessageEvent.getMessage()
                );
                channelMessageEvent.getTwitchChat().sendMessage(channelMessageEvent.getChannel().getName(), message);
            }
        }
    }
}