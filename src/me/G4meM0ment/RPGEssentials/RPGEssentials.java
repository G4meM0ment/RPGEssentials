package me.G4meM0ment.RPGEssentials;

import me.G4meM0ment.Ambience.Ambience;
import me.G4meM0ment.Junkie.Junkie;
import me.G4meM0ment.Orbia.Orbia;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.ReNature.ReNature;
import me.G4meM0ment.Rentables.Rentables;
import me.G4meM0ment.UnamedPortalPlugin.UnnamedPortalPlugin;
import net.dandielo.citizens.traders_v3.bukkit.DtlTraders;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.dthielke.herochat.Herochat;
import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.herocraftonline.heroes.Heroes;
import com.massivecraft.factions.Factions;
import com.massivecraft.mcore.MCore;
import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RPGEssentials extends JavaPlugin{

	private ReNature reNature;
	private Junkie junkie;
	private Orbia orbia;
	private RPGItem rpgItem;
	private UnnamedPortalPlugin upp;
	private Rentables rent;
	private Ambience ambience;
	
	private CommandHandler ch;
	
	private Economy econ;
	private WorldGuardPlugin wg;
	private WorldEditPlugin we;
	private MCore mcore;
	private Factions factions;
	private Towny towny;
	private Heroes heroes;
	private Herochat herochat;
	@SuppressWarnings("unused")
	private LWCPlugin lwcp;
	private LWC lwc;
	private DtlTraders dtlTraders;
	
	private static String dir = "plugins/RPGEssentials";
	
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

//################ Init APIs ###################
	    if (!setupEconomy() ) {
	        getLogger().info("Disabled due to no Vault dependency found!");
	        getServer().getPluginManager().disablePlugin(this);
	        return;
	    }
		we = initWorldEdit();
		wg = initWorldGuard();
		mcore = initMCore();
		factions = initFactions();
		towny = initTowny();
		heroes = initHeroes();
		lwc = initLWC();
		dtlTraders = initDtlTraders();
		herochat = initHerochat();
		
		//Initialize messages
		getLogger().info("Initializing sub-plugins:");
		
//################ Initializing RPGItem and debugging ################
		if(getConfig().getBoolean("RPGItemEnabled")) {
			rpgItem = new RPGItem(this);
			boolean rpgItemEnabled;
			rpgItemEnabled = rpgItem.onEnable();

			if(rpgItemEnabled)
				getLogger().info("RPGItem enabled!");
			else
				getLogger().info("RPGItem couldn't be enabled!");
		}
		else
			getLogger().info("RPGItem found, but disabled in config!");
		
//################ Initializing ReNature and debugging ################
		if(getConfig().getBoolean("ReNatureEnabled")) {
			reNature = new ReNature(this);
			boolean reNatureEnabled = reNature.onEnable();

			if(reNatureEnabled)
				getLogger().info("ReNature enabled!");
			else
				getLogger().info("ReNature couldn't be enabled!");
		} else
			getLogger().info("ReNature found, but disabled in config!");
		
//################ Initializing Junkie and debugging ################
		if(getConfig().getBoolean("JunkieEnabled")) {
			junkie = new Junkie(this);
			boolean junkieEnabled = junkie.onEnable();
		
			if(junkieEnabled)
				getLogger().info("Junkie enabled!");
			else
				getLogger().info("Junkie couldn't be enabled!");
		} else
			getLogger().info("Junkie found, but disabled in config!");
		
//################ Initializing UnnamedPortalPlugin and debugging ################
		if(getConfig().getBoolean("UnnamedPortalPluginEnabled")) {
			upp = new UnnamedPortalPlugin(this);
			boolean uppEnabled = upp.onEnable();
			
			if(uppEnabled)
				getLogger().info("UnnamedPortalPlugin enabled!");
			else
				getLogger().info("UnnamedPortalPlugin couldn't be enabled!");
		} else
			getLogger().info("UnnamedPortalPlugin found, but disabled in config!");
		
//################ Initializing Rentables and debugging ################
		if(getConfig().getBoolean("RentablesEnabled")) {
			rent = new Rentables(this);
			boolean rentEnabled = rent.onEnable();
			
			if(rentEnabled)
				getLogger().info("Rentables enabled!");
			else
				getLogger().info("Rentables couldn't be enabled!");
		} else
			getLogger().info("Rentables found, but disabled in config!");

//################ Initializing Ambience and debugging ################
		if(getConfig().getBoolean("AmbienceEnabled")) {
			ambience = new Ambience(this);
			boolean rentEnabled = ambience.onEnable();
			
			if(rentEnabled)
				getLogger().info("Ambience enabled!");
			else
				getLogger().info("Ambience couldn't be enabled!");
		} else
			getLogger().info("Ambience found, but disabled in config!");
		
//################ Initializing Orbia and debugging ################
		if(getConfig().getKeys(false).contains("OrbiaEnabled") && getConfig().getBoolean("OrbiaEnabled")) {
			orbia = new Orbia(this);
			boolean orbiaEnabled = orbia.onEnable();
			
			if(orbiaEnabled)
				getLogger().info("Orbia enabled!");
			else
				getLogger().info("Orbia couldn't be enabled!");
		} else
			getLogger().info("Orbia found, but disabled in config!");
		
		//Finished initializing plugin enabled
		getLogger().info("Initialization done!");
		
		//Plguin enabled
		getLogger().info("Enabled version "+pdf.getVersion());
	}

	@Override
	public void onDisable() {
		//Disable messages
		getServer().getScheduler().cancelTasks(this);
		getLogger().info("Config saved");
		
		//Disable sub-plugins
		getLogger().info("Disabling sub-plugins:");
		
		if(rpgItem != null)
			if(rpgItem.onDisable() && rpgItem.isEnabled())
				getLogger().info("RPGItem disabled!");
		
		if(reNature != null)
			if(reNature.isEnabled() && reNature.onDisable())
				getLogger().info("ReNature disabled!");
		
		if(junkie != null)
			if(junkie.onDisable() && junkie.isEnabled())
				getLogger().info("Junkie disabled!");
		
		if(upp != null)
			if(upp.onDisable() && upp.isEnabled())
				getLogger().info("UnnamedPortalPlugin disabled!");
		
		if(rent != null)
			if(rent.onDisable() && rent.isEnabled())
				getLogger().info("Rentables disabled!");
		
		if(orbia != null)
			if(orbia.onDisable() && orbia.isEnabled())
				getLogger().info("Orbia disabled!");
	}
	
	public String getDir() {
		return dir;
	}
	
    private boolean setupEconomy() {
    	if(getServer().getPluginManager().getPlugin("Vault") == null) {
    		this.getLogger().info("Vault not found");
    		return false;
    	}
    	RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    	if (rsp == null) {
    		getLogger().info("No economy plugin found!");
    		return true;
    	}
    	econ = rsp.getProvider();
    	return econ != null;
    }
    public Economy getEconomy() {
    	return econ;
    }
	
	private WorldEditPlugin initWorldEdit() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
	 
	    // WorldEdit may not be loaded
	    if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("WorldEdit found enabled features");
	    return (WorldEditPlugin) plugin;
	}
	public WorldEditPlugin getWorldEdit() {
		return we;
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
	
	private LWC initLWC() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("LWC");
	 
	    if (plugin == null || !(plugin instanceof LWCPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("LWC found enabled features");
	    return ((LWCPlugin) plugin).getLWC();
	}
	public LWC getLWC() {
		return lwc;
	}
	
	private DtlTraders initDtlTraders() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("dtlTraders");
	 
	    if (plugin == null || !(plugin instanceof DtlTraders)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("dtlTraders found enabled features");
	    return (DtlTraders) plugin;
	}
	public DtlTraders getDtlTraders() {
		return dtlTraders;
	}
	
	private Herochat initHerochat() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("Herochat");
	 
	    if (plugin == null || !(plugin instanceof Herochat)) {
	        return null; // Maybe you want throw an exception instead
	    }
		getLogger().info("Herochat found enabled features");
	    return (Herochat) plugin;
	}
	public Herochat getHerochat() {
		return herochat;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(ch.onCommand(sender, command, label, args))
			return true;
		else
			return false;
	}
	
	public void reloadRPGEssentials() {
		reloadConfig();
		if(getConfig().getBoolean("ReNatureEnabled") && reNature != null && reNature.isEnabled())
			reNature.reloadConfig();
		if(getConfig().getBoolean("JunkieEnabled") && junkie != null && junkie.isEnabled())
			junkie.reloadConfig();
		if(getConfig().getBoolean("OrbiaEnabled") && orbia != null && orbia.isEnabled())
			orbia.reloadConfigs();
		if(getConfig().getBoolean("RPGItemEnabled") && rpgItem != null && rpgItem.isEnabled())
			rpgItem.reloadConfigs();
		if(getConfig().getBoolean("RentablesEnabled") && rent != null && rent.isEnabled())
			rent.reloadConfigs();
		if(getConfig().getBoolean("UnnamedPortalPluginEnabled") && upp != null && upp.isEnabled())
			upp.reloadConfigs();
	}
	
	public RPGItem getRPGItem() {
		return rpgItem;
	}

	public UnnamedPortalPlugin getUnnamedPortalPlugin() {
		return upp;
	}
 }
