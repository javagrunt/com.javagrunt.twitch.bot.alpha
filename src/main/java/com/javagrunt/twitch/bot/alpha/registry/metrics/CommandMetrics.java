package com.javagrunt.twitch.bot.alpha.registry.metrics;

import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CommandMetrics extends HashMap<String, Counter> {
}