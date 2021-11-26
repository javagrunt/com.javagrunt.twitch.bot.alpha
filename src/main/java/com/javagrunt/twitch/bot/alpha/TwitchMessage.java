package com.javagrunt.twitch.bot.alpha;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwitchMessage {
    private final String rawMessage;
    private String sentBy;
    private String message;
    private String channel;

    public TwitchMessage(String rawMessage) throws ArrayIndexOutOfBoundsException {
        this.rawMessage = rawMessage;
        parseMessage(rawMessage);
    }

    private void parseMessage(String rawMessage) throws ArrayIndexOutOfBoundsException {
        String[] splitMessage = rawMessage.split(":");

        // Parse metadata
        String[] splitMetadata = splitMessage[1].split(" ");
        this.sentBy = splitMetadata[0].split("!")[0];
        this.channel = splitMetadata[2].replace("#", "").trim();

        // Extract the message
        String metadata = ":" + splitMetadata[0] + " PRIVMSG " + "#" + this.channel + " :";
        this.message = this.rawMessage.replace(metadata, "");
    }

    public String getMessage() {
        return this.message;
    }

    public String getSentBy() {
        return this.sentBy;
    }

    public String getChannel() {
        return this.channel;
    }
}