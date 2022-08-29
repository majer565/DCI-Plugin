package me.filipmajewski.discordbotintegrationplugin.dc_events;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public interface DCEventListener<T extends Event>{
    Class<T> getEventType();
    Mono<?> execute(T event);
}
