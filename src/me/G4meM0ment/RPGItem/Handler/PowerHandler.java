package me.G4meM0ment.RPGItem.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Utils.InvisibilityHandler;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PowerHandler {
	
	//TODO add regeneration, 
	
	private RPGEssentials plugin;
	private RPGItem subplugin;
	private ItemConfig itemConfig;
	private ListHandler lh;
	private ItemHandler itemHandler;
	private CustomItemHandler customItemHandler;
	private InvisibilityHandler iH;
	
	private static ConcurrentHashMap<Player, ConcurrentHashMap<String, Double>> playerPowers = new ConcurrentHashMap<Player, ConcurrentHashMap<String, Double>>();
	
	public PowerHandler(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		subplugin = new RPGItem();
		itemConfig = new ItemConfig();
		lh = new ListHandler();
		itemHandler = new ItemHandler();
		customItemHandler = new CustomItemHandler();
		iH = new InvisibilityHandler();
		start();
	}
	public PowerHandler() 
	{
		subplugin = new RPGItem();
		itemConfig = new ItemConfig();
		lh = new ListHandler();
		itemHandler = new ItemHandler();
		customItemHandler = new CustomItemHandler();
		iH = new InvisibilityHandler();
	}
	
	public ConcurrentHashMap<Player, ConcurrentHashMap<String, Double>> getPlayerPowers() 
	{
		return playerPowers;
	}
	public ConcurrentHashMap<String, Double> getPlayersPowers(Player p)
	{
		return getPlayerPowers().get(p);
	}
	
	public boolean hasPower(Player p, String effectName) 
	{
		if(p == null || effectName == null || effectName.isEmpty()) return false;
		if(!getPlayerPowers().containsKey(p)) return false;
		ConcurrentHashMap<String, Double> effects = getPlayersPowers(p);
		if(effects.size() < 1) return false;
		
		for(String s : effects.keySet())
			if(s.equalsIgnoreCase(effectName))
				return true;
		return false;
	}
	
	private boolean itemHasPower(CustomItem i)
	{
		if(i == null) return false;
		if(itemConfig.getConfig(itemConfig.getFile(i.getDispName())) == null) return false;
		ConfigurationSection section = itemConfig.getConfig(itemConfig.getFile(i.getDispName())).getConfigurationSection("powers");
		if(section == null) return false;
		for(String s : lh.getPowers())
		{
			if(!section.contains(s)) continue;
			if(itemConfig.getConfig(itemConfig.getFile(i.getDispName())).getInt("powers."+s) >= 0)
				return true;
		}
		return false;
	}
	private List<String> getItemPowers(CustomItem i)
	{
		List<String> powers = new ArrayList<String>();
		for(String s : lh.getPowers())
		{
			if(itemConfig.getConfig(itemConfig.getFile(i.getDispName())).getInt("powers."+s) >= 0)
				powers.add(s);
		}
		return powers;
	}
	
	private void applyPowers(Player p)
	{
		ConcurrentHashMap<String, Double> powers = getPlayerPowers().get(p);
		for(String s : powers.keySet())
		{
			switch(s)
			{
			case "speed":
				if(p.getWalkSpeed()+powers.get(s) <= 1)
					p.setWalkSpeed((float) (p.getWalkSpeed()+powers.get(s)));
				else if(p.getWalkSpeed()+powers.get(s) < 0)
					p.setWalkSpeed(0F);
				else
					p.setWalkSpeed(1);
				break;
			/*case "jump":
				SpoutManager.getPlayer(p).setJumpingMultiplier(SpoutManager.getPlayer(p).getJumpingMultiplier()+powers.get(s));
				break; */
			case "invisibility":
				iH.hidePlayer(p);
			case "scuba":
				p.setRemainingAir(p.getMaximumAir());
			case "nightvision":
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, powers.get(s).intValue()));
			}
		}
	}
	
	private void removeEmptyPowers(Player p)
	{
		if(p == null) return;
		if(!getPlayerPowers().containsKey(p)) return;
		for(String s : getPlayersPowers(p).keySet())
		{
			if(!getPlayersPowers(p).containsKey(s)) continue;
			if(getPlayersPowers(p).get(s) <= 0)
				getPlayersPowers(p).keySet().remove(s);
		}
	}
	
	private void removeAppliedPowers(Player p)
	{
		if(!hasPower(p, "speed"))
			p.setWalkSpeed(0.2F);
		if(iH.isHiden(p) && !hasPower(p, "invisibility"))
			iH.showPlayer(p);
		if(!hasPower(p, "nightvision"))
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
	}
	
	private void start() 
	{
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
		    @Override
		    public void run() 
		    {
		    	for(Player p : Bukkit.getOnlinePlayers())
		    	{
		    		/*
		    		 * 
		    		 * TODO check if power is active or passive
		    		 * 
		    		 */
		    		ConcurrentHashMap<String, Double> powers = new ConcurrentHashMap<String, Double>();
		    		if(itemHandler.isCustomItem(p.getItemInHand()) && itemHasPower(customItemHandler.getCustomItem(p.getItemInHand())))
		    		{
		    			ItemStack i = p.getItemInHand();
		    			CustomItem cItem = customItemHandler.getCustomItem(i);
	    				if(subplugin.getConfig().getInt("DurabilityAffectingUtility") > 0) 
	    				{
	    					int durability = cItem.getDurability();
	    					int maxDurability = itemConfig.getConfig(itemConfig.getFile(cItem.getDispName())).getInt("durability");
	    					int percent = (durability * 100) / maxDurability;
	    					if(percent < subplugin.getConfig().getInt("DurabilityAffectingUtility"))
	    						continue;
	    				}
		    			for(String power : getItemPowers(customItemHandler.getCustomItem(i)))
		    			{
		    				if(powers.containsKey(power))
		    					powers.put(power, powers.get(power)+itemConfig.getConfig(itemConfig.getFile(customItemHandler.getCustomItem(i).getDispName())).getDouble("powers."+power));
		    				else
		    					powers.put(power, itemConfig.getConfig(itemConfig.getFile(customItemHandler.getCustomItem(i).getDispName())).getDouble("powers."+power));
		    			}
		    		}
		    		
		    		for(ItemStack i : p.getInventory().getArmorContents())
		    			if(itemHandler.isCustomItem(i) && itemHasPower(customItemHandler.getCustomItem(i)))
		    			{
		    				CustomItem cItem = customItemHandler.getCustomItem(i);
	    					if(subplugin.getConfig().getInt("DurabilityAffectingUtility") > 0) 
	    					{
	    						int durability = cItem.getDurability();
	    						int maxDurability = itemConfig.getConfig(itemConfig.getFile(cItem.getDispName())).getInt("durability");
	    						int percent = (durability * 100) / maxDurability;
	    						if(percent < subplugin.getConfig().getInt("DurabilityAffectingUtility"))
	    							continue;
	    					}
		    				for(String power : getItemPowers(customItemHandler.getCustomItem(i)))
		    				{
		    					if(powers.containsKey(power))
		    						powers.put(power, powers.get(power)+itemConfig.getConfig(itemConfig.getFile(customItemHandler.getCustomItem(i).getDispName())).getInt("powers."+power));
		    					else
		    						powers.put(power, itemConfig.getConfig(itemConfig.getFile(customItemHandler.getCustomItem(i).getDispName())).getDouble("powers."+power));
		    				}
		    			}
		    		
		    		removeAppliedPowers(p);
		    		getPlayerPowers().put(p, powers);
		    		removeEmptyPowers(p);
		    		applyPowers(p);
		    	}
		    }
		}, 0, 60);
	}
}
