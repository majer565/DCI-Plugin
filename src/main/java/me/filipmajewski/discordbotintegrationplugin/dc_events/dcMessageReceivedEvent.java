package me.filipmajewski.discordbotintegrationplugin.dc_events;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import me.filipmajewski.discordbotintegrationplugin.DiscordBotIntegrationPlugin;
import org.bukkit.ChatColor;
import reactor.core.publisher.Mono;

public class dcMessageReceivedEvent implements DCEventListener<MessageCreateEvent>{

    private final DiscordBotIntegrationPlugin plugin;
    private final MessageChannel textChannel;

    public dcMessageReceivedEvent(DiscordBotIntegrationPlugin plugin, MessageChannel textChannel) {
        this.plugin = plugin;
        this.textChannel = textChannel;
    }

    @Override
    public Class getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<?> execute(MessageCreateEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getMessage().getChannel().block();
        Member eventMember = event.getMember().get();
        String messageContent = message.getContent();

        if(channel.getId().equals(textChannel.getId()) && !eventMember.isBot()) {

            message.delete().block();
            textChannel.createMessage(
                    "**" + eventMember.getDisplayName() + "#" + eventMember.getDiscriminator()
                            + "**" + " >> " + checkString(messageContent, 1024)
            ).block();

            plugin.getServer().broadcastMessage(
                    ChatColor.BLUE + eventMember.getDisplayName() + "#" + eventMember.getDiscriminator()
                            + ChatColor.GRAY + " >> " + checkString(messageContent, 256) + ChatColor.RESET
            );
        }

        return Mono.empty();
    }

    private String checkString(String message, int charsToCut) {

        if(message.length() >= charsToCut) {
            return message.substring(0, charsToCut - 4) + "...";
        }
        return message;
    }
}
