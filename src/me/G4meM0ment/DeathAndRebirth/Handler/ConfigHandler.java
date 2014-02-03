package me.G4meM0ment.DeathAndRebirth.Handler;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;

import org.bukkit.configuration.file.FileConfiguration;
import org.getspout.spoutapi.gui.Color;

public class ConfigHandler {
	
	private DeathAndRebirth subplugin;
	
	/*
	 * 
	 * Settings
	 * 
	 */
	public static boolean dropItems = false;
	public static boolean corpseSpawn = false;
	public static long timeUntilCanRes = 20000; //20 seconds in milliseconds
	
	public static int maxGraveDistance = 3;
	public static boolean graveSign = true;
	public static String signText = "R.I.P";
	public static long autoSignRemove = 86400000; //24h in milliseconds
	
	public static boolean blockGhostInteractions = true;
	public static List<String> blockedCommands = new ArrayList<String>();
	public static boolean compass = true;
	public static boolean destroyBlocks = true;
	public static boolean ghostChat = true;
	public static boolean crossWorldGhost = true;
	
	public static int shrineRadius = 6;
	public static boolean protectShrines = false;
	
	public static boolean useSpoutcraft = false;
	public static String deathSound = "";
	public static String rebirthSound = "";
	public static String resurrectSound = "";
	public static String ghostSkin = "";
	public static String ghostResourcepack = "";
	public static boolean playBackgroundSounds = true;
	public static String skyColor = "";
	public static String fogColor = "";
	public static String cloudColor = "";
	
	public static List<String> enabledWorlds = new ArrayList<String>();
	
	/*
	 * 
	 * Settings paths
	 * 
	 */
	private static String dropItemsPath = "dropItems";
	private static String corpseSpawnPath = "corpseSpawn";
	private static String timeUntilCanResPath = "timeUntilCanRes";
	
	private static String maxGraveDistancePath = "maxGraveDistance";
	private static String graveSignPath = "graveSign";
	private static String signTextPath = "signText";
	private static String autoSignRemovePath = "autoSignRemove";
	
	private static String blockGhostInteractionsPath = "blockGhostInteractions";
	private static String blockedCommandsPath = "disabledCommands";
	private static String compassPath = "compass";
	private static String destroyBlocksPath = "destroyBlocks";
	private static String ghostChatPath = "ghostChat";
	private static String crossWorldGhostPath = "crossWorldGhost";
	
	private static String shrineRadiusPath = "shrineRadius";
	private static String protectShrinesPath = "protectShrines";
	
	private static String useSpoutcraftPath = "useSpoutcraft";
	private static String deathSoundPath = "deathSound";
	private static String rebirthSoundPath = "rebirthSound";
	private static String resurrectSoundPath = "resurrectSound";
	private static String ghostSkinPath = "ghostSkin";
	private static String ghostResourcepackPath = "ghostResourcepack";
	private static String playBackgroundSoundsPath = "playBackgroundSounds";
	private static String skyColorPath = "skyColor";
	private static String fogColorPath = "fogColor";
	private static String cloudColorPath = "cloudColor";
	
	private static String enabledWorldsPath = "worlds";
	
	public ConfigHandler()
	{
		subplugin = new DeathAndRebirth();
	}
	
	/**
	 * load all settings from the config to cache unsaved changes are gone
	 */
	public void loadSettings()
	{
		FileConfiguration config = subplugin.getConfig();
		
		dropItems = config.getBoolean(dropItemsPath);
		corpseSpawn = config.getBoolean(corpseSpawnPath);
		timeUntilCanRes = config.getLong(timeUntilCanResPath);
		
		maxGraveDistance = config.getInt(maxGraveDistancePath);
		graveSign = config.getBoolean(graveSignPath);
		signText = config.getString(signTextPath);
		autoSignRemove = config.getLong(autoSignRemovePath);
		
		blockGhostInteractions = config.getBoolean(blockGhostInteractionsPath);
		blockedCommands = config.getStringList(blockedCommandsPath);
		compass = config.getBoolean(compassPath);
		destroyBlocks = config.getBoolean(destroyBlocksPath);
		ghostChat = config.getBoolean(ghostChatPath);
		crossWorldGhost = config.getBoolean(crossWorldGhostPath);
		
		shrineRadius = config.getInt(shrineRadiusPath);
		protectShrines = config.getBoolean(protectShrinesPath);
		
		useSpoutcraft = config.getBoolean(useSpoutcraftPath);
		deathSound = config.getString(deathSoundPath);
		rebirthSound = config.getString(rebirthSoundPath);
		resurrectSound = config.getString(resurrectSoundPath);
		ghostSkin = config.getString(ghostSkinPath);
		ghostResourcepack = config.getString(ghostResourcepackPath);
		playBackgroundSounds = config.getBoolean(playBackgroundSoundsPath);
		skyColor = config.getString(skyColorPath);
		fogColor = config.getString(fogColorPath);
		cloudColor = config.getString(cloudColorPath);
		
		enabledWorlds = config.getStringList(enabledWorldsPath);
	}
	
	/**
	 * save all settings from cache to file, changes made in flat file are gone
	 */
	public void saveSettings()
	{
		FileConfiguration config = subplugin.getConfig();
		
		config.set(dropItemsPath, dropItems);
		config.set(corpseSpawnPath, corpseSpawn);
		config.set(timeUntilCanResPath, timeUntilCanRes);
		
		config.set(maxGraveDistancePath, maxGraveDistance);
		config.set(graveSignPath, graveSign);
		config.set(signTextPath, signText);
		config.set(autoSignRemovePath, autoSignRemove);
		
		config.set(blockGhostInteractionsPath, blockGhostInteractions);
		config.set(blockedCommandsPath, blockedCommands);
		config.set(compassPath, compass);
		config.set(destroyBlocksPath, destroyBlocks);
		config.set(ghostChatPath, ghostChat);
		config.set(crossWorldGhostPath, crossWorldGhost);
		
		config.set(shrineRadiusPath, shrineRadius);
		config.set(protectShrinesPath, protectShrines);
		
		config.set(useSpoutcraftPath, useSpoutcraft);
		config.set(deathSoundPath, deathSound);
		config.set(rebirthSoundPath, rebirthSound);
		config.set(resurrectSoundPath, resurrectSound);
		config.set(ghostSkinPath, ghostSkin);
		config.set(ghostResourcepackPath, ghostResourcepack);
		config.set(playBackgroundSoundsPath, playBackgroundSounds);
		config.set(skyColorPath, skyColor);
		config.set(fogColorPath, fogColor);
		config.set(cloudColorPath, cloudColor);
		
		config.set(enabledWorldsPath, enabledWorlds);
	}
	
	/**
	 * checks if given world has DaR enabled
	 * @param world
	 * @return
	 */
	public static boolean isDAREnabled(String world)
	{
		return enabledWorlds.contains(world);
	}
	
	/**
	 * Returns the color value of the given String
	 * @param colorString
	 * @return
	 */
	public static Color getFloatColor(String colorString) 
	{ 
		float [] array = new float[3];
		String [] strings = colorString.split(";");
		
		for(int i=0; i<strings.length; i++)
			array[i] = Float.valueOf(strings[i]);
		
		return new Color(array[0], array[1], array[2]);
	}
}
