package me.G4meM0ment.Orbia.Handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.Herochat;

import me.G4meM0ment.Orbia.Tutorial.Stage;
import me.G4meM0ment.Orbia.Tutorial.TutorialHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

public class CommandHandler {
	
	private RPGEssentials plugin;
	private TutorialHandler tutHandler;
	
	public CommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		tutHandler = new TutorialHandler();
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) 
	{
		if(sender instanceof Player && command.getName().equalsIgnoreCase("l")) {
			plugin.getHerochat();
			if(plugin.getHerochat() == null) return false;
			Player p = (Player) sender;
			Channel c = Herochat.getChannelManager().getChannel("Lokal");
			if(args.length == 0) {
				if(Herochat.getChatterManager().getChatter(p).getActiveChannel() == c) 
				{
					p.sendMessage(ChatColor.YELLOW+"Du redest bereits im Lokalen Kanal");
					return true;
				}
				Herochat.getChatterManager().getChatter(p).addChannel(c, false, false);
				Herochat.getChatterManager().getChatter((Player)sender).setActiveChannel(c , false, false);
				p.sendMessage(ChatColor.YELLOW+"Du redest jetzt im Lokalen Kanal");
				return true;
			} else 
			{
				p.chat("/&8l "+getMessage(args));
				return true;
			}
			
		}
		
		if(sender instanceof Player && command.getName().equalsIgnoreCase("g")) {
			plugin.getHerochat();
			if(plugin.getHerochat() == null) return false;
			Player p = (Player) sender;
			Channel c = Herochat.getChannelManager().getChannel("Global");
			if(args.length == 0) {
				if(Herochat.getChatterManager().getChatter(p).getActiveChannel() == c) 
				{
					p.sendMessage(ChatColor.YELLOW+"Du redest bereits im Globalen Kanal");
					return true;
				}
				Herochat.getChatterManager().getChatter(p).addChannel(c, false, false);
				Herochat.getChatterManager().getChatter(p).setActiveChannel(c , false, false);
				p.sendMessage(ChatColor.YELLOW+"Du redest jetzt im Globalen Kanal");
				return true;
			} else 
			{
				p.chat("/&2g "+getMessage(args));
				return true;
			}
		}
		
		if(args.length == 3 && args[0].equalsIgnoreCase("setStage"))
		{
			tutHandler.setStage(getPlayer(args[1]), Stage.valueOf(args[2]));
			return true;
		}
		return false;
	}
	
	private String getMessage(String[] args) {
		String msg = "";
		for(String s : args)
		{
			msg += s+" ";
		}
		return msg;
	}
	
    public Player getPlayer(final String name) {
        Player[] players = Bukkit.getOnlinePlayers();
 
        Player found = null;
        String lowerName = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : players) {
            if (player.getName().toLowerCase().startsWith(lowerName)) {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                    break;
                }
                if (curDelta == 0) break;
            }

        }
        return found;
    }
}
