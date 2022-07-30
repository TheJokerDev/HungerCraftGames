package me.thejokerdev.hungercraftgames.managers;

import lombok.Getter;
import me.thejokerdev.hungercraftgames.Main;
import me.thejokerdev.hungercraftgames.game.District;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DistrictsManager {
    private Main plugin;
    private List<District> districts;

    public DistrictsManager(Main plugin){
        this.plugin = plugin;
        districts = new ArrayList<>();
        init();
    }

    public void init(){
        for (String s : plugin.getConfig().getConfigurationSection("districts").getKeys(false)){
            districts.add(new District(plugin, s));
        }
    }

    public boolean contains(String name){
        return districts.stream().anyMatch(d -> d.getTributes().contains(name.toLowerCase()));
    }


}
