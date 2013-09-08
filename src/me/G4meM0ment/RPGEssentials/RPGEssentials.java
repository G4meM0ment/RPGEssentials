package me.G4meM0ment.RPGEssentials;

import me.G4meM0ment.Junkie.Junkie;
import me.G4meM0ment.Orbia.Orbia;
import me.G4meM0ment.RPGEssentials.Schedule.Schedule;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.ReNature.ReNature;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.heroes.Heroes;
import com.massivecraft.factions.Factions;
import com.massivecraft.mcore.MCore;
import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RPGEssentials extends JavaPlugin{

	private ReNature reNature;
	private Junkie junkie;
	private Orbia orbia;
	private RPGItem rpgItem;
	private Schedule schedule;
	
	private CommandHandler ch;
	
	private WorldGuardPlugin wg;
	private MCore mcore;
	private Factions factions;
	private Towny towny;
	private Heroes heroes;
	
	private String dir = "plugins/RPGEssentials";
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = getDescription();
		ch = new CommandHandler(this);
		
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
		
//################ Initializing RPGItem and debugging ################
		rpgItem = new RPGItem(this);
		boolean rpgItemEnabled = rpgItem.onEnable();

		if(rpgItemEnabled && getConfig().getBoolean("RPGItemEnabled"))
			getLogger().info("RPGItem enabled!");
		else if (rpgItemEnabled)
			getLogger().info("RPGItem found, but disabled in config!");
		else
			getLogger().info("RPGItem couldn't be enabled!");
		
//################ Initializing ReNature and debugging ################
		reNature = new ReNature(this);
		boolean reNatureEnabled = reNature.onEnable();

		if(reNatureEnabled && getConfig().getBoolean("ReNatureEnabled"))
			getLogger().info("ReNature enabled!");
		else if (reNatureEnabled)
			getLogger().info("ReNature found, but disabled in config!");
		else
			getLogger().info("ReNature couldn't be enabled!");
		
//################ Initializing Junkie and debugging ################
		junkie = new Junkie(this);
		boolean junkieEnabled = junkie.onEnable();
		if(junkieEnabled && getConfig().getBoolean("JunkieEnabled"))
			getLogger().info("Junkie enabled!");
		else if (junkieEnabled)
			getLogger().info("Junkie found, but disabled in config!");
		else
			getLogger().info("Junkie couldn't be enabled!");
		
//################ Initializing Orbia and debugging ################
		orbia = new Orbia(this);
		boolean orbiaEnabled = orbia.onEnable();
		if(orbiaEnabled && getConfig().getBoolean("OrbiaEnabled"))
			getLogger().info("Orbia enabled!");
		else if (orbiaEnabled)
			getLogger().info("Orbia found, but disabled in config!");
		else
			getLogger().info("Orbia couldn't be enabled!");
				
		//Finished initializing plugin enabled
		getLogger().info("Initialization done!");
		
//################ Init APIs ###################
		wg = initWorldGuard();
		mcore = initMCore();
		factions = initFactions();
		towny = initTowny();
		heroes = initHeroes();
		
//############### Startsing scheduler #################
		schedule = new Schedule(this);
		Thread time = new Thread(schedule);
		time.start();
		getLogger().info("Setup schedule");
		
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
		
		if(rpgItem.onDisable())
			getLogger().info("RPGItem disabled!");
		
		if(reNature.onDisable())
			getLogger().info("ReNature disabled!");
			
		if(junkie.onDisable())
			getLogger().info("Junkie disabled!");
		
		if(orbia.onDisable())
			getLogger().info("Orbia disabled!");
	}
	
	public String getDir() {
		return dir;
	}
	
	private WorldGuardPlugin initWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("WorldGuard found enabled features");
	    return (WorldGuardPlugin) plugin;
	}
	public WorldGuardPlugin getWorldGuard() {
		return wg;
	}
	
	private MCore initMCore() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("mcore");
	 
	    if (plugin == null || !(plugin instanceof MCore)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("MCore found enabled features");
	    return (MCore) plugin;
	}
	public MCore getMCore() {
		return mcore;
	}
	
	private Factions initFactions() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("Factions");
	 
	    if (plugin == null || !(plugin instanceof Factions)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("Factions found enabled features");
	    return (Factions) plugin;
	}
	public Factions getFactions() {
		return factions;
	}
	
	private Towny initTowny() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("Towny");
	 
	    if (plugin == null || !(plugin instanceof Towny)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("Towny found enabled features");
	    return (Towny) plugin;
	}
	public Towny getTowny() {
		return towny;
	}
	
	private Heroes initHeroes() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("Heroes");
	 
	    if (plugin == null || !(plugin instanceof Heroes)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("Heroes found enabled features");
	    return (Heroes) plugin;
	}
	public Heroes getHeroes() {
		return heroes;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(ch.onCommand(sender, command, label, args))
			return true;
		else
			return false;
	}
	
	public void reloadRPGEssentials() {
		reloadConfig();
		reNature.reloadConfig();
		junkie.reloadConfig();
		orbia.reloadConfig();
		rpgItem.reloadConfigs();
		
	}
	
	public RPGItem getRPGItem() {
		return rpgItem;
	}
 }
