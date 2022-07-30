package me.thejokerdev.hungercraftgames.cmd;

import lombok.Getter;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
public abstract class CMD {
    public Main plugin;

    public CMD(Main plugin){
        this.plugin = plugin;
    }

    public abstract String name();
    public abstract String permission();

    public abstract boolean onCMD(CommandSender sender, String alias, String[] args);

    public abstract List<String> onTab(CommandSender sender, String alias, String[] args);

    public abstract MODE mode();

}
