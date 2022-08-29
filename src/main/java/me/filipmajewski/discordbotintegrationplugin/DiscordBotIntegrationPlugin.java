package me.filipmajewski.discordbotintegrationplugin;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import me.filipmajewski.discordbotintegrationplugin.dc_events.*;
import me.filipmajewski.discordbotintegrationplugin.spigot_events.MessageReceivedEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordBotIntegrationPlugin extends JavaPlugin {

    private final String DISCORD_BOT_TOKEN = getConfig().getString("discord_bot_token");
    private boolean isDiscordBotConnected;
    private GatewayDiscordClient client;
    private User bot;
    private EmbedList embedList;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        try {
            client = DiscordClientBuilder.create(DISCORD_BOT_TOKEN).build().login().block();
            bot = client.getSelf().block();
            embedList = new EmbedList(bot);
            isDiscordBotConnected = true;
            System.out.println("\u001b[32m" + "[DiscordBotIntegrationPlugin] Successfully loaded discord bot.\u001b[0m");
        } catch (Exception e) {
            System.out.println("\u001b[31m" + "[DiscordBotIntegrationPlugin] Error! You did not provide correct discord"
                    + " token! Edit token in config file.\u001b[0m"
            );
            getServer().getPluginManager().disablePlugin(this);
        }

        if (isDiscordBotConnected) {
            Guild guild = client.getGuildById(Snowflake.of(getConfig().getString("discord_guild_ID"))).block();
            MessageChannel messageChannel = (MessageChannel) guild.getChannelById(Snowflake.of(getConfig().getString("discord_messages_textchannel_ID"))).block();
            MessageChannel commandsChannel = (MessageChannel) guild.getChannelById(Snowflake.of(getConfig().getString("discord_commands_textchannel_ID"))).block();
            getServer().getPluginManager().registerEvents(
                    new MessageReceivedEvent(messageChannel),
                    this
            );

            DiscordBotIntegrationPlugin.register(client, new dcMessageReceivedEvent(this, messageChannel));
            DiscordBotIntegrationPlugin.register(client, new dcCommandReceivedEvent(this, commandsChannel, embedList));
            DiscordBotIntegrationPlugin.register(client, new StatsMessageEvent(this));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (isDiscordBotConnected) {
            client.logout();
            isDiscordBotConnected = false;
            System.out.println("[DiscordBotIntegrationPlugin] Logging out...");
        }
    }

    private static <T extends Event> void register(GatewayDiscordClient gateway, DCEventListener<T> eventListener) {
        gateway.getEventDispatcher()
                .on(eventListener.getEventType())
                .flatMap(eventListener::execute)
                .subscribe();
    }

    public User getBot() {
        return this.bot;
    }
}
