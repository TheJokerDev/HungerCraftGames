package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.game.District;
import me.thejokerdev.hungercraftgames.game.General;
import me.thejokerdev.hungercraftgames.utils.LocationUtil;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameCMD extends CMD {

    public GameCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "game";
    }

    @Override
    public String permission() {
        return "hgc.admin.game";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        if (args.length > 0){
            if (args[0].equalsIgnoreCase("chestreset")){
                General.clearOpened();
                plugin.getUtils().sendMSG(sender, "{prefix}&cYou have resetted the chests!");
            }
            if (args[0].equalsIgnoreCase("setspawn")){
                int i;
                try {
                    i = Integer.parseInt(args[1]);
                } catch (NumberFormatException e){
                    plugin.getUtils().sendMSG(sender, "{prefix}&cInvalid number!");
                    return true;
                }
                if (i < 1 || i > plugin.getConfig().getConfigurationSection("districts").getKeys(false).size()){
                    plugin.getUtils().sendMSG(sender, "{prefix}&cInvalid number!");
                    return true;
                }
                plugin.getConfig().set("districts." + i + ".spawn.arena", LocationUtil.getString(p.getLocation(), true));
                plugin.getUtils().sendMSG(sender, "{prefix}&aSetted spawn for district &f" + i + "&a!");
                plugin.saveConfig();
                plugin.reloadConfig();
                return true;
            }
            if (args[0].equalsIgnoreCase("setspawnlobby")){
                int i;
                try {
                    i = Integer.parseInt(args[1]);
                } catch (NumberFormatException e){
                    plugin.getUtils().sendMSG(sender, "{prefix}&cInvalid number!");
                    return true;
                }
                if (i < 1 || i > plugin.getConfig().getConfigurationSection("districts").getKeys(false).size()){
                    plugin.getUtils().sendMSG(sender, "{prefix}&cInvalid number!");
                    return true;
                }
                plugin.getConfig().set("districts." + i + ".spawn.lobby", LocationUtil.getString(p.getLocation(),true));
                plugin.getUtils().sendMSG(sender, "{prefix}&aSetted lobby spawn for district &f" + i + "&a!");
                plugin.saveConfig();
                plugin.reloadConfig();
            }
            if (args[0].equalsIgnoreCase("start")){
                for (District district : plugin.getDistrictsManager().getDistricts()){
                    district.getPlayers().forEach(player -> {
                        player.teleport(district.getSpawn());
                    });
                }
            }
            if (args[0].equalsIgnoreCase("tplobby")){
                for (District district : plugin.getDistrictsManager().getDistricts()){
                    district.getPlayers().forEach(player -> {
                        player.teleport(district.getLobby());
                    });
                }
            }
            if (args[0].equalsIgnoreCase("mute")){
                if (plugin.isMuted()){
                    plugin.setMuted(false);
                    plugin.getUtils().sendMSG(sender, "{prefix}&aYou have unmuted all player microphones!");
                } else {
                    plugin.setMuted(true);
                    plugin.getUtils().sendMSG(sender, "{prefix}&aYou have muted all player microphones!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("getmic")){
                p.getInventory().addItem(plugin.getItemUtils().mic_on());
                plugin.getUtils().sendMSG(sender, "{prefix}&aYou have received a microphone!");
                return true;
            }
            if (args[0].equalsIgnoreCase("getbandage")){
                p.getInventory().addItem(plugin.getItemUtils().getBandage());
                plugin.getUtils().sendMSG(sender, "{prefix}&aYou have received a bandage!");
                return true;
            }
            if (args[0].equalsIgnoreCase("setcenter")){
                plugin.getConfig().set("days.3.center", LocationUtil.getString(p.getLocation(), true));
                plugin.getUtils().sendMSG(sender, "{prefix}&aYou have set the center of arena!");
                plugin.saveConfig();
                plugin.reloadConfig();
                return true;
            }
            if (args[0].equalsIgnoreCase("setradius")){
                int i;
                try {
                    i = Integer.parseInt(args[1]);
                } catch (NumberFormatException e){
                    plugin.getUtils().sendMSG(sender, "{prefix}&cInvalid number!");
                    return true;
                }
                plugin.getConfig().set("days.3.radius", i);
                plugin.getUtils().sendMSG(sender, "{prefix}&aYou have set the radius of center!");
                plugin.saveConfig();
                plugin.reloadConfig();
                return true;
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 1){
            String arg1 = args[0];
            List<String> cmds = new ArrayList<>(Arrays.asList("setspawn", "setspawnlobby", "chestreset", "start", "tplobby", "mute", "getmic", "getbandage", "setcenter", "setradius"));
            StringUtil.copyPartialMatches(arg1, cmds, list);
        }
        return list;
    }

    @Override
    public MODE mode() {
        return MODE.PLAYER;
    }
}
