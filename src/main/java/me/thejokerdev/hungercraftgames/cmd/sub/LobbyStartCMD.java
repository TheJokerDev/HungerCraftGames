package me.thejokerdev.hungercraftgames.cmd.sub;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.cmd.CMD;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.utils.MODE;
import me.thejokerdev.hungercraftgames.xseries.messages.ActionBar;
import me.thejokerdev.hungercraftgames.xseries.messages.Titles;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LobbyStartCMD extends CMD {
    private List<String> sound = new ArrayList<>();
    private int i;
    private BukkitTask task;

    public LobbyStartCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String name() {
        return "lobbystart";
    }

    @Override
    public String permission() {
        return "hgc.admin.lobbystart";
    }

    @Override
    public boolean onCMD(CommandSender sender, String alias, String[] args) {
        if (args.length == 1){
            String arg = args[0];
            if (arg.equalsIgnoreCase("start")){
                plugin.setInLobby(true);
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (SPlayer player : plugin.getPlayerManager().getPlayers().values()){
                            if (!sound.contains(player.getName())){
                                //player.getPlayer().playSound(player.getPlayer().getLocation(), "squid", 1.0F, 1.0F);
                                sound.add(player.getName());
                            }
                            if (player.getType() == PlayerType.PLAYER) {
                                Titles.sendTitle(player.getPlayer(), 0, 25, 0, "\u3400", getPlugin().getJugadorSize() + "/" + getPlugin().getMaxPlayers());
                            } else {
                                ActionBar.sendActionBar(player.getPlayer(), "HG: "+getPlugin().getJugadorSize() + "/" + getPlugin().getMaxPlayers());
                            }
                        }
                    }
                }.runTaskTimerAsynchronously(plugin, 0L, 10L);
                plugin.getUtils().sendMSG(sender, "&a¡Activaste el modo lobby!");
            } else if (arg.equalsIgnoreCase("stop")) {
                plugin.setInLobby(false);
                if (task == null){
                    return true;
                }
                task.cancel();

                Bukkit.getOnlinePlayers().forEach(l->{
                    Titles.clearTitle(l);
                    l.stopAllSounds();
                });
                sound.clear();
                plugin.getUtils().sendMSG(sender, "&c¡Desactivaste el modo lobby!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            String arg = args[0];
            StringUtil.copyPartialMatches(arg, new ArrayList<>() {{
                add("start");
                add("stop");
            }}, list);
            return list;
        }
        return list;
    }

    @Override
    public MODE mode() {
        return null;
    }
}
