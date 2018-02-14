package com.minestom.Updater;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minestom.BossBarTimer;
import com.minestom.Utils.MessageUtil;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class Update {

    private final String REQUEST_URL;
    private final BossBarTimer plugin;
    private final Logger logger;
    private final int resourceId;

    public Update(BossBarTimer plugin, int resourceId) {
        this.REQUEST_URL = "https://api.spiget.org/v2/resources/" + resourceId + "/versions?sort=-releaseDate";
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.resourceId = resourceId;
    }

    public void sendUpdateMessage() {
        int newVersion = Integer.parseInt(getNewVersion().replaceAll("[^\\d]", ""));
        int currentVersion = Integer.parseInt(getCurrentVersion().replaceAll("[^\\d]", ""));

        logger.info("Looking for updates...");

        if (newVersion > currentVersion) {
            logger.info("There is a new update available.");
            logger.info("Current version " + getCurrentVersion());
            logger.info("Newest version " + getNewVersion());
            logger.info("Download the new version here: https://www.spigotmc.org/resources/" + resourceId);
        } else {
            logger.info("Plugin is up-to-date");
        }
    }

    public void sendUpdateMessage(Player player) {
        int newVersion = Integer.parseInt(getNewVersion().replaceAll("[^\\d]", ""));
        int currentVersion = Integer.parseInt(getCurrentVersion().replaceAll("[^\\d]", ""));

        if (newVersion > currentVersion) {
            String[] message = {"There is a new update available.", "Current version: &c" + getCurrentVersion(),
                    "Newest version: &c" + getNewVersion(), "Download the new version here:",
                    "https://www.spigotmc.org/resources/" + resourceId};
            MessageUtil.sendMessages(player, message, true);
        }
    }

    private String getNewVersion() {
        try {
            URL url = new URL(REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JsonElement element = new JsonParser().parse(reader).getAsJsonArray().get(0);
            JsonObject jsonObject = element.getAsJsonObject();

            return jsonObject.get("name").getAsString();

        } catch (IOException e) {
            logger.info("Error while checking for update.");
            logger.info(e.getMessage());
            return "0";
        }
    }

    private String getCurrentVersion() {
        return plugin.getDescription().getVersion();
    }
}
