package me.G4meM0ment.Orbia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.Orbia.Handler.GolemHandler;
import me.G4meM0ment.Orbia.Handler.ManaHandler;
import me.G4meM0ment.Orbia.Handler.SIHandler;
import me.G4meM0ment.Orbia.Handler.Job.JobListener;
import me.G4meM0ment.Orbia.Listener.CraftListener;
import me.G4meM0ment.Orbia.Listener.CustomItemListener;
import me.G4meM0ment.Orbia.Listener.DuellListener;
import me.G4meM0ment.Orbia.Listener.FoodListener;
import me.G4meM0ment.Orbia.Listener.GraceListener;
import me.G4meM0ment.Orbia.Listener.HeroesListener;
import me.G4meM0ment.Orbia.Listener.MAListener;
import me.G4meM0ment.Orbia.Listener.PListener;
import me.G4meM0ment.Orbia.Listener.TitleListener;
import me.G4meM0ment.Orbia.Tutorial.TutorialData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Orbia {
	private RPGEssentials plugin;
	private PListener plistener;
	private TutorialData tutData;
	@SuppressWarnings("unused")
	private GolemHandler golemHandler;
	private ManaHandler manaHandler;
	private SIHandler sih;
	private JobListener jl;
	private HeroesListener hl;
	private MAListener mal;
	private CraftListener cl;
	private DuellListener dl;
	private TitleListener tl;
	private GraceListener gl;
	private CustomItemListener cil;
	private FoodListener fl;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "Orbia: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;

	public Orbia(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		plistener = new PListener(plugin);
		jl = new JobListener(plugin);
		hl = new HeroesListener(plugin);
		mal = new MAListener(plugin);
		cl = new CraftListener();
		dl = new DuellListener(plugin);
		tl = new TitleListener();
		gl = new GraceListener(plugin);
		cil = new CustomItemListener();
		fl = new FoodListener(plugin);
		
		plugin.getServer().getPluginManager().registerEvents(plistener, plugin);
		plugin.getServer().getPluginManager().registerEvents(jl, plugin);
		plugin.getServer().getPluginManager().registerEvents(hl, plugin);
		plugin.getServer().getPluginManager().registerEvents(mal, plugin);
		plugin.getServer().getPluginManager().registerEvents(cl, plugin);
		plugin.getServer().getPluginManager().registerEvents(dl, plugin);
		plugin.getServer().getPluginManager().registerEvents(tl, plugin);
		plugin.getServer().getPluginManager().registerEvents(gl, plugin);
		plugin.getServer().getPluginManager().registerEvents(cil, plugin);
		plugin.getServer().getPluginManager().registerEvents(fl, plugin);
		
		dir = plugin.getDir()+"/Orbia";
		logger = plugin.getLogger();
		configFile = new File(dir+"/config.yml");
		
		tutData = new TutorialData(this);
		golemHandler = new GolemHandler(plugin);
		sih = new SIHandler(this);
		manaHandler = new ManaHandler(plugin);
	}
	public Orbia()
	{

	}

	public boolean onEnable() 
	{
		if(!plugin.getConfig().getBoolean("OrbiaEnabled"))
			return false;
		
		//creating config or loading
		reloadConfig();
		saveConfig();
		tutData.reloadConfig();
		tutData.saveConfig();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker deleteset id:markers");
				tl.startAutoUpdater();
			}
		});
		
		manaHandler.startUpdater();
		
		isEnabled = true;
		return true;
	}

	public boolean onDisable()
	{
		isEnabled = false;
		return true;
	}
	
	public void reloadConfigs() 
	{
		reloadConfig();
		tutData.reloadConfig();
		sih.reloadList();
	}
	public void reloadConfig() 
	{
	    if (configFile == null) 
	    {
	    	configFile = new File(dir, "config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defOrbiaConf.yml");
	    if (defConfigStream != null) 
	    {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
		plugin.getLogger().info(logTit+"Config Loaded.");
	}
	public FileConfiguration getConfig() 
	{
	    if (config == null) 
	    {
	        reloadConfig();
	    }
	    return config;
	}
	public void saveConfig() 
	{
	    if (config == null || configFile == null) 
	    {
	    	return;
	    }
	    try 
	    {
	        config.save(configFile);
	        plugin.getLogger().info(logTit+"Config saved");
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
	
	public String getLogTit() 
	{
		return logTit;
	}
	public String getDir() 
	{
		return dir;
	}
	public Logger getLogger() 
	{
		return logger;
	}
	
	public boolean isEnabled() 
	{
		return isEnabled;
	}
}
