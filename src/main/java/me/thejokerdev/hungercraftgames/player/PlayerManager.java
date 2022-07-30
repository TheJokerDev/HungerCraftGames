package me.thejokerdev.hungercraftgames.player;

import lombok.Getter;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.events.DeathEvents;
import me.thejokerdev.hungercraftgames.game.District;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PlayerManager implements Listener {
    private final Main plugin;
    private final HashMap<UUID, SPlayer> players;
    Team team;

    public PlayerManager(Main plugin){
        this.plugin = plugin;
        players = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        team = plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam("backend");
        if (team == null){
            team = plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("backend");
            team.setAllowFriendlyFire(false);
            team.setColor(ChatColor.GOLD);
            team.setCanSeeFriendlyInvisibles(true);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    public SPlayer getPlayer(String name){
        Player t = Bukkit.getPlayer(name);
        if (t == null){
            return null;
        }
        return getPlayer(t);
    }

    public SPlayer getPlayer(UUID uniqueId){
        return players.getOrDefault(uniqueId, null);
    }

    public SPlayer getPlayer(Player p){
        return getPlayer(p.getUniqueId());
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent e){
        String name = e.getName();
        /*if (plugin.isMuerto(name)){
            String msg = plugin.getUtils().ct("&c¡Has sido eliminado de los HungerCraftGames!");
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(msg);
        }*/
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        String msg = e.getJoinMessage();
        e.setJoinMessage(null);
        plugin.getUtils().sendMSGAStaff(msg);

        SPlayer player = new SPlayer(p);
        players.put(p.getUniqueId(), player);

        if (plugin.getStaff().contains(p.getName())){
            player.setType(PlayerType.STAFF);
            staffJoin(p);
        } else if (plugin.getAdmins().contains(p.getName())) {
            player.setType(PlayerType.ADMIN);
            staffJoin(p);
        }
        if (player.getType() == PlayerType.PLAYER){
            /*int max = plugin.getMaxPlayers();
            if (plugin.getJugadorSize() == max){
                p.kickPlayer("Jugadores completos!");
                return;
            }*/
            hideStaff(p);
            String name = p.getName();
            if (!plugin.getDistrictsManager().contains(name)){
                System.out.println("No district found for " + name);
                player.setDeath(true);
                p.setGameMode(GameMode.SPECTATOR);
                for (SPlayer t : plugin.getPlayerManager().getPlayers().values()){
                    if (t.getType() == PlayerType.PLAYER){
                        if (t.getPlayer() != p){
                            t.getPlayer().hidePlayer(plugin, p);
                        }
                    }
                }
                return;
            }
            for (District district : plugin.getDistrictsManager().getDistricts()){
                if (district.onConfig(p)){
                    district.add(p);
                    player.setDistrict(district);
                    break;
                }
            }
            if (plugin.isMuerto(p)){
                player.setDeath(true);
                for (SPlayer t : plugin.getPlayerManager().getPlayers().values()){
                    if (t.getType() == PlayerType.PLAYER){
                        if (t.getPlayer() != p){
                            t.getPlayer().hidePlayer(plugin, p);
                        }
                    }
                }
            } else {
                for (SPlayer t : plugin.getPlayerManager().getPlayers().values()){
                    if (t.getType() == PlayerType.PLAYER){
                        if (t.getPlayer() != p){
                            t.getPlayer().showPlayer(plugin, p);
                        }
                    }
                }
                //p.setGameMode(GameMode.SURVIVAL);
                if (player.getDistrict().getSpawn() != null){
                    p.teleport(player.getDistrict().getSpawn());
                }
            }
        }

        player.setKit();

        if (player.getType() == PlayerType.ADMIN || p.hasPermission("hungergames.broadcast")){
            plugin.getUtils().sendMSGAStaff(plugin.getUtils().ct("&a&l" + p.getName() + " &7ha entrado al juego."));
            p.getInventory().setItem(0, plugin.getItemUtils().mic_off());
        }
    }

    public void hideStaff(Player p){
        for (SPlayer player : players.values()){
            if (player.getType() != PlayerType.PLAYER){
                p.hidePlayer(plugin, player.getPlayer());
            }
        }
    }
    public void staffJoin(Player p){
        for (SPlayer player : players.values()){
            if (player.getType() == PlayerType.PLAYER){
                player.getPlayer().hidePlayer(plugin, p);
            }
        }
        team.addEntry(p.getName());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();

        String msg = e.getQuitMessage();
        e.setQuitMessage(null);
        plugin.getUtils().sendMSGAStaff(msg);
        SPlayer player = getPlayer(p);
        if (player == null){
            return;
        }
        if (player.getType() == PlayerType.PLAYER) {
            for (District district : plugin.getDistrictsManager().getDistricts()) {
                if (district.onConfig(p)) {
                    district.remove(p);
                    break;
                }
            }
            if (player.isDeath()){
                team.removeEntry(p.getName());
            }
        } else {
            team.removeEntry(p.getName());
        }

        players.remove(p.getUniqueId());
    }
}