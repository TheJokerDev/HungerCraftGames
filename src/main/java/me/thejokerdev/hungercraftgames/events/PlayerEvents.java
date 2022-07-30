package me.thejokerdev.hungercraftgames.events;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEvents implements Listener {
    private Main plugin;

    public PlayerEvents(Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        SPlayer player = plugin.getPlayerManager().getPlayer(p);
        if (player.getType() == PlayerType.PLAYER){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        SPlayer player = plugin.getPlayerManager().getPlayer(p);

        if (player == null){
            e.setCancelled(true);
        }
        if (player.getType() == PlayerType.PLAYER){
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onDrop (PlayerDropItemEvent e){
        Player p = e.getPlayer();
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(p);

        if (sPlayer == null){
            return;
        }

        if (sPlayer.getType() != PlayerType.PLAYER){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop (EntityPickupItemEvent e){
        if (!(e.getEntity() instanceof Player)){
            return;
        }
        Player p = (Player) e.getEntity();
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(p);
        if (sPlayer == null){
            return;
        }

        if (sPlayer.getType() != PlayerType.PLAYER){
            e.setCancelled(true);
        }
    }

}
