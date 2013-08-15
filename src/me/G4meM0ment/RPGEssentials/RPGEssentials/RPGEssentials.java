package me.G4meM0ment.RPGEssentials.RPGEssentials;

import me.G4meM0ment.ReNature.ReNature;

import org.bukkit.plugin.java.JavaPlugin;

public class RPGEssentials extends JavaPlugin{

	private ReNature reNature;
	
	@Override
	public void onEnable() {
		//Initilize messages
		reNature = new ReNature(this);
		
		getLogger().info("");
	}
	
	@Override
	public void onDisable() {
		//Disable messages
	}
}
