package me.thejokerdev.hungercraftgames.game;

import me.thejokerdev.hungercraftgames.Main;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Day2 implements Listener {
    private Main plugin;

    public Day2(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (plugin.getConfig().getInt("settings.day") != 2){
            return;
        }
        double damage = e.getDamage();
        double newDamage = damage * 0.15;
        e.setDamage(damage + newDamage);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (plugin.getConfig().getInt("settings.day") != 2){
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
        if (item.isSimilar(plugin.getItemUtils().getBandage())){
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
                p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.5F, 1);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*5, 1, false, false));
                if (item.getAmount() > 1){
                    item.setAmount(item.getAmount() - 1);
                } else {
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
                }
                p.updateInventory();
            }
            e.setCancelled(true);
        }
    }

}
