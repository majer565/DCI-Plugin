package me.filipmajewski.discordbotintegrationplugin.dc_events;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import me.filipmajewski.discordbotintegrationplugin.DiscordBotIntegrationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StatsMessageEvent implements DCEventListener<MessageCreateEvent>{

    private final DiscordBotIntegrationPlugin plugin;

    public StatsMessageEvent(DiscordBotIntegrationPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<?> execute(MessageCreateEvent event) {
        Message eventMessage = event.getMessage();
        String[] message = eventMessage.getContent().split(" ");

        if(message.length == 2 && message[0].equalsIgnoreCase("!stats")) {
            Player player = plugin.getServer().getPlayer(message[1]);
            if(player == null) player = Bukkit.getOfflinePlayer(message[1]).getPlayer();

            String isOnline;
            List<Advancement> advancements = new ArrayList<>();
            List<Advancement> playerAdvancements = new ArrayList<>();

            for (Iterator<Advancement> it = Bukkit.advancementIterator(); it.hasNext(); ) {
                Advancement progress = it.next();
                advancements.add(progress);
                if(player.getAdvancementProgress(progress).isDone()) {
                    playerAdvancements.add(progress);
                }
            }


            if(player != null) {
                if(player.isOnline()) isOnline = "ONLINE";
                else isOnline = "OFFLINE";

                long timePlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20;

                EmbedCreateSpec statsEmbed = EmbedCreateSpec.builder()
                        .color(Color.of(237, 183, 33))
                        .title("Player: " + player.getDisplayName())
                        .description("Status: " + isOnline +
                                     "\nPing: " + player.getPing() +
                                     "\nAdvancements: " + playerAdvancements.size() + "/" + advancements.size() +
                                     "\nOnline time: " + calculateTime(timePlayed)
                        )
                        .build();

                event.getMessage().getChannel()
                        .flatMap(channel -> channel.createMessage(statsEmbed))
                        .block();
            } else {
                //add playererror
            }

        }

        return Mono.empty();
    }

    private String calculateTime(long time) {
        String calculatedTime = "";
        long days = time / 86400;
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;

        calculatedTime = days + "D : " + hours + "H : " + minutes + "M";
        return calculatedTime;
    }
}
