package me.G4meM0ment.RPGEssentials.RPGEssentials;

import me.G4meM0ment.ReNature.ReNature;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGEssentials extends JavaPlugin{

	private ReNature reNature;
	
	private String dir				=	"plugins/RPGEssentials";
	
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
		if(reNature.onEnable() && getConfig().getBoolean("ReNatureEnabled"))
			getLogger().info("ReNature enabled!");
		else if (reNature.onEnable())
			getLogger().info("ReNature found, but disabled!");
		else
			getLogger().info("ReNature couldn't be enabled!");
		
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
		
		if(reNature.onDisable()) {
			getLogger().info("ReNature disabled!");
		}
	}
	
	public String getDir() {
		return dir;
	}
}
