package me.G4meM0ment.Ambience.Handler;

import java.util.HashMap;

import me.G4meM0ment.Ambience.DataStorage.SoundData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.SoundManager;

public class SoundHandler {
	
	private RPGEssentials plugin;
	private SoundManager sm;
	private SoundData sd;
	
	private static HashMap<String, String> music = new HashMap<String, String>();
	private static HashMap<String, HashMap<String, Long>> exactMusic = new HashMap<String, HashMap<String, Long>>();
	
	public SoundHandler(RPGEssentials plugin)
	{
		this.plugin = plugin;
		sm = SpoutManager.getSoundManager();
		sd = new SoundData();
	}
	public SoundHandler()
	{
		plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		sm = SpoutManager.getSoundManager();
		sd = new SoundData();
	}

	public boolean hasSilence(Player p)
	{
		if(!getPlayersMusic().containsKey(p.getName())) return true;
		if(getPlayersMusic().get(p.getName()).isEmpty())  return true;
		return false;
	}
	public boolean hasCombatMusic(Player p)
	{
		if(!getPlayersMusic().containsKey(p.getName())) return false;
		if(getPlayersMusic().get(p.getName()).equalsIgnoreCase("combat")) return true;
		return false;
	}
	public boolean hasBiomeMusic(Player p)
	{
		if(!getPlayersMusic().containsKey(p.getName())) return false;
		if(getPlayersMusic().get(p.getName()).equalsIgnoreCase("biome")) return true;
		return false;
	}
	public boolean hasRegionMusic(Player p)
	{
		if(!getPlayersMusic().containsKey(p.getName())) return false;
		if(getPlayersMusic().get(p.getName()).equalsIgnoreCase("region")) return true;
		return false;
	}
	public HashMap<String, String> getPlayersMusic()
	{
		return music;
	}
	public HashMap<String, HashMap<String, Long>> getPlayersExactMusic()
	{
		return exactMusic;
	}

	public void prePlay(String trigger, SpoutPlayer p) 
	{	
		if(trigger.isEmpty()) return;
		String sound = getSound(p.getPlayer(), trigger);
		if(sound.isEmpty() || trigger.equalsIgnoreCase("silence"))
		{
			setPlayersExactMusic(p.getPlayer(), sound, 0L);
			storeMusic(p, "");
			sm.playCustomMusic(plugin, p, "silence.wav", false);
		}
		
		String type = sd.getConfig().getString(trigger+".type");
		
		if(type == null) return;

		if(type.equalsIgnoreCase("sound") || type.equalsIgnoreCase("global_sound"))
		{
			int distance = sd.getConfig().getInt(trigger+".distance");
			int vol = sd.getConfig().getInt(trigger+".vol");
			playSound(trigger, sound, type, p, distance, vol);
		}
		else if(type.equalsIgnoreCase("music") || type.equalsIgnoreCase("global_music"))
		{
			int length = sd.getConfig().getInt(trigger+".length");
			playMusic(trigger, sound, type, p, length);
		}
	}
	
	public String getSound(Player p, String trigger)
	{
		if(trigger.isEmpty()) return "";
		for(String s : sd.getConfig().getKeys(false))
		{
			if(s.equalsIgnoreCase(trigger))
				if(p.getWorld().getTime() < 12000 && sd.getConfig().getConfigurationSection(trigger).getKeys(false).contains("url_night"))
					if(sd.getConfig().getBoolean(s+".preLogin"))
						return sd.getConfig().getString(s+".url_night").split("/")[sd.getConfig().getString(s+".url_night").split("/").length-1];
					else
						return sd.getConfig().getString(s+".url_night");
				else
					if(sd.getConfig().getBoolean(s+".preLogin"))
						return sd.getConfig().getString(s+".url").split("/")[sd.getConfig().getString(s+".url").split("/").length-1];
					else
						return sd.getConfig().getString(s+".url");
		}
		return "";
	}
	
