package me.thejokerdev.hungercraftgames.game;

import lombok.Getter;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class District {
    private Main plugin;
    private String name;
    private List<Player> players = new ArrayList<>();
    private Location spawn;
    private Team team;

    public District(Main plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        team = plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam("district_"+name);
        if (team == null){
            team = plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("district_"+name);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team.setAllowFriendlyFire(false);
        }
        team.setAllowFriendlyFire(true);
    }

    public Location getSpawn() {
        String s = plugin.getConfig().getString("districts."+name+".spawn.arena");
        return s != null ?LocationUtil.getLocation(s) : null;
    }

    public Location getLobby() {
        String s = plugin.getConfig().getString("districts."+name+".spawn.lobby");
        return s != null ?LocationUtil.getLocation(s) : null;
    }

    public List<String> getTributes(){
        return plugin.getConfig().getStringList("districts."+name+".tributes").stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public void add(Player p){
        if (!getTributes().contains(p.getName().toLowerCase())){
            List<String> list = new ArrayList<>(getTributes());
            list.add(p.getName().toLowerCase());
            plugin.getConfig().set("districts."+name+".tributes", list);
            plugin.saveConfig();
            plugin.reloadConfig();
        }
        players.add(p);
        team.addEntry(p.getName());
    }

    public void remove(Player p){
        players.remove(p);
        team.removeEntry(p.getName());
    }

    public boolean contains(Player p){
        return players.contains(p);
    }

    public boolean onConfig(Player p){
        return getTributes().contains(p.getName().toLowerCase());
    }

    public int getAlivePlayers(){
        int i = 0;
        for (Player p : players){
            SPlayer sp = plugin.getPlayerManager().getPlayer(p);
            if (sp == null) {
                continue;
            }
            if (!sp.isDeath()){
                i++;
            }
        }
        return i;
    }

    public int getTributesAmount(){
        return getTributes().size();
    }


}
