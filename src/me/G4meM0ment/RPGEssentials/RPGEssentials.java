package me.G4meM0ment.RPGEssentials;

import me.G4meM0ment.Junkie.Junkie;
import me.G4meM0ment.ReNature.ReNature;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RPGEssentials extends JavaPlugin{

	private ReNature reNature;
	private Junkie junkie;
	private WorldGuardPlugin wg;
	
	private String dir = "plugins/RPGEssentials";
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = getDescription();
		
		//Enabling config
		try {
			getConfig().options().copyDefaults(true);
			saveConfig();
			getLogger().info("Config loaded.");
		} catch(Exception e) {
			getLogger().info("Could not load config.");
		}
		
		//Initialize messages
		getLogger().info("Initializing sub-plugins:");
		
		//Initializing ReNature and debugging
		reNature = new ReNature(this);
		boolean reNatureEnabled = reNature.onEnable();

		if(reNatureEnabled && getConfig().getBoolean("ReNatureEnabled"))
			getLogger().info("ReNature enabled!");
		else if (reNatureEnabled)
			getLogger().info("ReNature found, but disabled in config!");
		else
			getLogger().info("ReNature couldn't be enabled!");
		
		//Initializing Junkie and debugging
		junkie = new Junkie(this);
		boolean junkieEnabled = junkie.onEnable();
		if(junkieEnabled && getConfig().getBoolean("JunkieEnabled"))
			getLogger().info("Junkie enabled!");
		else if (junkieEnabled)
			getLogger().info("Junkie found, but disabled in config!");
		else
			getLogger().info("Junkie couldn't be enabled!");
		
		//Finished initializing plugin enabled
		getLogger().info("Initialization done!");
		
		//Plguin enabled
		getLogger().info("Enabled version "+pdf.getVersion());
	}
	
	@Override
	public void onDisable() {
		//Disable messages
		getServer().getScheduler().cancelTasks(this);
		saveConfig();
		getLogger().info("Config saved");
		
		//Disable sub-plugins
		getLogger().info("Disabling sub-plugins:");
		
		if(reNature.onDisable())
			getLogger().info("ReNature disabled!");
			
		if(junkie.onDisable())
			getLogger().info("Junkie disabled!");
	}
	
	public String getDir() {
		return dir;
	}
	
	private WorldGuardPlugin initWorldGuard() {
	    Plugin wg = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (wg == null || !(wg instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) wg;
	}
	public WorldGuardPlugin getWorldGuard() {
		return wg;
	}
 }