	public void musicEnded(final Player p, int i, final String sound, final Long then)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{
			@Override
			public void run() 
			{
				if(!getPlayersExactMusic().containsKey(p.getName())) return;
				if(!(getPlayersExactMusic().get(p.getName()).containsKey(sound))) return;
				if(!hasSilence(p) && (getPlayersExactMusic().get(p.getName()).get(sound) /100) == (then /100))
				{
					getPlayersMusic().put(p.getName(), "");
					setPlayersExactMusic(p.getPlayer(), sound, 0L);
				}
			}
		}, i*20);
	}

	public void playSound(String trigger, String sound, String type, SpoutPlayer p, int distance, int vol)
	{
		if(type.equalsIgnoreCase("sound"))
			sm.playCustomSoundEffect(plugin, p, sound, false, p.getLocation(), distance, vol);
		else if(type.equalsIgnoreCase("global_sound"))
			sm.playGlobalCustomSoundEffect(plugin, sound, false, p.getLocation(), distance, vol);
	}
	public void playMusic(String trigger, final String sound, String type, final SpoutPlayer p, int length)
	{
		if(type.equalsIgnoreCase("music"))
		{
			if(!hasMusic(p, trigger, sound))
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { @Override public void run() { sm.playCustomMusic(plugin, p, sound, false); } }, 1);
				sm.playCustomMusic(plugin, p, sound, false);
				storeMusic(p, trigger);
				setPlayersExactMusic(p.getPlayer(), sound, System.currentTimeMillis());
				musicEnded(p, length, sound, System.currentTimeMillis());
			}
		}
		else if(type.equalsIgnoreCase("global_music")) 
		{
			if(!hasMusic(p, trigger, sound))
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { @Override public void run() { sm.playGlobalCustomMusic(plugin, sound, false); } }, 1);
				storeMusic(p, trigger);
				setPlayersExactMusic(p.getPlayer(), sound, System.currentTimeMillis());
			}
		}
	}
	
	public void storeMusic(SpoutPlayer sp, String trigger)
	{	
		if(sp == null) return;
		String p = sp.getName();
	
		if(trigger.isEmpty())
		{
			getPlayersMusic().put(p, "");
		}
		else if(trigger.equalsIgnoreCase("combat"))
		{
			getPlayersMusic().put(p, trigger);
		}
		else if(isBiome(trigger))
		{
			getPlayersMusic().put(p, "biome");
		}
		else if(plugin.getWorldGuard().getRegionManager(sp.getWorld()).hasRegion(trigger))
		{
			getPlayersMusic().put(p, "region");
		}
	}
	
	public boolean isBiome(String b)
	{
		if(b.equalsIgnoreCase("nether")) return true;
		if(b.equalsIgnoreCase("end")) return true;
		if(b.equalsIgnoreCase("underground")) return true;
		
		for(Biome biome : Biome.values())
			if(biome.toString().equalsIgnoreCase(b))
				return true;
		return false;			
	}
	
	public String getMusic(Player p, String trigger)
	{
		if(trigger.equalsIgnoreCase("combat")) return "combat";
		if(isBiome(trigger)) return "biome";
		if(plugin.getWorldGuard().getRegionManager(p.getWorld()).hasRegion(trigger)) return "region";
		else return "";
	}
	
	public boolean hasMusic(Player p, String trigger, String sound)
	{
		String type = getMusic(p, trigger);
		if(type.isEmpty()) return true;
		if(hasSilence(p)) return false;
		if(hasCombatMusic(p)) return true;
		if(type.equalsIgnoreCase("combat")) return false;
		if(!exactMusic.containsKey(p.getName())) return false;
		if(hasRegionMusic(p) && type.equalsIgnoreCase("region") && exactMusic.get(p.getName()).containsKey(sound)) return true;
		if(hasBiomeMusic(p) && type.equalsIgnoreCase("region")) return false;
		if(hasBiomeMusic(p) && type.equalsIgnoreCase("biome") && exactMusic.get(p.getName()).containsKey(sound)) return true;
		if(!hasBiomeMusic(p) && type.equalsIgnoreCase("biome")) return false;
		return true;
	}
	
	public void setPlayersExactMusic(Player p, String s, Long i)
	{
		HashMap<String, Long> exactSound = new HashMap<String, Long>();
		exactSound.put(s, i);
		getPlayersExactMusic().put(p.getName(), exactSound);
	}
}
