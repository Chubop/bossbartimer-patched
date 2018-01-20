package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.PlayerEditingData;
import com.minestom.Utils.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditMenu implements Listener {

    private BossbarTimer plugin;

    public EditMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();
        if (inventoryName.equals("Edit Mode") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {
            ItemStack item = event.getCurrentItem();
            if (item == null) {
                return;
            }
            event.setCancelled(true);

            Utilities utilities = plugin.getUtilities();

            Player player = (Player) event.getWhoClicked();
            PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);

            int slot = event.getRawSlot();

            if (slot == 0 && item.hasItemMeta()) {
                BossbarMenuMaker.createColorMenu(player);
            }
            if (slot == 1 && item.hasItemMeta()) {
                BossbarMenuMaker.createStyleMenu(player);
            }
            if (slot == 2 && item.hasItemMeta()) {
                List<String> lore = new ArrayList<>();

                for (String cmds : editingData.getBarValue("DisplayName").split(", ")) {
                    if (editingData.getBarValue("DisplayName").isEmpty()) continue;
                    lore.add(cmds.replaceAll("[\\[\\]]", ""));
                }
                if (event.getClick() == ClickType.LEFT) {
                    player.closeInventory();
                    editingData.setEditing(false);
                    editingData.setEditingName(true);
                    MessageUtil.sendMessage(player, "&aEnter the next frame for the display name of the bar in the chat. Use &eCancel &ato cancel.");
                    MessageUtil.sendMessage(player, "&7TIP: You can use '{time}' to show the time left!");
                }
                if (event.getClick() == ClickType.RIGHT) {

                    if (!lore.isEmpty()) {
                        lore.remove(lore.size() - 1);
                        editingData.addBarValue("DisplayName", lore.toString());
                    }
                    if (lore.size() == 0) {
                        lore.clear();
                        editingData.addBarValue("DisplayName", "");
                    }
                    BossbarMenuMaker.createEditMenu(player, plugin);

                    utilities.setFrames(lore);
                }
                if (event.getClick() == ClickType.SHIFT_LEFT) {

                    lore.clear();
                    editingData.addBarValue("DisplayName", "");
                    BossbarMenuMaker.createEditMenu(player, plugin);

                    utilities.setFrames(Arrays.asList("&cExample &fText", "&fExample &cText"));

                }
                if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    editingData.setEditPeriod(true);
                    MessageUtil.sendMessage(player, "&aEnter the period time of the name bar in the chat &7(time in ticks)&a. Use &eCancel &ato cancel.");
                    player.closeInventory();
                }
            }
            if (slot == 3 && item.hasItemMeta()) {
                BossbarMenuMaker.createAvancedMenu(player, plugin);
            }
            if (slot == 4 && item.hasItemMeta()) {

                if (event.getClick() == ClickType.SHIFT_LEFT) {

                    editingData.setEditing(false);
                    editingData.setCanceling(true);
                    editingData.setConfirm(true);

                    BossbarMenuMaker.createConfimMenu(player);

                }
                if (event.getClick() == ClickType.LEFT) {

                    editingData.setEditing(false);
                    editingData.setSaving(true);
                    editingData.setConfirm(true);

                    BossbarMenuMaker.createConfimMenu(player);

                }
            }
        }
    }
}
