package me.filipmajewski.discordbotintegrationplugin.dc_events;

import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

public class EmbedList {
    private final User bot;
    private final EmbedCreateSpec helpEmbed;
    private final EmbedCreateSpec reloadEmbed;
    private final EmbedCreateSpec stopEmbed;
    private final EmbedCreateSpec errorEmbed;
    private final EmbedCreateSpec whitelistPlayerErrorEmbed;
    private final EmbedCreateSpec whitelistOffErrorEmbed;
    private final EmbedCreateSpec setTimeEmbed;
    private final EmbedCreateSpec weatherClearEmbed;

    public EmbedList(User bot) {
        this.bot = bot;

        //Embeds setup
        this.errorEmbed = EmbedCreateSpec.builder()
                .color(Color.of(255, 36, 36))
                .author(bot.getUsername(), "", bot.getAvatarUrl())
                .addField("Error 01", "Command not found", false)
                .build();

        this.whitelistPlayerErrorEmbed = EmbedCreateSpec.builder()
                .color(Color.of(255, 36, 36))
                .author(bot.getUsername(), "", bot.getAvatarUrl())
                .addField("Error 05", "There is no such a player available", false)
                .build();

        this.whitelistOffErrorEmbed = EmbedCreateSpec.builder()
                .color(Color.of(255, 36, 36))
                .author(bot.getUsername(), "", bot.getAvatarUrl())
                .addField("Error 06", "Whitelist is disabled.", false)
                .build();

        this.helpEmbed = EmbedCreateSpec.builder()
                .color(Color.of(237, 183, 33))
                .author(bot.getUsername(), "", bot.getAvatarUrl())
                .addField("!dci", "Shows available discord commands", false)
                .addField("!dci whitelist add/remove <playerName>", "Add/Remove player in the whitelist (if enabled)", false)
                .addField("!dci stop", "Stop the server", false)
                .addField("!dci setday", "Set overworld time to day", false)
                .addField("!dci setnight", "Set overworld time to night", false)
                .addField("!dci weathercl", "Clear overworld weather", false)
                .addField("!stats <player>", "Displays specified player statistics", false)
                .build();

        this.reloadEmbed = EmbedCreateSpec.builder()
                .color(Color.of(255, 61, 36))
                .author(bot.getUsername(), "", bot.getAvatarUrl())
                .description("Reloading the server...")
                .build();

        this.stopEmbed = EmbedCreateSpec.builder()
                .color(Color.of(255, 61, 36))
                .author(bot.getUsername(), "", bot.getAvatarUrl())
                .description("Stopping the server...")
                .build();

        this.setTimeEmbed = EmbedCreateSpec.builder()
                .color(Color.of(18, 252, 80))
                .author(bot.getUsername(), "", bot.getAvatarUrl())
                .description("Time successfully changed.")
                .build();

        this.weatherClearEmbed = EmbedCreateSpec.builder()
                .color(Color.of(18, 252, 80))
                .author(bot.getUsername(), "", bot.getAvatarUrl())
                .description("Weather successfully changed.")
                .build();

    }

    public EmbedCreateSpec getErrorEmbed() {
        return errorEmbed;
    }

    public EmbedCreateSpec getWhitelistPlayerErrorEmbed() {
        return whitelistPlayerErrorEmbed;
    }

    public EmbedCreateSpec getHelpEmbed() {
        return helpEmbed;
    }

    public EmbedCreateSpec getReloadEmbed() {
        return reloadEmbed;
    }

    public EmbedCreateSpec getStopEmbed() {
        return stopEmbed;
    }

    public EmbedCreateSpec getSetTimeEmbed() {
        return setTimeEmbed;
    }

    public EmbedCreateSpec getWeatherClearEmbed() {
        return weatherClearEmbed;
    }

    public EmbedCreateSpec getWhitelistOffErrorEmbed() {
        return whitelistOffErrorEmbed;
    }
}
