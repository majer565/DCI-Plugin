package me.filipmajewski.discordbotintegrationplugin.dc_events;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import me.filipmajewski.discordbotintegrationplugin.DiscordBotIntegrationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import reactor.core.publisher.Mono;

public class dcCommandReceivedEvent implements DCEventListener<MessageCreateEvent>{

    private final DiscordBotIntegrationPlugin plugin;
    private final MessageChannel commandsChannel;
    private final EmbedList embedList;

    public dcCommandReceivedEvent(DiscordBotIntegrationPlugin plugin, MessageChannel commandsChannel, EmbedList embedList) {
        this.plugin = plugin;
        this.commandsChannel = commandsChannel;
        this.embedList = embedList;
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<?> execute(MessageCreateEvent event) {
        Message commandMessage = event.getMessage();
        MessageChannel channel = event.getMessage().getChannel().block();
        String[] args = commandMessage.getContent().split(" ");

        if(channel.getId().equals(commandsChannel.getId())) {
            if(args[0].equalsIgnoreCase("!dci")) {
                if(args.length == 1) {
                    event.getMessage().getChannel()
                            .flatMap(ch -> ch.createMessage(embedList.getHelpEmbed()))
                            .block();
                    return Mono.empty();
                } else {
                    switch(args[1]) {
                        case "stop":

                            plugin.getServer().shutdown();
                            event.getMessage().getChannel()
                                    .flatMap(ch -> ch.createMessage(embedList.getStopEmbed()))
                                    .block();
                            break;
                        case "whitelist":
                            if(plugin.getServer().hasWhitelist()) {
                                if(args.length == 4) {
                                    Player player = Bukkit.getPlayer(args[3]);
                                    switch(args[2]) {
                                        case "add":
                                            if(player != null) player.setWhitelisted(true);
                                            else {
                                                event.getMessage().getChannel()
                                                        .flatMap(ch -> ch.createMessage(embedList.getWhitelistPlayerErrorEmbed()))
                                                        .block();
                                            }
                                            break;
                                        case "remove":
                                            if(player != null) player.setWhitelisted(false);
                                            else {
                                                event.getMessage().getChannel()
                                                        .flatMap(ch -> ch.createMessage(embedList.getWhitelistPlayerErrorEmbed()))
                                                        .block();
                                            }
                                            break;
                                        default:
                                            event.getMessage().getChannel()
                                                    .flatMap(ch -> ch.createMessage(embedList.getErrorEmbed()))
                                                    .block();
                                    }
                                } else {
                                    event.getMessage().getChannel()
                                            .flatMap(ch -> ch.createMessage(embedList.getErrorEmbed()))
                                            .block();
                                }
                            } else {
                                event.getMessage().getChannel()
                                        .flatMap(ch -> ch.createMessage(embedList.getWhitelistOffErrorEmbed()))
                                        .block();
                            }
                            break;
                        case "setday":

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    plugin.getServer().getWorlds().get(0).setTime(1000L);
                                }
                            }.runTask(plugin);

                            event.getMessage().getChannel()
                                    .flatMap(ch -> ch.createMessage(embedList.getSetTimeEmbed()))
                                    .block();
                            break;
                        case "setnight":

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    plugin.getServer().getWorlds().get(0).setTime(13000L);
                                }
                            }.runTask(plugin);

                            event.getMessage().getChannel()
                                    .flatMap(ch -> ch.createMessage(embedList.getSetTimeEmbed()))
                                    .block();
                            break;
                        case "weathercl":
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    plugin.getServer().getWorlds().get(0).setStorm(false);
                                }
                            }.runTask(plugin);

                            event.getMessage().getChannel()
                                    .flatMap(ch -> ch.createMessage(embedList.getWeatherClearEmbed()))
                                    .block();
                            break;
                        default:
                            event.getMessage().getChannel()
                                    .flatMap(ch -> ch.createMessage(embedList.getErrorEmbed()))
                                    .block();
                    }
                }
            }
        }

        return Mono.empty();
    }
}
