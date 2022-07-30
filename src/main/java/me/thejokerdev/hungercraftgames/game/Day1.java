package me.thejokerdev.hungercraftgames.game;

import me.thejokerdev.hungercraftgames.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Day1 implements Listener {
    private Main plugin;

    public Day1(Main plugin){
        this.plugin = plugin;
    }

    boolean isDay(){
        return (plugin.getConfig().getInt("settings.day") == 1);
    }


    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (!isDay()){
            return;
        }
        if (e.getEntity() instanceof Player){
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL){
                e.setCancelled(true);
            }
        }
    }
}
