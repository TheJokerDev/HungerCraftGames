package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimerCMD extends CMD {

    public TimerCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "timer";
    }

    @Override
    public String permission() {
        return "hgc.admin.timer";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        if (args.length > 0){
            if (args[0].equalsIgnoreCase("stop")){
                plugin.getUtils().stopClock();
                plugin.getUtils().clock(5);
                return true;
            }
            if (args[0].equalsIgnoreCase("forcestop")){
                plugin.getUtils().stopClock();
                return true;
            }
            String arg = args[0];
            String var2 = args[1];
            if (arg.equalsIgnoreCase("start")) {
                int time;
                try {
                    time = Integer.parseInt(var2);
                } catch (NumberFormatException e) {
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine sp = manager.getEngineByName("JavaScript");
                    try {
                        time = (int) sp.eval(var2);
                    } catch (ScriptException ex) {
                        sender.sendMessage("§c¡Eso no es un número!");
                        return true;
                    }
                }
                plugin.getUtils().clock(time);
                sender.sendMessage("§aTimer iniciado de: §r"+getFormattedTimer(time));
                return true;
            }
        }
        return true;
    }

    public String getFormattedTimer(int var4){
        int var5 = var4 % 86400 % 3600 % 60;
        int var6 = var4 % 86400 % 3600 / 60;
        int var7 = var4 % 86400 / 3600;
        int var8 = var4 / 86400;
        boolean var9 = true;
        boolean var10 = true;
        boolean var11 = true;
        boolean var12 = true;
        if (var5 == 1) {
            var9 = false;
        }

        if (var6 == 1) {
            var10 = false;
        }

        if (var7 == 1) {
            var11 = false;
        }

        if (var8 == 1) {
            var12 = false;
        }

        String var13 = var9 ? "§f%s §asegs." : "§f%s §aseg.";
        String var14 = String.format(var13, var5);
        String var15 = var10 ? "§f%s §amins, " : "§f%s §amin, ";
        String var16 = String.format(var15, var6);
        String var17 = var11 ? "§f%s §ah, " : "§f%s §ahrs, ";
        String var18 = String.format(var17, var7);
        String var19 = var12 ? "§f%s §ads, " : "§f%s §ad, ";
        String var20 = String.format(var19, var8);
        if (var8 == 0) {
            var20 = "";
        }

        if (var7 == 0) {
            var18 = "";
        }

        if (var6 == 0) {
            var16 = "";
        }

        String var21 = var20+var18+var16+var14;
        return var21;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            StringUtil.copyPartialMatches(args[0], Arrays.asList("start", "stop", "forcestop"), list);
            return list;
        }
        return list;
    }

    @Override
    public MODE mode() {
        return MODE.BOTH;
    }
}
