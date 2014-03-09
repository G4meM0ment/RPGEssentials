package me.G4meM0ment.RPGEssentials.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.G4meM0ment.Chaintrain.Commands.ChaintrainCommand;
import me.G4meM0ment.DeathAndRebirth.Commands.DARCommand;
import me.G4meM0ment.DeathAndRebirth.Commands.User.GraveCommand;
import me.G4meM0ment.InventoryBackup.Commands.IBCommand;
import me.G4meM0ment.Karma.Commands.KarmaCommand;
import me.G4meM0ment.Orbia.Commands.HelpCommand;
import me.G4meM0ment.Orbia.Commands.OrbiaCommand;
import me.G4meM0ment.Orbia.Commands.Chat.AdminChatCommand;
import me.G4meM0ment.Orbia.Commands.Chat.GlobalChatCommand;
import me.G4meM0ment.Orbia.Commands.Chat.LokalChatCommand;
import me.G4meM0ment.Orbia.Commands.Combat.CombatCommand;
import me.G4meM0ment.Orbia.Commands.Duell.DuellCommand;
import me.G4meM0ment.Orbia.Commands.Job.JobCommand;
import me.G4meM0ment.RPGEssentials.PermHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.RPGItem.Commands.RPGItemCommand;
import me.G4meM0ment.Rentables.Commands.RentablesCommand;
import me.G4meM0ment.UnamedPortalPlugin.Commands.UPPCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
	
	private RPGEssentials plugin;
    
    private Map<String,Command> commands;
    
    public CommandHandler(RPGEssentials plugin) {
        this.plugin = plugin;
        
        registerCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command bcmd, String label, String[] args) {
        // Grab the base and arguments.
    	String base = bcmd.getName();
        //String base = (args.length > 0 ? args[0] : "");
        String last = (args.length > 0 ? args[args.length - 1] : "");
        
        // If there's no base argument, show a helpful message.
        if (base.equals("")) {
            Messenger.sendMessage(sender, "/rpge help|?");
            return true;
        }
        
        // The help command is a little special
        if (base.equals("?") || base.equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }
        
        // Get all commands that match the base.
        List<Command> matches = getMatchingCommands(base);
        
        // If there's more than one match, display them.
        if (matches.size() > 1) {
            Messenger.sendMessage(sender, "Multiple command matches");
            for (Command cmd : matches) {
                showUsage(cmd, sender, false);
            }
            return true;
        }
        
        // If there are no matches at all, notify.
        if (matches.size() == 0) {
          	Messenger.sendMessage(sender, "Command not found");
            return true;
        }
        
        // Grab the only match.
        Command command = matches.get(0);
        CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
        
        // First check if the sender has permission.
        if (!PermHandler.hasPerm(sender, info.permission())) {
        	Messenger.sendMessage(sender, "No Permission");
            return true;
        }
        
        // Check if the last argument is a ?, in which case, display usage and description
        if (last.equals("?") || last.equals("help")) {
            showUsage(command, sender, true);
            return true;
        }
        
        // Otherwise, execute the command!
        String[] params = args;//trimFirstArg(args);
        if (!command.execute(plugin, sender, params)) {
            showUsage(command, sender, true);
        }
        return true;
    }
    
    /**
	* Get all commands that match a given string.
	* @param arg the given string
	* @return a list of commands whose patterns match the given string
	*/
    private List<Command> getMatchingCommands(String arg) {
        List<Command> result = new ArrayList<Command>();
        
        // Grab the commands that match the argument.
        for (Entry<String,Command> entry : commands.entrySet()) {
            if (arg.equalsIgnoreCase(entry.getKey())) {
                result.add(entry.getValue());
            }
        }        
        return result;
    }
    
    /**
	* Show the usage and description messages of a command to a player.
	* The usage will only be shown, if the player has permission for the command.
	* @param cmd a Command
	* @param sender a CommandSender
	*/
    private void showUsage(Command cmd, CommandSender sender, boolean prefix) {
        CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
        if(!PermHandler.hasPerm(sender, info.permission())) return;

        sender.sendMessage((prefix ? "Usage: " : "") + info.usage() + " " + ChatColor.YELLOW + info.desc());
    }
    
    /**
	* Remove the first argument of a string. This is because the very first
	* element of the arguments array will be the command itself.
	* @param args an array of length n
	* @return the same array minus the first element, and thus of length n-1
	*/
    @SuppressWarnings("unused")
	private String[] trimFirstArg(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
    
    /**
	* List all the available MobArena commands for the CommandSender.
	* @param sender a player or the console
	*/
    private void showHelp(CommandSender sender) {
        StringBuilder user = new StringBuilder();
        StringBuilder admin = new StringBuilder();

        for (Command cmd : commands.values()) {
            CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
            if(!PermHandler.hasPerm(sender, info.permission())) continue;

            StringBuilder buffy;
            if (info.permission().contains("admin"))
                buffy = admin;
            else 
                buffy = user;
            
            buffy.append("\n")
                 .append(ChatColor.RESET).append(info.usage()).append(" ")
                 .append(ChatColor.YELLOW).append(info.desc());
        }

        if (admin.length() == 0) {
        	Messenger.sendMessage(sender, "Available Commands: "+user.toString());
        } else {
        	Messenger.sendMessage(sender, "User Commands: "+user.toString());
        	Messenger.sendMessage(sender, "Admin Commands: "+admin.toString());
        }
    }
    
    /**
	* Register all the commands directly.
	* This could also be done with a somewhat dirty classloader/resource reader
	* method, but this is neater, albeit more manual work.
	*/
    private void registerCommands() {
        commands = new LinkedHashMap<String,Command>();
        
        /*
         *  admin commands
         */
        //file util cmds
        
        //rpgitem cmds
        register(RPGItemCommand.class);
        try {
			RPGItemCommand.class.newInstance().registerCommands();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        
        //dar cmds
        register(DARCommand.class);
        try {
			DARCommand.class.newInstance().registerCommands();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        register(GraveCommand.class);
        
        //karma cmds
        register(KarmaCommand.class);
        try {
			KarmaCommand.class.newInstance().registerCommands();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        
        //ib cmds
        register(IBCommand.class);
        
        //rentables cmds
        register(RentablesCommand.class);
        
        //upp cmds
        register(UPPCommand.class);
        try {
			UPPCommand.class.newInstance().registerCommands();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        
        //chaintrain cmds
        register(ChaintrainCommand.class);
        try {
			ChaintrainCommand.class.newInstance().registerCommands();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        
        //orbia cmds
        register(LokalChatCommand.class);
        register(GlobalChatCommand.class);
        register(AdminChatCommand.class);
        register(CombatCommand.class);
        register(JobCommand.class);
        register(DuellCommand.class);
        register(OrbiaCommand.class);
        register(HelpCommand.class);
        
        /*        
         * user commands
         */
    }

	/**
	* Register a command.
	* The Command's CommandInfo annotation is queried to find its pattern
	* string, which is used to map the commands.
	* @param c a Command
	*/
    public void register(Class<? extends Command> c) {
        CommandInfo info = c.getAnnotation(CommandInfo.class);
        if (info == null) return;
        
        try {
            commands.put(info.pattern(), c.newInstance());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }	
}
