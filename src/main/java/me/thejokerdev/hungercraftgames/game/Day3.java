package me.thejokerdev.hungercraftgames.game;

import lombok.Getter;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Day3 implements Listener {
    private Main plugin;
    List<Block> circleBlocks = new ArrayList<>();

    public Day3(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (plugin.getConfig().getInt("settings.day") != 3){
            return;
        }
        double damage = e.getDamage();
        double newDamage = damage * 0.35;
        e.setDamage(damage + newDamage);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (plugin.getConfig().getInt("settings.day") != 3){
            return;
        }
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null){
            return;
        }
        if (item.getType() == Material.AIR){
            return;
        }
        if (item.getType() == Material.PLAYER_HEAD){
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
                plugin.getUtils().sendMSG(p, "&a¡Lleva esta cabeza al centro y reclama tu recompensa!");
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if (plugin.getConfig().getInt("settings.day") != 3){
            return;
        }
        Player p = e.getPlayer();
        Location loc = p.getLocation();
        Location center = LocationUtil.getLocation(plugin.getConfig().getString("days.3.center"));
        int radius = plugin.getConfig().getInt("days.3.radius");
        List<Block> blocks = getSphere(center, radius);
        if (blocks.isEmpty()){
            return;
        }
        boolean isIn = loc.toVector().isInSphere(center.toVector(), radius);
        SPlayer sp = plugin.getPlayerManager().getPlayer(p);
        if ((isIn && sp.isInCenter())||(!isIn && !sp.isInCenter())){
            return;
        }

        if (sp.getType() == PlayerType.PLAYER){
            if (plugin.getUtils().getPlayerInCenter() == null){
                return;
            }
            p.setVelocity(p.getLocation().getDirection().multiply(-0.5));
        } else if (sp.getType() == PlayerType.ADMIN){
            if (isIn){
                sp.setInCenter(true);
                plugin.getUtils().sendMSG(p, "&a¡Has entrado en el centro!");
            } else {
                sp.setInCenter(false);
                plugin.getUtils().sendMSG(p, "&a¡Has salido del centro!");
            }
        }
    }

    private List<Block> getSphere(Location centerBlock, int radius) {
        if (!circleBlocks.isEmpty()){
            return circleBlocks;
        }
        if (centerBlock == null)
            return new ArrayList<>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < (radius * radius)) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(l.getBlock());
                    }
                }
            }
        }
        return circleBlocks;
    }

}
