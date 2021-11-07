package com.javagrunt.twitch.bot.alpha;

import com.javagrunt.twitch.bot.alpha.commands.BotCommand;
import com.javagrunt.twitch.bot.alpha.config.TwitchProperties;
import com.javagrunt.twitch.bot.alpha.registry.CommandRegistry;
import com.javagrunt.twitch.bot.alpha.registry.metrics.CommandMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Set;

@Component
@EnableConfigurationProperties(TwitchProperties.class)
@Slf4j
public class TwitchBot {
    private final TwitchProperties twitchProperties;
    private final IRCConnection ircConnection;
    private final CommandRegistry commandRegistry;
    private final CommandMetrics commandMetrics;
    private Counter totalBotCommands_counter;
    private Boolean isAuthenticated;

    /**
     * Connect using the default hostname and port
     */
    public TwitchBot(TwitchProperties twitchProperties,
                     IRCConnection ircConnection,
                     CommandRegistry commandRegistry,
                     CommandMetrics commandMetrics,
                     Set<BotCommand> botCommandSet) {
        this.twitchProperties = twitchProperties;
        this.ircConnection = ircConnection;
        //MeterRegistry registry = new SimpleMeterRegistry();
        this.commandRegistry = commandRegistry;
        this.commandMetrics = commandMetrics;
        registerCommands(botCommandSet);
        initializeCommandCounter();
        // Enable metrics for schedulers
        Schedulers.enableMetrics();
        subscribe();
    }

    private void initializeCommandCounter(){
        // Build the counter to gather metrics
        this.totalBotCommands_counter = Counter.builder("botcommand_total_counter")
                .description("Total invocations of all bot commands")
                .register(Metrics.globalRegistry);
    }

    /**
     * Registers a new command for the bot to respond to
     *
     * @param botCommandSet collection of BotCommand implementations found
     */
    private void registerCommands(Set<BotCommand> botCommandSet) {
        for(BotCommand botCommand : botCommandSet) {
            this.commandRegistry.put(botCommand.commandName(), botCommand);
            // Build the counter to gather metrics
            Counter counter = Counter.builder("botcommand_" + botCommand.commandName() + "_counter")
                    .description("Invocation of the " + botCommand.commandName() + " command")
                    .register(Metrics.globalRegistry);
            this.commandMetrics.put(botCommand.commandName(), counter);
        }
    }

    private void subscribe() {
        Flux<String> messages = ircConnection.authorizeCommunication(twitchProperties.getOauthToken(), twitchProperties.getUsername());
        messages.metrics().subscribeOn(Schedulers.parallel()).subscribe(this::processMessage);
        waitForAuthentication();
    }

    /**
     * Waits until either the underlying IRC connection is authenticated or the timeout is reached
     *
     */
    private void waitForAuthentication() {
        int count = 0;
        while (count < 10) {
            if (this.isAuthenticated) {
                return;
            } else {
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * Basic message processing. Automatically handles Twitch API specific messages,
     * then attempts to see if any message is invoking a registered command
     *
     * @param message The message to process
     */
    private void processMessage(String message) {
        if (message.contains("Welcome, GLHF!")) {
            this.isAuthenticated = true;
            log.info("AUTHENTICATED TO SERVER");
        } else if (message.startsWith("PING")) {
            log.info("Responding to PING");
            ircConnection.pong(message);
        } else {
            // Not an internal command, see if it's a bot command
            TwitchMessage tMessage = new TwitchMessage(message);
            log.debug(tMessage.getSentBy() + ": " + tMessage.getMessage());
            if (tMessage.getMessage().startsWith("!")) {
                processCommand(tMessage);
            }
        }
    }

    /**
     * Invoked with a command message has been detected, look it up
     * in the registered commands to ensure it's valid, and then
     * execute it
     *
     * @param tMessage The TwitchMessage obeject containing the command
     */
    private void processCommand(TwitchMessage tMessage) {
        String command = tMessage.getMessage().split(" ")[0].replace("!", "");
        String data = tMessage.getMessage().replace("!" + command, "").trim();

        if (this.commandRegistry.containsKey(command)) {
            BotCommand botCommand = this.commandRegistry.get(command);
            ircConnection.sendChannelMessage(twitchProperties.getChannel(),
                    botCommand.execute(data, tMessage));

            // Increment the counters
            this.commandMetrics.get(command).increment();
            this.totalBotCommands_counter.increment();
        }else{
            log.error("Command not found");
        }
    }
}