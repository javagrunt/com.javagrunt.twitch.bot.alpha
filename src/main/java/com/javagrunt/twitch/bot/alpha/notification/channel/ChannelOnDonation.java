package com.javagrunt.twitch.bot.alpha.notification.channel;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.DonationEvent;

public class ChannelOnDonation {
    public ChannelOnDonation(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(DonationEvent.class, this::onDonation);
    }

    /**
     * Subscribe to the Donation Event
     */
    private void onDonation(DonationEvent event) {
        String message = String.format(
                "%s just donated %s using %s!",
                event.getUser().getName(),
                event.getAmount(),
                event.getSource()
        );
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }
}