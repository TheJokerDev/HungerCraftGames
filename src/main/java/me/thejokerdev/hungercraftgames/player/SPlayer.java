package me.thejokerdev.hungercraftgames.player;

import lombok.Getter;
import lombok.Setter;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.game.District;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SPlayer {
    private String name;
    private District district;
    private UUID uniqueId;
    private int number = 0;
    private boolean death = false;
    private boolean inCenter = false;

    private PlayerType type = PlayerType.PLAYER;

    private boolean broadcast = false;
    private boolean armor = false;
    private boolean weapon = false;
    private boolean shield = false;

    public SPlayer(Player player){
        name = player.getName();
        uniqueId = player.getUniqueId();
    }

    public void setDeath(boolean death) {
        this.death = death;
        if (!death){
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }
    }
    public void setType(PlayerType type) {
        this.type = type;
    }

    public void kill(){
        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().setHealth(0);
            }
        }.runTask(Main.getPlugin());
        Main.plugin.getUtils().playAudio("jugador");

    }

    public void setKit(){
        setKit(type);
    }

    public void setKit(PlayerType type){
        if (Main.getPlugin().getConfig().get("kits."+type.name().toLowerCase())==null){
            return;
        }
        //getPlayer().getInventory().clear();
        List<String> items = Main.getPlugin().getConfig().getStringList("kits."+type.name().toLowerCase());
        if (items.isEmpty()){
            return;
        }
        List<ItemStack> list = (List<ItemStack>) Main.getPlugin().getConfig().get("kits."+type.name().toLowerCase());
        for (ItemStack item : list){
            if (item == null){
                continue;
            }
            if (isArmor(item)){
                String name = item.getType().name().toLowerCase();
                if (name.contains("helmet")){
                    getPlayer().getInventory().setHelmet(item);
                    continue;
                }
                if (name.contains("chestplate")){
                    getPlayer().getInventory().setChestplate(item);
                    continue;
                }
                if (name.contains("leggings")){
                    getPlayer().getInventory().setLeggings(item);
                    continue;
                }
                if (name.contains("boots")){
                    getPlayer().getInventory().setBoots(item);
                    continue;
                }
            }
            getPlayer().getInventory().addItem(item);
        }
        getPlayer().updateInventory();
    }

    public boolean isArmor(ItemStack item){
        String name = item.getType().name().toLowerCase();
        if (name.contains("helmet") || name.contains("chestplate") ||name.contains("leggings") ||name.contains("boots")){
            return true;
        }
        return false;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(name)!=null ? Bukkit.getPlayer(name) : Bukkit.getPlayer(uniqueId);
    }
}
