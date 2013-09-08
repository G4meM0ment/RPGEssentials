package me.G4meM0ment.RPGItem.Handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PowerHandler {
	
	//TODO add regeneration, 
	
	private RPGEssentials plugin;
	private RPGItem rpgitem;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private ListHandler lh;
	
	private static HashMap<Player, List<String>> playerPowers = new HashMap<Player, List<String>>();
	
	public PowerHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		rpgitem = new RPGItem();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		start();
	}
	public PowerHandler() {
		rpgitem = new RPGItem();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
	}
	
	public HashMap<Player, List<String>> getPlayerPowers() {
		return playerPowers;
	}
	
	public boolean hasPower(Player p, String effectName) {
		if(!getPlayerPowers().containsKey(p)) return false;
		List<String> effects = getPlayerPowers().get(p);
		if(effects.size() < 1) return false;
		
		for(String s : effects) {
			if(s.equalsIgnoreCase(effectName)) {
				return true;
			}
		}
		return false;
	}
	
	public void applyPower(Player p, CustomItem item) {
		if(p == null || item == null) return;
		String name = item.getDispName();
		File configFile = itemConfig.getFile(name);
		File dataFile = itemData.getFile(name);
		if(dataFile == null || configFile == null) return;
		FileConfiguration config = itemConfig.getConfig(configFile);
		FileConfiguration data = itemData.getDataFile(dataFile);
		if(rpgitem.getConfig().getInt("DurabilityAffectingUtility") > 0) {

			int durability = data.getInt(Integer.toString(item.getId()));
			int maxDurability = config.getInt("durability");
			int percent = (durability * 100) / maxDurability;
			if(percent < rpgitem.getConfig().getInt("DurabilityAffectingUtility"))
				return;
		}
		
		for(String s : lh.getPowers()) {
			if(config.getInt("powers."+s) >= 1) {
				if(!hasPower(p, s)) {
					if(!getPlayerPowers().containsKey(p))
						getPlayerPowers().put(p, new ArrayList<String>());
					getPlayerPowers().get(p).add(s+":"+config.getInt("powers."+s));
				}
			}
		}
	}
	
	public void removePower(Player p, CustomItem item) {
		String name = item.getDispName();
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(name));
		
		for(String s : lh.getPowers()) {
			if(config.getInt("powers."+s) >= 0) {
				if(hasPower(p, s)) {
					getPlayerPowers().get(p).remove(s+":"+config.getInt("powers."+s));
				}
			}
		}
	}
	
	public void clearPowers(Player p) {
		if(getPlayerPowers().containsKey(p)) {
			getPlayerPowers().get(p).clear();
		}
	}
	
	public PotionEffectType getPotionEffectType(String effectName) {
		switch(effectName) {
			case "nightvision":
				return PotionEffectType.NIGHT_VISION;
			case "speed":
				return PotionEffectType.SPEED;	
			case "jump":
				return PotionEffectType.JUMP;
			case "scuba":
				return PotionEffectType.WATER_BREATHING;
			case "invisibility":
				return PotionEffectType.INVISIBILITY;
		}
		return null;
	}
	
	private void start() {
		
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
		    @Override
		    public void run() {
		        for (Player p : getPlayerPowers().keySet()) {

		            for (String s : getPlayerPowers().get(p)) {
		            	String effect = s.split(":")[0];
		            	int strength = Integer.valueOf(s.split(":")[1]);
		            	if(effect.equalsIgnoreCase("invisibility")) {
		            		if(p.isSneaking())
		            			p.addPotionEffect(new PotionEffect(getPotionEffectType(effect), 20, strength));
		            	} else
		            		p.addPotionEffect(new PotionEffect(getPotionEffectType(effect), 20, strength));
		            }
		        }
		     
		    }
		 
		}, 0, 20);
	}
}
