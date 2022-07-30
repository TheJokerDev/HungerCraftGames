package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.utils.MODE;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetTypeCMD extends CMD {

    public SetTypeCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "settype";
    }

    @Override
    public String permission() {
        return "hgc.admin.settype";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        if (args.length == 2){
            String name = args[0];
            String type = args[1];

            Player t = Bukkit.getPlayer(name);
            if (t == null){
                plugin.getUtils().sendMSG(sender, "{prefix}&c¡Debes escribir un nick válido!");
                return true;
            }
            SPlayer player = plugin.playerManager.getPlayer(t);
            PlayerType playerType;
            try {
                playerType = PlayerType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.getUtils().sendMSG(sender, "{prefix}&c¡Debes escribir un tipo válido!");
                return true;
            }
            player.setType(playerType);
            if (playerType == PlayerType.STAFF){
                plugin.removeOrganizador(t);
                plugin.addGuardia(t);
            } else if (playerType == PlayerType.ADMIN){
                plugin.removeGuardia(t);
                plugin.addOrganizador(t);
            }
            plugin.getUtils().sendMSG(sender, "{prefix}&aHas establecido el tipo de usuario de &f"+t.getName()+"&a a &e"+playerType.name()+"&a.");
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            String arg = args[0];
            List<String> names = new ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
            StringUtil.copyPartialMatches(arg, names, list);
            return list;
        }
        if (args.length == 2){
            String arg = args[1];
            List<String> tipos = new ArrayList<>(Arrays.stream(PlayerType.values()).map(PlayerType::name).collect(Collectors.toList()));
            StringUtil.copyPartialMatches(arg, tipos, list);
            return list;
        }
        return list;
    }

    @Override
    public MODE mode() {
        return MODE.BOTH;
    }
}
