package me.G4meM0ment.Ambience.Handler;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.Ambience.DataStorage.SoundData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.FileManager;

public class CacheHandler {
	
	private RPGEssentials plugin;
	private SoundData sd;
	private SoundHandler sh;
	
	public CacheHandler(RPGEssentials plugin)
	{
		this.plugin = plugin;
		sd = new SoundData();
		sh = new SoundHandler();
	}
	public CacheHandler()
	{
		plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		sd = new SoundData();
		sh = new SoundHandler();
	}
	
	/*
	 * 
	 * WORKAROUND need to cache files untemp after login 
	 * 
	 * 
	 */
	public void cacheFiles() 
	{	
		FileManager fm = SpoutManager.getFileManager();
		fm.addToPreLoginCache(plugin, "https://dl.dropboxusercontent.com/u/96045686/SoundPack/silence.wav");
		List<String> urls = new ArrayList<String>();
		
		for(String s : sd.getConfig().getKeys(false))
		{
			if(!urls.contains(sd.getConfig().getString(s+".url")) && !urls.contains(sd.getConfig().getString(s+".url_night")))
				if(sd.getConfig().getBoolean(s+".preLogin"))
					fm.addToPreLoginCache(plugin, sd.getConfig().getString(s+".url"));
				urls.add(sd.getConfig().getString(s+".url"));
				if(sd.getConfig().getConfigurationSection(s).getKeys(false).contains("url_night"))
					if(sd.getConfig().getBoolean(s+".preLogin"))
						fm.addToCache(plugin, sd.getConfig().getString(s+".url_night"));
		}
	}
	
	public String getCombatSound()
	{
		for(String s : sd.getConfig().getKeys(false))
		{
			if(s.equalsIgnoreCase("combat"))
				return s;
		}
		return "";
	}
	public String getBiomeSound(String b)
	{
		for(String s : sd.getConfig().getKeys(false))
		{
			if(s.equalsIgnoreCase(b))
				return s;
			if(sh.isBiome(s) && sh.isBiome(b) && s.equalsIgnoreCase(b))
				return s;
		}
		return "";
	}
	public String getRegionSound(Player p, String r)
	{
		for(String s : sd.getConfig().getKeys(false))
		{
			if(s.equalsIgnoreCase(r))
				if(plugin.getWorldGuard().getRegionManager(p.getWorld()).hasRegion(r))
				return s;
		}
		return "";
	}
}
