package me.thejokerdev.hungercraftgames;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import lombok.Getter;
import lombok.Setter;
import me.thejokerdev.hungercraftgames.cmd.CMDExecutor;
import me.thejokerdev.hungercraftgames.events.*;
import me.thejokerdev.hungercraftgames.game.*;
import me.thejokerdev.hungercraftgames.managers.DistrictsManager;
import me.thejokerdev.hungercraftgames.menus.MenuListener;
import me.thejokerdev.hungercraftgames.player.PlayerManager;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Getter
public final class Main extends JavaPlugin {
    public static Main plugin;
    public final String PLUGIN_ID = "HungerCraftGames";
    public final Logger LOGGER = LogManager.getLogManager().getLogger(PLUGIN_ID);

    //Classes
    public Utils utils;
    public PlayerManager playerManager;
    public DistrictsManager districtsManager;
    public ItemUtils itemUtils;
    @Setter public boolean inLobby = false;
    @Setter public boolean muted = false;

    //Booleans
    public boolean papiEnabled;
    @Setter public boolean pvp = false;

    //Lists
    public List<String> staff;
    public List<String> admins;

    //Games
    public Day3 day3;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        PluginManager pm = Bukkit.getPluginManager();
        if (!loadBooleans(pm)){
            return;
        }
        loadClasses();
        loadGames();
        loadLists();;
        CMDExecutor cmdExecutor = new CMDExecutor(this);
        getCommand("hungergames").setExecutor(cmdExecutor);
        getCommand("hungergames").setTabCompleter(cmdExecutor);
        cmdExecutor.loadCMDs();

        listener(new DeathEvents(this), new InteractEvents(this), new PvPEvents(this)
        , new MoveEvents(this), new MenuListener(this), new PlayerEvents(this));

        day3 = new Day3(plugin);
        listener(new General(plugin), new Day1(plugin), new Day2(plugin), day3);

        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        service.registerPlugin(new MicUtils(plugin));

    }

    public static Main getPlugin() {
        return plugin;
    }

    public void listener(Listener... l){
        Arrays.stream(l).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public void reloadConfig(){
        super.reloadConfig();
        loadLists();
        if (day3 != null){
            day3.getCircleBlocks().clear();
        }
    }

    /* Load objects && classes */
    public boolean loadBooleans(PluginManager pm){
        papiEnabled = pm.isPluginEnabled("PlaceholderAPI");
        if (!papiEnabled){
            pm.disablePlugin(this);
            return false;
        }

        return true;
    }

    public int getMaxPlayers(){
        return getDistrictsManager().getDistricts().stream().mapToInt(District::getTributesAmount).sum();
    }

    public void loadGames(){
    }

    public void loadClasses(){
        utils = new Utils(this);
        playerManager = new PlayerManager(this);
        itemUtils = new ItemUtils(this);
        districtsManager = new DistrictsManager(this);
    }

    public void loadLists() {
        admins = new ArrayList<>();
        staff = new ArrayList<>();

        admins = new ArrayList<>(getConfig().getStringList("admins"));
        staff = new ArrayList<>(getConfig().getStringList("staff"));
    }

    public int getJugadorSize(){
        return getDistrictsManager().getDistricts().stream().mapToInt(District::getAlivePlayers).sum();
    }

    public boolean isMuerto(Player p){
        return getConfig().getStringList("died").contains(p.getName());
    }
    public boolean isMuerto(String name){
        return getConfig().getStringList("died").contains(name);
    }

    public void addGuardia(Player p){
        List<String> list = new ArrayList<>(getConfig().getStringList("staff"));
        if (!list.contains(p.getName())){
            list.add(p.getName());
        }
        getConfig().set("staff", list);
        saveConfig();
        reloadConfig();
    }
    public void removeGuardia(Player p){
        List<String> list = new ArrayList<>(getConfig().getStringList("staff"));
        list.remove(p.getName());
        getConfig().set("staff", list);
        saveConfig();
        reloadConfig();
    }

    public void addOrganizador(Player p){
        List<String> list = new ArrayList<>(getConfig().getStringList("admins"));
        if (!list.contains(p.getName())){
            list.add(p.getName());
        }
        getConfig().set("admins", list);
        saveConfig();
        reloadConfig();
    }

    public void addOrganizador(String p){
        List<String> list = new ArrayList<>(getConfig().getStringList("admins"));
        if (!list.contains(p)){
            list.add(p);
        }
        getConfig().set("admins", list);
        saveConfig();
        reloadConfig();
    }
    public void removeOrganizador(Player p){
        List<String> list = new ArrayList<>(getConfig().getStringList("admins"));
        list.remove(p.getName());
        getConfig().set("admins", list);
        saveConfig();
        reloadConfig();
    }
    public void addMuerto(SPlayer p){
        List<String> list = new ArrayList<>(getConfig().getStringList("died"));
        if (!list.contains(p.getName())) {
            list.add(p.getName());
        }
        p.setDeath(true);
        getConfig().set("died", list);
        saveConfig();
        reloadConfig();
    }
    public void removeMuerto(String p){
        List<String> list = new ArrayList<>(getConfig().getStringList("died"));
        list.remove(p);
        getConfig().set("died", list);
        saveConfig();
        reloadConfig();
    }

    public String getPrefix(){
        return getUtils().ct(getConfig().getString("settings.prefix"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
