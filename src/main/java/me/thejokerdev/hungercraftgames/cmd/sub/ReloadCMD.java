package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCMD extends CMD {
    public ReloadCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String permission() {
        return "hgc.admin.reload";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        plugin.reloadConfig();
        plugin.getUtils().sendMSG(sender, "{prefix}&a¡Configuración recargada!");
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
