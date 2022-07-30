package me.thejokerdev.hungercraftgames.events;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.xseries.messages.Titles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class DeathEvents implements Listener {
    private Main plugin;
    public static Team team;

    public DeathEvents(Main plugin) {
        this.plugin = plugin;
        team = plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam("spectators");
        if (team == null) {
            team = plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("spectators");
            team.setAllowFriendlyFire(false);
            team.setColor(ChatColor.GRAY);
            team.setCanSeeFriendlyInvisibles(false);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    public static void addEntry(Player p) {
        if (team.hasEntry(p.getName())) {
            return;
        }
        team.addEntry(p.getName());
    }

    public static void removeEntry(Player p) {
        if (!team.hasEntry(p.getName())) {
            return;
        }
        team.removeEntry(p.getName());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        SPlayer player = plugin.getPlayerManager().getPlayer(p);
        String message = e.getDeathMessage();
        e.setDeathMessage(null);
        e.setDroppedExp(0);
        if (player.getType() == PlayerType.PLAYER && !player.isDeath()){
            if (!Main.getPlugin().getConfig().getBoolean("settings.test")) {
                plugin.addMuerto(player);
                player.setDeath(true);
                onDie(p);
            }
            if (e.getEntity().getKiller() != null){
                Player t = e.getEntity().getKiller();
                e.setDeathMessage(plugin.getUtils().getMSG(p, "&6"+p.getName()+" &fha sido eliminado por &7"+t.getName()+"&f."));
                t.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5, 0, false, false));
            } else {
                e.setDeathMessage(plugin.getUtils().getMSG(p, "&6"+p.getName()+" &fha sido eliminado."));
            }
        } else {
            e.getDrops().clear();
            plugin.getUtils().sendMSGAStaff(message);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> p.spigot().respawn(), 5L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        e.setRespawnLocation(p.getLocation());
        SPlayer player = plugin.playerManager.getPlayer(p);
        if (player.getType() != PlayerType.PLAYER){
            player.setKit();
        }
    }

    public void onDie(Player p){
        for (SPlayer player : plugin.getPlayerManager().getPlayers().values()){
            if (player.getType() == PlayerType.PLAYER){
                if (player.getPlayer() != p){
                    player.getPlayer().hidePlayer(plugin, p);
                }
            }
        }
        Titles.sendTitle(p,  10, 100, 10, "§cHas muerto", "§fHas sido eliminado");
        p.setGameMode(GameMode.SPECTATOR);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        plugin.getUtils().sendMSG(p, "&cHas muerto.");
        /*plugin.getUtils().sendMSG(p, "&cSerás expulsado en 10 segundos.");
        new BukkitRunnable() {
            @Override
            public void run() {
                p.kickPlayer("Has muerto");
            }
        }.runTaskLater(plugin, 20L * 10);*/
    }
}
