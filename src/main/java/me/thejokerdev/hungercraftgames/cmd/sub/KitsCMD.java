package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KitsCMD extends CMD {

    public KitsCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "kits";
    }

    @Override
    public String permission() {
        return "hgc.admin";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        Player p = (Player) sender;
        if (args.length > 1){
            String arg = args[0].toLowerCase();
            String type = args[1].toUpperCase();
            PlayerType test;
            try {
                test = PlayerType.valueOf(type);
            } catch (IllegalArgumentException e) {
                plugin.getUtils().sendMSG(sender, "{prefix}&cDebes ingresar un tipo de jugador valido.");
                return true;
            }
            if (arg.equals("set")){
                List<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
                plugin.getConfig().set("kits."+type.toLowerCase(), items.stream().filter(Objects::nonNull).collect(Collectors.toList()));
                plugin.getUtils().sendMSG(sender, "{prefix}&aKit de &e"+type+" &aestablecido.");
                plugin.saveConfig();
                plugin.reloadConfig();
            }
            if (arg.equals("delete")){
                plugin.getUtils().sendMSG(sender, "{prefix}&aKit de &e"+type+" &aeliminado.");
                plugin.getConfig().set("kits."+type.toLowerCase(), null);
                plugin.saveConfig();
                plugin.reloadConfig();
            }

            if (arg.equals("load")){
                if (plugin.getConfig().get("kits."+type.toLowerCase())==null){
                    plugin.getUtils().sendMSG(sender, "{prefix}&cNo hay kit para ese tipo.");
                    return true;
                }
                Player t = p;
                if (args.length == 3){
                    t = Bukkit.getPlayer(args[2]);
                    if (t == null){
                        plugin.getUtils().sendMSG(sender, "{prefix}&cEse jugador no esta conectado.");
                        return true;
                    }
                }
                plugin.getPlayerManager().getPlayer(t).setKit(test);
                plugin.getUtils().sendMSG(sender, "{prefix}&aKit establecido.");
            }
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            StringUtil.copyPartialMatches(args[0], new ArrayList<>(Arrays.asList("set", "load", "delete")), list);
        }
        if (args.length == 2){
            StringUtil.copyPartialMatches(args[1], new ArrayList<>(Arrays.stream(PlayerType.values()).map(PlayerType::name).collect(Collectors.toList())), list);
        }
        if (args.length == 3){
            StringUtil.copyPartialMatches(args[2], new ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList())), list);
        }
        return list;
    }

    @Override
    public MODE mode() {
        return MODE.PLAYER;
    }
}
