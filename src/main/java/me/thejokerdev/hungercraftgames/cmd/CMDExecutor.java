package me.thejokerdev.hungercraftgames.cmd;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.sub.*;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class CMDExecutor implements CommandExecutor, TabCompleter {
    private final Main plugin;
    public List<CMD> commands;

    public CMDExecutor(Main plugin){
        this.plugin = plugin;
    }

    public void loadCMDs(){
        commands = new ArrayList<>();

        commands.add(new ReloadCMD(plugin));
        commands.add(new GameCMD(plugin));
        commands.add(new PvPCMD(plugin));
        commands.add(new TerminarDiaCMD(plugin));
        commands.add(new RevivirCMD(plugin));
        commands.add(new SetTypeCMD(plugin));
        commands.add(new LobbyStartCMD(plugin));
        commands.add(new SetupCMD(plugin));
        commands.add(new SoundCMD(plugin));
        commands.add(new KitsCMD(plugin));
        commands.add(new TimerCMD(plugin));
        commands.add(new LootCMD(plugin));
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hcg.admin")){
            plugin.getUtils().sendMSG(sender, "&c¡No tienes permisos!");
            return true;
        }
        if (args.length == 0){
            plugin.getUtils().sendMSG(sender, plugin.getConfig().getStringList("help.argszero").toArray(new String[0]));
            return true;
        }

        String arg = args[0];
        Vector<String> vector = new Vector<>(Arrays.stream(args).collect(Collectors.toList()));
        vector.remove(0);
        args = vector.toArray(new String[0]);

        for (CMD cmd : commands){
            if (!cmd.name().equalsIgnoreCase(arg)){
                continue;
            }
            if (cmd.permission()!=null && !cmd.permission().equals("none")){
                if (!sender.hasPermission(cmd.permission())){
                    plugin.getUtils().sendMSG(sender, "&c¡No tienes permisos!");
                    return true;
                }
            }
            if (cmd.mode() == MODE.PLAYER && cmd.mode() != MODE.BOTH && !(sender instanceof Player)){
                plugin.getUtils().sendMSG(sender, "{prefix}&c¡Este comando es para jugadores!");
                return true;
            } else if (cmd.mode() == MODE.CONSOLE && cmd.mode() != MODE.BOTH && sender instanceof Player){
                plugin.getUtils().sendMSG(sender, "{prefix}&c¡Este comando es para consola!");
            } else {
                cmd.onCMD(sender, label, args);
                break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (!sender.hasPermission("squidgames.admin")) {
            return list;
        }
        if (args.length == 1) {
            String arg = args[0];
            List<String> cmds = new ArrayList(commands.stream().map(CMD::name).collect(Collectors.toList()));
            StringUtil.copyPartialMatches(arg, cmds, list);
            return list;
        }
        if (args.length >= 2) {
            String str = args[0];
            Vector<String> vector = new Vector<>(Arrays.asList(args));
            vector.remove(0);
            args = vector.toArray(new String[0]);

            for (CMD cmd : commands){
                if (!cmd.name().equals(str)){
                    continue;
                }
                return cmd.onTab(sender, command.getLabel(), args);
            }
        }
        return list;
    }
}
