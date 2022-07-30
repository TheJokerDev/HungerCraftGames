package me.thejokerdev.hungercraftgames.menus;

import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.player.SPlayer;
import me.thejokerdev.hungercraftgames.utils.ItemBuilder;
import me.thejokerdev.hungercraftgames.utils.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LootMenu extends Menu {
    private ItemStack question;

    public LootMenu(Main plugin, Player p) {
        super(plugin, p, "loot", "&6Escoje tu loot", 6);
    }

    public SPlayer getSPlayer() {
        return getPlugin().getPlayerManager().getPlayer(getPlayer());
    }
    @Override
    public void onOpen(InventoryOpenEvent var1) {
        if (getSPlayer() == null){
            getPlugin().getUtils().sendMSG(getPlayer(), "{prefix}&cNo se pudo cargar el menú.");
            getPlayer().closeInventory();
            return;
        }
        update();
        getPlugin().getUtils().setPlayerInCenter(getSPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent var1) {
        getPlugin().getUtils().setPlayerInCenter(null);
        getPlugin().getUtils().sendMSG(getPlayer(), "&a¡Has terminado de lootear!", "&fAhora, quedarás desprotegido.");
    }

    @Override
    public void onClick(InventoryClickEvent var1) {

        if (var1.getSlot() == 21){
            if (!getSPlayer().isArmor() && getSkullsAmount() >= 1){
                giveArmor1();
                getSPlayer().setArmor(true);
                removeSkulls(1);
            } else {
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 1F);
            }
        }

        if (var1.getSlot() == 22){
            if (!getSPlayer().isArmor() && getSkullsAmount() >= 2){
                giveArmor2();
                getSPlayer().setArmor(true);
                removeSkulls(2);
            } else {
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 1F);
            }
        }

        if (var1.getSlot() == 30){
            if (!getSPlayer().isWeapon() && getSkullsAmount() >= 1){
                giveWeapon1();
                getSPlayer().setWeapon(true);
                removeSkulls(1);
            } else {
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 1F);
            }
        }
        if (var1.getSlot() == 31){
            if (!getSPlayer().isWeapon() && getSkullsAmount() >= 2){
                giveWeapon2();
                getSPlayer().setWeapon(true);
                removeSkulls(2);
            } else {
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 1F);
            }
        }

        if (var1.getSlot() == 39){
            if (!getSPlayer().isShield() && getSkullsAmount() >= 1){
                giveShield();
                getSPlayer().setShield(true);
                removeSkulls(1);
            } else {
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 1F);
            }
        }

        update();
    }

    @Override
    public void update() {
        if (question == null){
            question = SkullUtils.getHead(Bukkit.getOfflinePlayer(UUID.fromString("606e2ff0-ed77-4842-9d6c-e1d3321c7838")));
        }
        ItemBuilder info = new ItemBuilder(Material.PAPER);
        info.setName("&bInformación");
        info.setLore(
                "",
                "&f En este menú podrás canjear las cabezas",
                "&f de los jugadores que hayan muerto.",
                ""
        );

        ItemBuilder info_armor = new ItemBuilder(Material.WHITE_CANDLE);
        info_armor.setSkullOwner("MHF_Arrow");
        info_armor.setName("&6Armaduras");
        info_armor.setLore(
                "",
                "&f Escoje la armadura que",
                "&f quieres canjear.",
                ""
        );

        ItemBuilder info_weapons = new ItemBuilder(Material.WHITE_CANDLE);
        info_weapons.setSkullOwner("MHF_Arrow");
        info_weapons.setName("&6Armas");
        info_weapons.setLore(
                "",
                "&f Escoje el arma que",
                "&f quieres canjear.",
                ""
        );

        ItemBuilder info_shields = new ItemBuilder(Material.WHITE_CANDLE);
        info_shields.setSkullOwner("MHF_Arrow");
        info_shields.setName("&6Escudos");
        info_shields.setLore(
                "",
                "&f Escoje el escudo que",
                "&f quieras canjear.",
                ""
        );

        setItem(4, info.toItemStack());
        setItem(20, info_armor.toItemStack());
        setItem(29, info_weapons.toItemStack());
        setItem(38, info_shields.toItemStack());

        ItemBuilder armor1 = new ItemBuilder(getSPlayer().isArmor() ? Material.BARRIER : getSkullsAmount() >= 1 ? Material.NETHERITE_HELMET : Material.BARRIER);
        armor1.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        armor1.setName("&4Armadura media de Netherite");
        armor1.setLore(
                "",
                "&f Coste: &e1 cabeza",
                "",
                "&e Esta armadura contiene:",
                "&f ",
                "&7  - x1 Casco de Netherite (Protección II)",
                "&7  - x1 Pechera de Netherite (Protección II)",
                "&7  - x1 Pantalones de Netherite (Protección II)",
                "&7  - x1 Botas de Netherite (Protección II)",
                "",
                "&e Características:",
                "&c  - Tendrás un 20% de lentitud",
                "",
                "&f Estado: "+ (getSPlayer().isArmor() ? "&cYa canjeaste una armadura" : getSkullsAmount() >= 1 ? "&aCanjeable": "&cNo tienes suficientes cabezas"),
                ""
        );

        ItemBuilder armor2 = new ItemBuilder(getSPlayer().isArmor() ? Material.BARRIER : getSkullsAmount() >= 2 ? Material.NETHERITE_HELMET : Material.BARRIER);
        armor2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        armor2.setName("&4Armadura fuerte de Netherite");
        armor2.setLore(
                "",
                "&f Coste: &e2 cabeza",
                "",
                "&e Esta armadura contiene:",
                "&f ",
                "&7  - x1 Casco de Netherite (Protección II)",
                "&7  - x1 Pechera de Diamante (Protección III)",
                "&7  - x1 Pantalones de Netherite (Protección II)",
                "&7  - x1 Botas de Diamante (Protección III, Caida de pluma I)",
                "",
                "&e Características:",
                "&c  - Tendrás un 30% de lentitud",
                "&a  + Tendrás 1 corazón más",
                "",
                "&f Estado: "+ (getSPlayer().isArmor() ? "&cYa canjeaste una armadura" : getSkullsAmount() >= 2 ? "&aCanjeable": "&cNo tienes suficientes cabezas"),
                ""
        );

        ItemBuilder weapon = new ItemBuilder(getSPlayer().isWeapon() ? Material.BARRIER : getSkullsAmount() >= 1 ? Material.NETHERITE_SWORD : Material.BARRIER);
        weapon.addEnchant(Enchantment.DAMAGE_ALL, 2);
        weapon.setName("&4Espada media de Netherite");
        weapon.setLore(
                "",
                "&f Coste: &e1 cabeza",
                "",
                "&e Esta arma contiene:",
                "&f ",
                "&7  - x1 Espada de Netherite (Filo II)",
                "",
                "&e Características:",
                "&c  - Tendrás un 20% de lentitud al atacar",
                "",
                "&f Estado: "+ (getSPlayer().isWeapon() ? "&cYa canjeaste una arma" : getSkullsAmount() >= 1 ? "&aCanjeable": "&cNo tienes suficientes cabezas"),
                ""
        );

        ItemBuilder weapon2 = new ItemBuilder(getSPlayer().isWeapon() ? Material.BARRIER : getSkullsAmount() >= 1 ? Material.DIAMOND_SWORD : Material.BARRIER);
        weapon2.addEnchant(Enchantment.DAMAGE_ALL, 3);
        weapon2.setName("&4Espada fuerte de Diamante");
        weapon2.setLore(
                "",
                "&f Coste: &e2 cabeza",
                "",
                "&e Esta arma contiene:",
                "&f ",
                "&7  - x1 Espada de Diamante (Filo III)",
                "",
                "&e Características:",
                "&c  - Tendrás un 50% de lentitud al atacar",
                "&a  + Tendrás 1 corazón más",
                "",
                "&f Estado: "+ (getSPlayer().isWeapon() ? "&cYa canjeaste una arma" : getSkullsAmount() >= 2 ? "&aCanjeable": "&cNo tienes suficientes cabezas"),
                ""
        );

        ItemBuilder shield1 = new ItemBuilder(getSPlayer().isShield() ? Material.BARRIER : getSkullsAmount() >= 1 ? Material.SHIELD : Material.BARRIER);
        shield1.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        shield1.setName("&4Escudo especial");
        shield1.setLore(
                "",
                "&f Coste: &e1 cabeza",
                "",
                "&e Esta arma contiene:",
                "&f ",
                "&7  - x1 Escudo (Protección IV)",
                "",
                "&e Características:",
                "&a  + Tendrás 1 corazón más",
                "",
                "&f Estado: "+ (getSPlayer().isShield() ? "&cYa canjeaste un escudo" : getSkullsAmount() >= 1 ? "&aCanjeable": "&cNo tienes suficientes cabezas"),
                ""
        );

        setItem(21, armor1.toItemStack());
        setItem(22, armor2.toItemStack());
        setItem(30, weapon.toItemStack());
        setItem(31, weapon2.toItemStack());
        setItem(39, shield1.toItemStack());
    }

    public void removeSkulls(int amount){
        List<ItemStack> skulls = new ArrayList<>();
        for (ItemStack item : getPlayer().getInventory().getContents()){
            if (item != null && item.getType() == Material.PLAYER_HEAD){
                skulls.add(item);
            }
        }
        for (int i = 0; i < amount; i++){
            if (skulls.size() > 0){
                getPlayer().getInventory().removeItem(skulls.get(i));
            }
        }
    }

    public Integer getSkullsAmount(){
        int i = 0;
        for (ItemStack item : getPlayer().getInventory().getContents()){
            if (item != null && item.getType() == Material.PLAYER_HEAD){
                i++;
            }
        }
        return i;
    }

    public void giveArmor1(){
        ItemBuilder item1 = new ItemBuilder(Material.NETHERITE_HELMET);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.0025, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.0025, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.0025, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.0025, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        item1.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        item1.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED, modifier);

        ItemBuilder item2 = new ItemBuilder(Material.NETHERITE_CHESTPLATE);
        item2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        item2.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED, modifier2);

        ItemBuilder item3 = new ItemBuilder(Material.NETHERITE_LEGGINGS);
        item3.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        item3.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED, modifier3);

        ItemBuilder item4 = new ItemBuilder(Material.NETHERITE_BOOTS);
        item4.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        item4.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);


        getPlayer().getInventory().addItem(item1.toItemStack(), item2.toItemStack(), item3.toItemStack(), item4.toItemStack());
    }

    public void giveArmor2(){
        ItemBuilder item1 = new ItemBuilder(Material.NETHERITE_HELMET);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.0045, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(), "generic.moreHealth", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.0045, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.0045, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.0045, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        item1.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        item1.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED, modifier);
        item1.addAttribute(Attribute.GENERIC_MAX_HEALTH, modifier1);

        ItemBuilder item2 = new ItemBuilder(Material.DIAMOND_CHESTPLATE);
        item2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        item2.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED, modifier2);

        ItemBuilder item3 = new ItemBuilder(Material.NETHERITE_LEGGINGS);
        item3.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        item3.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED, modifier3);

        ItemBuilder item4 = new ItemBuilder(Material.DIAMOND_BOOTS);
        item4.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        item4.addEnchant(Enchantment.PROTECTION_FALL, 1);
        item4.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);


        getPlayer().getInventory().addItem(item1.toItemStack(), item2.toItemStack(), item3.toItemStack(), item4.toItemStack());
    }

    public void giveWeapon1(){
        ItemBuilder item1 = new ItemBuilder(Material.NETHERITE_SWORD);
        item1.addEnchant(Enchantment.DAMAGE_ALL, 2);

        getPlayer().getInventory().addItem(item1.toItemStack());
    }

    public void giveShield(){
        ItemBuilder item1 = new ItemBuilder(Material.SHIELD);
        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(), "generic.moreHealth", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND);
        item1.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        item1.addAttribute(Attribute.GENERIC_MAX_HEALTH, modifier1);

        getPlayer().getInventory().addItem(item1.toItemStack());
    }

    public void giveWeapon2(){
        ItemBuilder item1 = new ItemBuilder(Material.DIAMOND_SWORD);
        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(), "generic.moreHealth", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        item1.addEnchant(Enchantment.DAMAGE_ALL, 3);
        item1.addAttribute(Attribute.GENERIC_MAX_HEALTH, modifier1);

        getPlayer().getInventory().addItem(item1.toItemStack());
    }
}
