package me.thejokerdev.hungercraftgames.utils;

import lombok.Getter;
import lombok.Setter;
import me.aleiv.cinematicCore.paper.CinematicTool;
import me.aleiv.cinematicCore.paper.Game;
import me.aleiv.cinematicCore.paper.objects.Cinematic;
import me.clip.placeholderapi.PlaceholderAPI;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.config.Config;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.xseries.messages.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    private final Main plugin;
    @Getter @Setter
    private SPlayer playerInCenter = null;

    public Utils(Main plugin){
        this.plugin = plugin;
    }

    //Returns
    /* Strings */

    public String ct(String msg){
        return translateCCode('&', msg);
    }

    public String getMSG(CommandSender sender, String msg){

        if (msg.contains("{prefix}")) {
            msg = msg.replace("{prefix}", plugin.getPrefix());
        }

        if (plugin.isPapiEnabled()){
            msg = PlaceholderAPI.setPlaceholders(sender instanceof Player ? (Player)sender : null, msg);
        }

        return ct(msg);
    }

    public void sendMSG(CommandSender sender, String... array){
        for (String s : array){
            if (sender instanceof Player){
                sender.sendMessage(getMSG(sender, s));
            } else {
                Bukkit.getConsoleSender().sendMessage(getMSG(sender, s));
            }
        }
    }
    int clock_secs = 0;
    public BukkitTask clock;

    public void stopClock(){
        if (clock == null){
            return;
        }
        clock.cancel();
    }

    public void clock(int secs){
        clock_secs = secs;
        if (clock != null){
            clock.cancel();
        }
        clock = new BukkitRunnable() {
            @Override
            public void run() {
                ActionBar.sendPlayersActionBar("♝ " + secToTime(clock_secs));
                clock_secs--;
                if (clock_secs == 2){
                    playAudio("reloj");
                }
                if (clock_secs <= 0){
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    public void playAudio(Sound sound){
        for (Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(), sound, 1.0F, 1.0F);
        }
    }
    public void playAudio(String sound){
        for (Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(), sound, 10.0F, 1.0F);
        }
    }

    public String secToTime(int sec) {
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if( hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days,hours%24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void setGlow(Entity entity, ChatColor color) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("GLOW_COLOR_" + color.name().toUpperCase());
        team.addEntry(entity.getName());
        entity.setGlowing(true);
        if(entity instanceof Player){
            ((Player) entity).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            ((Player) entity).getInventory().setHelmet(new ItemStack(Material.TNT));
        }
    }

    public Config getChest(){
        File file = new File(plugin.getDataFolder(), "chests.yml");
        if (!file.exists()){
            plugin.saveResource("chests.yml", false);
        }
        return new Config(plugin, file);
    }

    public void saveInventory(Inventory inv){
        List<ItemStack> items = new ArrayList<>();
        items.addAll((List<ItemStack>) plugin.getUtils().getChest().getList("others"));
        items.addAll(Arrays.asList(inv.getContents()));
        getChest().set("others", items.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        getChest().save();
    }

    public void remGlow(Entity entity, ChatColor color) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("GLOW_COLOR_" + color.name().toUpperCase());
        team.removeEntry(entity.getName());
        entity.setGlowing(false);
        if(entity instanceof Player){
            ((Player) entity).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            ((Player) entity).getInventory().setHelmet(new ItemStack(Material.AIR));
        }
    }

    public void sendMSGAJugadores(String... msg){
        for (SPlayer p : plugin.getPlayerManager().getPlayers().values()){
            if (!p.isDeath()){
                for (String s : msg){
                    s = ct(s);
                    p.getPlayer().sendMessage(s);
                }
            }
        }
    }
    public void sendMSGAStaff(String... msg){
        for (SPlayer p : plugin.getPlayerManager().getPlayers().values()){
            if (p.getType() != PlayerType.PLAYER){
                for (String s : msg){
                    s = getMSG(p.getPlayer(), s);
                    p.getPlayer().sendMessage(s);
                }
            }
        }
    }


    public void playCinematic(String... cinematic){
        Game game = CinematicTool.getInstance().getGame();
        HashMap<String, Cinematic> cinematics = game.getCinematics();
        boolean allExist = true;
        String[] var7 = cinematic;
        int var8 = cinematic.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            String name = var7[var9];
            if (!cinematics.containsKey(name)) {
                allExist = false;
            }
        }

        if (!allExist) {
            sendMSG(Bukkit.getConsoleSender(), "&cCinematic doesn't exist.");
        } else {
            List<Player> players;
            players = new ArrayList<>(Bukkit.getOnlinePlayers());
            List<UUID> uuids = players.stream().map(Player::getUniqueId).collect(Collectors.toList());
            game.setFade(false);
            game.play(uuids, cinematic);
        }

    }

    //Voids

    //Extra utils
    public static String translateCCode(char altColorChar, String textToTranslate)
    {
        char[] b = textToTranslate.toCharArray();
        for ( int i = 0; i < b.length - 1; i++ )
        {
            if ( b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf( b[i + 1] ) > -1 )
            {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase( b[i + 1] );
            }
        }
        return new String( b );
    }
}
