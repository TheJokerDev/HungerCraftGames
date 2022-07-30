package me.thejokerdev.hungercraftgames.game;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.PlayerType;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.xseries.messages.ActionBar;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class General implements Listener {
    private Main plugin;

    public static List<Location> opened = new ArrayList<>();

    public General(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        Player p = e.getEntity();
        SPlayer s = plugin.getPlayerManager().getPlayer(p);
        if (s == null){
            return;
        }
        if (s.getType() != PlayerType.PLAYER){
            return;
        }
        e.getEntity().dropItem(true);
        e.getEntity().getLocation().getWorld().dropItem(p.getLocation(), plugin.getItemUtils().getSkull(p));
        if (p.getKiller() != null){
            ActionBar.sendActionBar(plugin, p.getKiller(), "&fMataste a " + p.getName());
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        List<Block> blocks = new ArrayList<>();
        for (Block b : e.blockList()){
            if (b.getType() == Material.TNT){
                blocks.add(b);
            }
        }
        e.blockList().clear();
        e.blockList().addAll(blocks);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e){
        List<Block> blocks = new ArrayList<>();
        for (Block b : e.blockList()){
            if (b.getType() == Material.TNT){
                blocks.add(b);
            }
        }
        e.blockList().clear();
        e.blockList().addAll(blocks);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if (e.getEntity() instanceof Player){
            return;
        }
        e.getDrops().clear();
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent var1) {
        Player var2 = var1.getPlayer();
        SPlayer var3 = plugin.getPlayerManager().getPlayer(var2);
        if (var3 != null) {
            if (var1.getAction().equals(Action.RIGHT_CLICK_BLOCK) || var1.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Block var4 = var1.getClickedBlock();
                Location var5 = var4.getLocation();
                if (var4.getState() instanceof Chest) {
                    Chest var7 = (Chest)var4.getState();
                    if (!opened.contains(var5.getBlock().getLocation())) {
                        opened.add(var5.getBlock().getLocation());
                        Inventory var8 = var7.getInventory();
                        fillChest(var8, true);
                    }
                }
            }

        }
    }

    public static void clearOpened(){
        opened.clear();
    }

    private int countItems(Inventory paramInventory) {
        byte b = 0;
        for (ItemStack itemStack : paramInventory.getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR)
                b++;
        }  return b;
    }

    public List<ItemStack> getWeapons(){
        List<ItemStack> weapons = new ArrayList<>();
        if (plugin.getUtils().getChest().getList("weapons") == null){
            return weapons;
        }
        weapons.addAll((List<ItemStack>) plugin.getUtils().getChest().getList("weapons"));
        return weapons;
    }

    public List<ItemStack> getFood(){
        List<ItemStack> weapons = new ArrayList<>();
        if (plugin.getUtils().getChest().getList("food") == null){
            return weapons;
        }
        weapons.addAll((List<ItemStack>) plugin.getUtils().getChest().getList("food"));
        return weapons;
    }

    public List<ItemStack> getOthers(){
        List<ItemStack> weapons = new ArrayList<>();
        if (plugin.getUtils().getChest().getList("others") == null){
            return weapons;
        }
        weapons.addAll((List<ItemStack>) plugin.getUtils().getChest().getList("others"));
        return weapons;
    }

    public List<ItemStack> getItems(){
        List<ItemStack> items = new ArrayList<>();
        items.addAll(getWeapons());
        items.addAll(getFood());
        items.addAll(getOthers());
        return items;
    }

    private void fillChest(Inventory paramInventory, boolean paramBoolean) {
        paramInventory.clear();
        if (getItems().size() > 0) {
            int i = plugin.getUtils().getChest().getInt("max");
            while (countItems(paramInventory) < i) {
                Collections.shuffle(getItems(), new Random());
                for (ItemStack randomItem : getItems()) {
                    if (countItems(paramInventory) < i &&
                            hasChance())
                        paramInventory.setItem((new Random()).nextInt(paramInventory.getSize()), randomItem);
                }
            }
        }
    }

    public boolean hasChance() { return ((new Random()).nextInt(10000) < 15 * 100.0D); }

 }

