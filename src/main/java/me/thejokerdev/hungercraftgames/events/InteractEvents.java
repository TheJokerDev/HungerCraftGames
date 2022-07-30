package me.thejokerdev.hungercraftgames.events;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvents implements Listener {
    private Main plugin;

    public InteractEvents (Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        SPlayer player = plugin.getPlayerManager().getPlayer(p);
        ItemStack item = e.getItem();
        if (item == null){
            return;
        }
        if (item.isSimilar(plugin.getItemUtils().mic_on())){
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                player.setBroadcast(false);
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), plugin.getItemUtils().mic_off());
                p.updateInventory();
            }
            e.setCancelled(true);
            return;
        }
        if (item.isSimilar(plugin.getItemUtils().mic_off())){
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                player.setBroadcast(true);
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), plugin.getItemUtils().mic_on());
                p.updateInventory();
            }
            e.setCancelled(true);
        }
    }
}
