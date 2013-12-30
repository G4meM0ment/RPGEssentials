package me.G4meM0ment.Junkie.Handler;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.player.SkyManager;
import org.getspout.spoutapi.player.SpoutPlayer;

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
					if(p.isDead()) return;
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
						
						if (dd.getConfig().getInt(p.getName()+"."+drug+".addicted")-5 <= 0)
							dd.getConfig().set(p.getName()+"."+drug+".addicted", 0);						
						else
							dd.getConfig().set(p.getName()+"."+drug+".addicted", dd.getConfig().getInt(p.getName()+"."+drug+".addicted")-5);
						
						if(dd.getConfig().getInt(p.getName()+"."+drug+".overdose")-20 <= 0)
							dd.getConfig().set(p.getName()+"."+drug+".overdose", 0);
						else
						dd.getConfig().set(p.getName()+"."+drug+".overdose", dd.getConfig().getInt(p.getName()+"."+drug+".overdose")-20);
						
						dd.saveConfig();
						
						int addicted = dd.getConfig().getInt(p.getName()+"."+drug+".addicted");
						long time = System.currentTimeMillis() - dd.getConfig().getLong(p.getName()+"."+drug+".consum");
						if(addicted > 0 && !dd.getConfig().getBoolean(p.getName()+"."+drug+".clean") && time < 600000)
						{
							int random = (int) (Math.random()*12000);
							final Player fP = p;
							final int fDrug = drug;
							Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
							{
								@Override
								public void run()
								{
									addWithdrawalSymptom(fP, fDrug);
								}
							}, random);

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
	
	public void consum(final Player p, int drug)			//DROGEN-Effekte
	{
		switch(drug)
		{
		case 39: 
			p.getWorld().playSound(p.getLocation(), Sound.EAT, 1, 0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3600, 1));
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			
			int addicted = dd.getConfig().getInt(p.getName()+"."+drug+".addicted");
			if(addicted+33 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".addicted", addicted+33);
			else
			{
				dd.getConfig().set(p.getName()+"."+drug+".clean", false);
				dd.getConfig().set(p.getName()+"."+drug+".addicted", 100);
			}
			
			int overdose = dd.getConfig().getInt(p.getName()+"."+drug+".overdose");
			if(overdose+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".overdose", overdose+20);
			else
				tookOverdose(p, drug);
			
			dd.saveConfig();
			break;
		case 40: 
			p.getWorld().playSound(p.getLocation(), Sound.EAT, 1, 0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7200, 2));
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			
			addicted = dd.getConfig().getInt(p.getName()+"."+drug+".addicted");
			if(addicted+33 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".addicted", addicted+33);
			else
			{
				dd.getConfig().set(p.getName()+"."+drug+".clean", false);
				dd.getConfig().set(p.getName()+"."+drug+".addicted", 100);
			}
			
			overdose = dd.getConfig().getInt(p.getName()+"."+drug+".overdose");
			if(overdose+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".overdose", overdose+20);
			else
				tookOverdose(p, drug);
			
			dd.saveConfig();
			break;
		case 353: 
			p.getWorld().playSound(p.getLocation(), Sound.BREATH, 1, 0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 1));
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			
			addicted = dd.getConfig().getInt(p.getName()+"."+drug+".addicted");
			if(addicted+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".addicted", addicted+20);
			else
			{
				dd.getConfig().set(p.getName()+"."+drug+".clean", false);
				dd.getConfig().set(p.getName()+"."+drug+".addicted", 100);
			}
			
			overdose = dd.getConfig().getInt(p.getName()+"."+drug+".overdose");
			if(overdose+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".overdose", overdose+20);
			else
				tookOverdose(p, drug);
			
			dd.saveConfig();
			break;
		case 357:
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			p.getWorld().playSound(p.getLocation(), Sound.EAT, 1, 0);
			
			final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run() 
				{
					if(p.getFoodLevel() > 0)
						p.setFoodLevel(p.getFoodLevel()-1);
				}
			}, 0, 40);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run() 
				{
					Bukkit.getScheduler().cancelTask(id);
				}
			}, 800);
			
			final SpoutPlayer sp = SpoutManager.getPlayer(p);
			final SkyManager sky = SpoutManager.getSkyManager();
			sky.setSkyColor(sp, new Color(255, 0, 255));
			sky.setCloudColor(sp, new Color(153, 60, 153));
			sky.setFogColor(sp, new Color(255, 153, 255));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
						sky.setCloudColor(sp, Color.remove());
						sky.setFogColor(sp, Color.remove());
						sky.setSkyColor(sp, Color.remove());
				}
			}, 12000);

			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			dd.saveConfig();
			break;
		case 377: 
			p.getWorld().playSound(p.getLocation(), Sound.BREATH, 1, 0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 3));
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			
			addicted = dd.getConfig().getInt(p.getName()+"."+drug+".addicted");
			if(addicted+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".addicted", addicted+20);
			else
			{
				dd.getConfig().set(p.getName()+"."+drug+".clean", false);
				dd.getConfig().set(p.getName()+"."+drug+".addicted", 100);
			}
			
			overdose = dd.getConfig().getInt(p.getName()+"."+drug+".overdose");
			if(overdose+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".overdose", overdose+20);
			else
				tookOverdose(p, drug);
			
			dd.saveConfig();
			break;
		case 348: 
			p.getWorld().playSound(p.getLocation(), Sound.GHAST_SCREAM, 1, 0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 1));
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			
			addicted = dd.getConfig().getInt(p.getName()+"."+drug+".addicted");
			if(addicted+30 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".addicted", addicted+30);
			else
			{
				dd.getConfig().set(p.getName()+"."+drug+".clean", false);
				dd.getConfig().set(p.getName()+"."+drug+".addicted", 100);
			}
			
			overdose = dd.getConfig().getInt(p.getName()+"."+drug+".overdose");
			if(overdose+45 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".overdose", overdose+45);
			else
				tookOverdose(p, drug);
			
			dd.saveConfig();
			break;
		case 282:
			p.getWorld().playSound(p.getLocation(), Sound.BURP, 1, 0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2500, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1100, 1));
			dd.getConfig().set(p.getName()+"."+drug+".consum", System.currentTimeMillis());
			
			overdose = dd.getConfig().getInt(p.getName()+"."+drug+".overdose");
			if(overdose+20 <= 100)
				dd.getConfig().set(p.getName()+"."+drug+".overdose", overdose+20);
			else
				tookOverdose(p, drug);
			
			dd.saveConfig();
			break;
		
		default:
			break;
		}
	}

	private void tookOverdose(Player p, int drug) 							//DROGEN-Überdosis
	{
		switch(drug)
		{
		case 39:
			dd.getConfig().set(p.getName()+"."+drug+".overdose", 0);
			dd.saveConfig();
			p.removePotionEffect(PotionEffectType.REGENERATION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6000, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 12000, 2));
		case 40:
			dd.getConfig().set(p.getName()+"."+drug+".overdose", 0);
			dd.saveConfig();
			p.removePotionEffect(PotionEffectType.REGENERATION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 12000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 12000, 2));
		case 353:
			dd.getConfig().set(p.getName()+"."+drug+".overdose", 0);
			dd.saveConfig();
			p.removePotionEffect(PotionEffectType.SPEED);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 3600, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3600, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 12000, 2));

			break;
		case 377:
			dd.getConfig().set(p.getName()+"."+drug+".overdose", 0);
			dd.saveConfig();
			p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 12000, 3));
			p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3600, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 12000, 2));

			break;
		case 348:
			dd.getConfig().set(p.getName()+"."+drug+".overdose", 0);
			dd.saveConfig();
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 6000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 12000, 2));
			break;
			
		case 282:
			dd.getConfig().set(p.getName()+"."+drug+".overdose", 0);
			dd.saveConfig();
			p.removePotionEffect(PotionEffectType.CONFUSION);
			p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 140, 5));
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 2400, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1500, 2));
			p.teleport(new Location(Bukkit.getServer().getWorld("OrbiA"), 1771.0, 72, 902));
		}
	}
	private void addWithdrawalSymptom(Player p, int drug) 							//Entzugserscheinungen
	{
		final Player fP = p;
		switch(drug)
		{
		case 39:
			int random = (int) (Math.random()*1200);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 6000, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1300, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1000, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
				}
			}, random);
			break;
		case 40:
			random = (int) (Math.random()*1200);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 6000, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1500, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1300, 2));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 900, 2));
				}
			}, random);
			break;
		case 353:
			random = (int) (Math.random()*1200);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 6000, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1500, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1300, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 1));
				}
			}, random);
			break;
		case 377:
			random = (int) (Math.random()*1200);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 6000, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3600, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 3600, 1));
				}
			}, random);
		case 348:
			random = (int) (Math.random()*1200);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 6000, 1));
				}
			}, random);
			random = (int) (Math.random()*1200);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
			{
				@Override
				public void run()
				{
					fP.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3700, 1));
				}
			}, random);
			break;
		}
	}
}
