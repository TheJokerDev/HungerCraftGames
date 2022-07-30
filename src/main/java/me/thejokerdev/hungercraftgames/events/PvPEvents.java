package me.thejokerdev.hungercraftgames.events;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPEvents implements Listener {
    private Main plugin;

    public PvPEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e){
        Entity a = e.getEntity();
        Entity b = e.getDamager();
        if (a instanceof Player && b instanceof Player){
            Player p = (Player)a;
            Player t = (Player)b;
            SPlayer player = plugin.getPlayerManager().getPlayer(t);
            if (player == null){
                e.setCancelled(true);
                return;
            }
            if (!plugin.pvp){
                if (player.getType() == PlayerType.PLAYER){
                    e.setCancelled(true);
                }
            }
        }
    }
}
