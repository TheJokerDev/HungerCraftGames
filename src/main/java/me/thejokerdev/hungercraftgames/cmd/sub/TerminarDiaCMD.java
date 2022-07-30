package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TerminarDiaCMD extends CMD {

    public TerminarDiaCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "terminardia";
    }

    @Override
    public String permission() {
        return "hgc.admin.terminardia";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        if (args.length == 1){
            String arg = args[0];
            if (arg.equals("1") || arg.equals("2")){
                for (Player p : Bukkit.getOnlinePlayers()){
                    p.kickPlayer(plugin.getUtils().ct("&fDía "+arg+" finalizado."));
                }
                Bukkit.shutdown();
                return true;
            }
        }
        plugin.getUtils().sendMSG(sender, "{prefix}&cPor favor, ingresa el número del día.");
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        return null;
    }

    @Override
    public MODE mode() {
        return null;
    }
}
