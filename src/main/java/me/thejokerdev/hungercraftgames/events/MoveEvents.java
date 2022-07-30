package me.thejokerdev.hungercraftgames.events;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvents implements Listener {
    private Main plugin;

    public MoveEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        SPlayer player = plugin.getPlayerManager().getPlayer(p);
        if (player == null){
            return;
        }
        if (player.getType() == PlayerType.PLAYER && plugin.isInLobby()){
            e.setCancelled(true);
        }
    }
}
