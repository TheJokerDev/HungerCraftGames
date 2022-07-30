package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.events.DeathEvents;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RevivirCMD extends CMD {
    public RevivirCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "revivir";
    }

    @Override
    public String permission() {
        return "hgc.admin.revivir";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        if (args.length > 0){
            String arg = args[0];
            if (!plugin.isMuerto(arg)){
                plugin.getUtils().sendMSG(sender, "{prefix}&c¡No puedes revivir a alguien que no ha muerto!");
                return true;
            }
            plugin.removeMuerto(arg);
            if (Bukkit.getPlayer(arg) != null){
                Player p = Bukkit.getPlayer(arg);
                SPlayer sp = plugin.getPlayerManager().getPlayer(p);
                DeathEvents.removeEntry(p);
                for (SPlayer t : plugin.getPlayerManager().getPlayers().values()){
                    if (t.getType() == PlayerType.PLAYER){
                        if (t.getPlayer() != p){
                            t.getPlayer().showPlayer(plugin, p);
                        }
                    }
                }
                p.setGameMode(GameMode.SURVIVAL);
                if (sp.getDistrict().getSpawn() != null){
                    p.teleport(sp.getDistrict().getSpawn());
                }
            }
            plugin.getUtils().sendMSG(sender, "{prefix}&a¡Reviviste a &f"+arg+"&a!");
            if (args.length == 2){
                if (args[1].equalsIgnoreCase("setadmin")){
                    plugin.getUtils().sendMSG(sender, "{prefix}&a¡Has asignado &f"+arg+"&a como administrador!");
                    plugin.addOrganizador(arg);
                    if (Bukkit.getPlayer(arg) != null){
                        Player p = Bukkit.getPlayer(arg);
                        plugin.getPlayerManager().staffJoin(p);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            String arg = args[0];
            List<String> names = new ArrayList<>(plugin.getConfig().getStringList("died"));
            StringUtil.copyPartialMatches(arg, names, list);
            return list;
        }
        if (args.length == 2){
            StringUtil.copyPartialMatches(args[1], List.of("setadmin"), list);
            return list;
        }
        return list;
    }

    @Override
    public MODE mode() {
        return MODE.BOTH;
    }
}
