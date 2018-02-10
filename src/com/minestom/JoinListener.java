package com.minestom;

import com.minestom.DataHandler.BarsData;
import com.minestom.Utils.BossBarManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class JoinListener implements Listener {

    private BossbarTimer plugin;

    public JoinListener(BossbarTimer plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Map<String, Long> timer = plugin.getTimer();
        for (Map.Entry<String, Long> entry : timer.entrySet()) {
            BarsData barsData = plugin.getBarDataMap().get(entry.getKey());
            BossBarManager barManager = barsData.getBossBarManager();
            barManager.addPlayer(player);
        }
        sendUpdate(player);
    }

    private void sendUpdate(Player player) {
        if (player.hasPermission("bossbartimer.update.notification"))
            plugin.getUpdate().sendUpdateMessage(player);
    }

}
