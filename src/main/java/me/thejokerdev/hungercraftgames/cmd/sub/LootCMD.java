package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.menus.LootMenu;
import me.thejokerdev.hungercraftgames.menus.Menu;
import me.thejokerdev.hungercraftgames.menus.MenuListener;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LootCMD extends CMD {

    public LootCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "loot";
    }

    @Override
    public String permission() {
        return "hungergames.loot";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        Player p = (Player)sender;
        Menu menu = MenuListener.getPlayerMenu(p, "loot");
        if (menu == null) {
            menu = new LootMenu(plugin, p);
        }
        p.openInventory(menu.getInventory());
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        return null;
    }

    @Override
    public MODE mode() {
        return MODE.PLAYER;
    }
}
