package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SoundCMD extends CMD {
    public SoundCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "sounds";
    }

    @Override
    public String permission() {
        return "hgc.admin.sounds";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        if (args.length == 0){
            plugin.getUtils().sendMSG(sender, "{prefix}&c¡Debes poner el nombre del sonido!");
            return true;
        }
        if (args.length == 1){
            String arg = args[0];
            for (Player t : Bukkit.getOnlinePlayers()){
                t.playSound(t.getLocation(), arg, 10.0F, 1.0F);
            }
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        return null;
    }

    @Override
    public MODE mode() {
        return MODE.BOTH;
    }
}
