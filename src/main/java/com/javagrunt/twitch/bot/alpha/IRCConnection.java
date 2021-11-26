package com.javagrunt.twitch.bot.alpha;

import com.javagrunt.twitch.bot.alpha.config.IrcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.Stream;

@Component
@EnableConfigurationProperties(IrcProperties.class)
@Slf4j
public class IRCConnection {
    private final IrcProperties ircProperties;
    private PrintWriter out;
    private Socket sock;
    private Flux<String> messages;

    public IRCConnection(IrcProperties ircProperties) {
        this.ircProperties = ircProperties;
    }

    /**
     * Connect to the IRC server and create a Flux to stream messages to
     * any subscribers
     *
     * @throws IOException could be UnknownHostException
     */
    public void connect() throws IOException {
        sock = new Socket(ircProperties.getHost(), ircProperties.getPort());
        this.out = new PrintWriter(sock.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        Stream<String> inputStream = in.lines();
        messages = Flux.fromStream(inputStream);
    }

    public Boolean isConnected(){
        return sock.isConnected();
    }

    /**
     * Authorize against the configured IRC server
     *
     * @param oauth OAuth token for the account to authorize with
     * @param nick Username to authenticate with
     */
    public void authorizeCommunication(String oauth, String nick) {
        send("PASS " + oauth);
        send("NICK " + nick);
    }

    public Flux<String> getMessages(){
        return messages;
    }

    /**
     *
     * @param channel IRC/Twitch channel to join
     */
    public void joinChannel(String channel) {
        send("JOIN #" + channel);
    }

    public void pong(String message){
        send(message.replace("PING", "PONG"));
    }

    /**
     * Send a message to the channel the bot is currently joined to
     *
     * @param message Message to send
     */
    public void sendChannelMessage(String channel, String message) {
        send("PRIVMSG #" + channel + " :" + message);
    }

    private void send(String message) {
        this.out.println(message);
    }

}