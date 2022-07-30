package me.thejokerdev.hungercraftgames.menus;

import me.thejokerdev.hungercraftgames.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class CreateChests extends Menu {

    public CreateChests(Main plugin, Player player) {
        super(plugin, player, "CreateChests", "§cCreate Chests", 6);
    }
    @Override
    public void onOpen(InventoryOpenEvent var1) {

    }

    @Override
    public void onClose(InventoryCloseEvent var1) {
        getPlugin().getUtils().saveInventory(var1.getInventory());
    }

    @Override
    public void onClick(InventoryClickEvent var1) {
        var1.setCancelled(false);
    }

    @Override
    public void update() {

    }
}
