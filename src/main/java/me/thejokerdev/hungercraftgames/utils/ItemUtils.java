package me.thejokerdev.hungercraftgames.utils;

import me.thejokerdev.hungercraftgames.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    private Main plugin;
    public ItemUtils (Main plugin){
        this.plugin = plugin;
    }

    public String ct(String msg){
        return plugin.getUtils().ct(msg);
    }

    public ItemStack region_wand(){
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ct("&aVarita de región"));
        List<String> lore = new ArrayList<>();
        lore.add(ct(""));
        lore.add(ct(" &7Clic izquierdo - &fPosición #1"));
        lore.add(ct(" &7Clic izquierdo - &fPosición #2"));
        lore.add(ct(""));

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack mic_on(){
        ItemStack item = new ItemStack(Material.LIME_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ct("&aMicrófono ON"));
        List<String> lore = new ArrayList<>();
        lore.add(ct(""));
        lore.add(ct("&f Clic para desactivar"));
        lore.add(ct(""));
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getSkull(Player p){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(p.getName());
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);

        meta.setDisplayName(ct("&fCabeza de &6"+p.getName()));
        List<String> lore = new ArrayList<>();
        lore.add(ct(""));
        lore.add(ct("&f Colecciona cabezas para"));
        lore.add(ct("&f canjearlas en el centro."));
        lore.add(ct(""));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack spawnTool(){
        ItemStack item = new ItemStack(Material.GOLDEN_SHOVEL);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ct("&eHerramienta de spawn"));
        List<String> lore = new ArrayList<>();
        lore.add(ct(""));
        lore.add(ct("&f Clic para añadir el spawn"));
        lore.add(ct(""));
        meta.setLore(lore);


        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getBandage(){
        ItemStack item = new ItemStack(Material.WHITE_WOOL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ct("&aVenda"));
        List<String> lore = new ArrayList<>();
        lore.add(ct(""));
        lore.add(ct("&f Clic para curar"));
        lore.add(ct(""));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack mic_off(){
        ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ct("&cMicrófono OFF"));
        List<String> lore = new ArrayList<>();
        lore.add(ct(""));
        lore.add(ct("&f Clic para activar"));
        lore.add(ct(""));
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack exit(){
        ItemBuilder item = new ItemBuilder(Material.REDSTONE);
        item.setName("&cSalir");
        return item.toItemStack();
    }
}
