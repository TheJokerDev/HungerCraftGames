package me.thejokerdev.hungercraftgames.menus;

import lombok.Getter;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.config.Config;
import me.thejokerdev.hungercraftgames.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Getter
public abstract class Menu {
    private String menuId;
    private Inventory inv;
    private Player player;
    private String title;
    private String back;
    private Main plugin;

    public Menu(Main plugin, Player var1, String var2, String var3, int var4) {
        this.plugin = plugin;
        this.player = var1;
        this.menuId = var2;
        this.title = plugin.getUtils().ct(var3);
        this.inv = Bukkit.createInventory(null, var4 * 9,title);
        this.setBack("none");
        HashMap<String, Menu> var5 = MenuListener.getPlayerMenus(var1);
        var5.put(var2, this);
        MenuListener.menus.put(var1.getName(), var5);
    }

    public Menu(Main plugin, Player var1, String var2) {
        this.plugin = plugin;
        this.player = var1;
        this.menuId = var2;
        this.title = plugin.getUtils().ct(getConfig().getString("settings.title"));
        this.inv = Bukkit.createInventory(null, getConfig().getInt("settings.rows") * 9,title);
        this.setBack("none");
        HashMap<String, Menu> var5 = MenuListener.getPlayerMenus(var1);
        var5.put(var2, this);
        MenuListener.menus.put(var1.getName(), var5);
    }

    public Config getConfig(){
        File file = new File(plugin.getDataFolder(), getMenuId()+".yml");
        return new Config(plugin, file);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = plugin.getUtils().ct(title);
    }

    public Menu(Main plugin, Player var1, String var2, String var3, int var4, String var5) {
        this.plugin = plugin;
        this.player = var1;
        this.menuId = var2;
        title = plugin.getUtils().ct(var3);
        this.inv = Bukkit.createInventory(null, var4 * 9, title);
        this.setBack(var5);
        HashMap<String, Menu> var6 = MenuListener.getPlayerMenus(var1);
        var6.put(var2, this);
        MenuListener.menus.put(var1.getName(), var6);
    }

    protected Menu() {
    }

    public Menu addItem(ItemStack var1) {
        this.inv.addItem(var1);
        return this;
    }

    public Menu addItem(ItemBuilder var1) {
        return this.addItem(var1.toItemStack());
    }

    public Menu setItem(int var1, ItemBuilder var2) {
        this.inv.setItem(var1, var2.toItemStack());
        return this;
    }
    public Menu setItem(List<Integer> var1, ItemBuilder var2) {
        for (int i : var1){
            this.inv.setItem(i, var2.toItemStack());
        }
        return this;
    }
    public Menu setItem(int var1, ItemStack var2) {
        this.inv.setItem(var1, var2);
        return this;
    }

    public Menu setItem(int var1, int var2, ItemBuilder var3) {
        this.inv.setItem((var1 - 1) * 9 + (var2 - 1), var3.toItemStack());
        return this;
    }

    public Menu setItem(int var1, int var2, ItemStack var3) {
        this.inv.setItem(var1 * 9 + var2, var3);
        return this;
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public void newInventoryName(String var1) {
        this.inv = Bukkit.createInventory(null, this.inv.getSize(), var1);
    }

    public String getMenuId() {
        return this.menuId;
    }

    public Player getPlayer(){
        return player.getPlayer();
    }

    public String getBack() {
        return this.back;
    }

    public void setBack(String var1) {
        this.back = var1;
    }

    public void addFullLine(int var1, ItemBuilder var2) {
        var2.setName(" &r");

        for(int var3 = 1; var3 < 10; ++var3) {
            this.setItem(var1, var3, var2);
        }

    }

    public abstract void onOpen(InventoryOpenEvent var1);

    public abstract void onClose(InventoryCloseEvent var1);

    public abstract void onClick(InventoryClickEvent var1);

    public abstract void update();
}