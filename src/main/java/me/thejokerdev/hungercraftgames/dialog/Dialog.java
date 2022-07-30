package me.thejokerdev.hungercraftgames.dialog;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.xseries.messages.ActionBar;
import me.thejokerdev.hungercraftgames.xseries.messages.Titles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Dialog implements Listener{
    private Main plugin;
    private final Player player;
    private Recall<Player> recall;
    private final HashMap<String, String> placeholders;
    private BukkitTask task;
    private long lastMoved;

    public Dialog(Main plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.placeholders = new HashMap();
        this.openDialog();
    }
    public void loadListener(){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    public void unloadListener(){
        HandlerList.unregisterAll(this);
    }

    public void openDialog() {
        unloadListener();
        getPlayer().closeInventory();
        task = new BukkitRunnable(){
            @Override
            public void run() {
                sendTitleAndActionbar();
            }
        }.runTaskTimer(plugin, 0, 20*2);
        loadListener();
        if (canClose()) {
            plugin.getUtils().sendMSG(getPlayer(), "&fHaz &aclic izquierdo&f para cancelar.");
        }
    }

    private void sendTitleAndActionbar() {
        if (getTitle() == null || getActionbar() == null){
            task.cancel();
            plugin.getUtils().sendMSG(getPlayer(), "&c¡Ups! Ocurrió un error.");
        }
        Titles.sendTitle(this.getPlayer(), 0, 999, 0, this.apply(plugin.getUtils().ct(this.getTitle() != null ? this.getTitle() : "")), this.apply(plugin.getUtils().ct(this.getSubtitle() != null ? this.getSubtitle() : "")));
        ActionBar.sendActionBar(this.getPlayer(), this.apply(plugin.getUtils().ct(this.getActionbar() != null ? this.getActionbar() : "")));
    }

    public void close() {
            this.task.cancel();
            unloadListener();
            Titles.clearTitle(this.getPlayer());
            ActionBar.clearActionBar(this.getPlayer());
            this.onDialogClose();
            if (this.recall != null) {
                this.recall.run(this.getPlayer());
            }
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onMove(PlayerMoveEvent event) {
        if (this.canClose() && this.getPlayer().equals(event.getPlayer())) {
            if (this.lastMoved == 0L) {
                this.lastMoved = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - this.lastMoved >= 5000L) {
                plugin.getUtils().sendMSG(getPlayer(), "&fHaz &aclic izquierdo&f para cancelar.");
                this.lastMoved = System.currentTimeMillis();
            }
        }

    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (this.getPlayer().equals(event.getPlayer()) && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && this.canClose()) {
            plugin.getUtils().sendMSG(getPlayer(), "&fHas cancelado el diálogo.");
            this.close();
        }

    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        if (event.getPlayer().equals(this.getPlayer())) {
            event.setCancelled(true);
            String message = event.getMessage();
            boolean result = this.onResult(message);
            if (result) {
                this.close();
            }
        }

    }

    public Player getPlayer() {
        return this.player;
    }

    public Dialog setRecall(Recall<Player> recall) {
        this.recall = recall;
        return this;
    }

    public Dialog addPlaceholders(HashMap<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public Dialog addPlaceholder(String key, String value) {
        this.placeholders.put(key, value);
        return this;
    }

    public Dialog removePlaceholder(String key) {
        this.placeholders.remove(key);
        return this;
    }

    private String apply(String s) {
        AtomicReference<String> r = new AtomicReference(s);
        this.placeholders.forEach((k, v) -> {
            r.set(r.get().replace(k, v));
        });
        return r.get();
    }

    public abstract String getTitle();

    public abstract String getSubtitle();

    public abstract String getActionbar();

    public abstract boolean onResult(String var1);

    public boolean canClose() {
        return true;
    }

    public void onDialogClose() {
    }
}
