package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PvPCMD extends CMD {

    public PvPCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "pvp";
    }

    @Override
    public String permission() {
        return "hgc.pvp";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
if (args.length == 0) {
            if (plugin.pvp) {
                plugin.pvp = false;
                sender.sendMessage(plugin.getPrefix() + "§fPvP §cdesactivado§f.");
            } else {
                plugin.setPvp(true);
                sender.sendMessage(plugin.getPrefix() + "§fPvP §aactivado§f.");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                plugin.setPvp(true);
                sender.sendMessage(plugin.getPrefix() + "§fPvP §aactivado§f.");
            } else if (args[0].equalsIgnoreCase("off")) {
                plugin.setPvp(false);
                sender.sendMessage(plugin.getPrefix() + "§fPvP §cdesactivado§f.");
            } else {
                sender.sendMessage(plugin.getPrefix() + "§cArgumento inválido.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + "§cArgumento inválido.");
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("on", "off"), list);
        }
        return list;
    }

    @Override
    public MODE mode() {
        return null;
    }
}
