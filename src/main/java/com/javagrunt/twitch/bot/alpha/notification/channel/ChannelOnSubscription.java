package com.javagrunt.twitch.bot.alpha.notification.channel;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;

public class ChannelOnSubscription {
    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public ChannelOnSubscription(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(SubscriptionEvent.class, this::onSubscription);
    }

    /**
     * Subscribe to the Subscription Event
     */
    private void onSubscription(SubscriptionEvent event) {
        String message = "";

        // New Subscription
        if (event.getMonths() <= 1) {
            message = String.format(
                    "%s has subscribed to %s!",
                    event.getUser().getName(),
                    event.getChannel().getName()
            );
        }
        // Renewal Subscription
        if (event.getMonths() > 1) {
            message = String.format(
                    "%s has subscribed to %s in for %s months!",
                    event.getUser().getName(),
                    event.getChannel().getName(),
                    event.getMonths()
            );
        }

        // Send Message
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }
}