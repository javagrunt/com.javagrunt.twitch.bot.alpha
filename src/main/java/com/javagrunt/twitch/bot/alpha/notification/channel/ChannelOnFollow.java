package com.javagrunt.twitch.bot.alpha.notification.channel;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.FollowEvent;

public class ChannelOnFollow {
    public ChannelOnFollow(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(FollowEvent.class, this::onFollow);
    }

    /**
     * Subscribe to the Follow Event
     */
    private void onFollow(FollowEvent event) {
        String message = String.format(
                "%s is now following %s!",
                event.getUser().getName(),
                event.getChannel().getName()
        );

        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }
}