package me.G4meM0ment.Orbia.Commands.Chat;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.Chatter;
import com.dthielke.herochat.Herochat;

@CommandInfo(
		name = "a",
		pattern = "a",
		usage = "/a (<message>)",
		desc = "Sendet eine Nachricht in den Admin Chat oder setzt ihn als aktiven Kanal",
		permission = "herochat.join.Administratoren"
	)
public class AdminChatCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "Du bist kein Spieler!");
			return false;
		}
		
		plugin.getHerochat();
		if(plugin.getHerochat() == null) return false;
		Player p = (Player) sender;
		Channel c = Herochat.getChannelManager().getChannel("Administratoren");
		Chatter chatter = Herochat.getChatterManager().getChatter(p);
		
		if(args.length == 0) {
			if(chatter.getActiveChannel() == c)
				p.sendMessage(ChatColor.YELLOW+"Du redest bereits im Administratoren Kanal");
			else {
				chatter.addChannel(c, false, false);
				chatter.setActiveChannel(c , false, false);
				p.sendMessage(ChatColor.YELLOW+"Du redest jetzt im Administratoren Kanal");
			}
		} else
			p.chat("/&4a "+getMessage(args));
		return true;
	}
	
	private String getMessage(String[] args) {
		String msg = "";
		for(String s : args)
			msg += s+" ";
		return msg;
	}
}
