package me.G4meM0ment.Junkie.Handler;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.G4meM0ment.Junkie.Junkie;
import me.G4meM0ment.Junkie.DataStorage.DrugData;

public class DrugHandler {
	
	@SuppressWarnings("unused")
	private Junkie subplugin;
	private DrugData dd;
	
	public DrugHandler(Junkie subplugin)
	{
		this.subplugin = subplugin;
		dd = new DrugData();
		
		start();
	}
	public DrugHandler() 
	{
		subplugin = new Junkie();
		dd = new DrugData();
	}
	
	
	public void start()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
		{
			@Override
			public void run()
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					Set<String> keys = null;
					try
					{
						keys = dd.getConfig().getConfigurationSection(p.getName()).getKeys(false);
					} catch(NullPointerException e)
					{
						continue;
					}
					
					for(String s : keys)
					{
						int drug = Integer.parseInt(s);
						dd.getConfig().set(p.getName()+"."+drug+".addicted", dd.getConfig().getInt(p.getName()+"."+drug+".addicted")-5);
						dd.getConfig().set(p.getName()+"."+drug+".overdose", dd.getConfig().getInt(p.getName()+"."+drug+".overdose")-20);
						dd.saveConfig();
						
						int addicted = dd.getConfig().getInt(p.getName()+"."+drug+".addicted");
						if(addicted > 0 && !dd.getConfig().getBoolean(p.getName()+"."+drug+".clean"))
						{
							addWithdrawalSymptom(p, drug);
						}
						else if(addicted <= 0)
						{
							dd.getConfig().set(p.getName()+"."+drug+".clean", true);
						}
						dd.saveConfig();
					}

				}
			}
		}, 0, 12000);
	}
	
	public void consum(Player p, int drug)
	{
		switch(drug)
		{
		case 353: 
			p.getWorld().playSound(p.getLocation(), Sound.BREATH, 1, 0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 1));
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			
			int addicted = dd.getConfig().getInt(p.getName()+"."+drug+".addicted");
			if(addicted+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".addicted", addicted+20);
			else
				dd.getConfig().set(p.getName()+"."+drug+".clean", false);
			
			int overdose = dd.getConfig().getInt(p.getName()+"."+drug+".overdose");
			if(overdose+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".overdose", overdose+20);
			else
				tookOverdose(p, drug);
			
			dd.saveConfig();
			break;
		case 357:
			p.getWorld().playSound(p.getLocation(), Sound.EAT, 1, 0);
			p.setFoodLevel(1);
			//TODO add spout feature: change sky color
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			dd.saveConfig();
			break;
		default:
			break;
		}
	}

	private void tookOverdose(Player p, int drug) 
	{
		switch(drug)
		{
		case 353:
			dd.getConfig().set(p.getName()+"."+drug+".overdose", 0);
			dd.saveConfig();
			p.removePotionEffect(PotionEffectType.SPEED);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 3600, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3600, 2));

			break;
		}
	}
	private void addWithdrawalSymptom(Player p, int drug) 
	{
		switch(drug)
		{
		case 353:
			p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1500, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1300, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 1));
			//TODO add kotzeffekt
			break;
		}
	}
}
