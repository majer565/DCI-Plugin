package me.filipmajewski.discordbotintegrationplugin.spigot_events;

import discord4j.core.object.entity.channel.MessageChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageReceivedEvent implements Listener {

    private final MessageChannel textchannel;

    public MessageReceivedEvent(MessageChannel textchannel) {
        this.textchannel = textchannel;
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onMessageReceived(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        message = message.replaceAll("@everyone", "`@everyone`").replaceAll("@here", "`@here`");
        Player player = event.getPlayer();

        textchannel.createMessage("**" + player.getDisplayName() + "**" + ">> " + message).block();
    }
}
