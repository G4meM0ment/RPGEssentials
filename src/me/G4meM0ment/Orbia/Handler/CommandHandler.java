package me.G4meM0ment.Orbia.Handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.Herochat;
import com.herocraftonline.heroes.characters.Hero;

import me.G4meM0ment.Orbia.Handler.Duell.DuellHandler;
import me.G4meM0ment.Orbia.Handler.Duell.DuellState;
import me.G4meM0ment.Orbia.Tutorial.Stage;
import me.G4meM0ment.Orbia.Tutorial.TutorialHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

public class CommandHandler {
	
	private RPGEssentials plugin;
	private TutorialHandler tutHandler;
	private CMHandler cmh;
	private DuellHandler dh;
	
	public CommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		tutHandler = new TutorialHandler();
		cmh = new CMHandler();
		dh = new DuellHandler(plugin);
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) 
	{
		if(sender instanceof Player && command.getName().equalsIgnoreCase("l")) 
		{
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
		
		if(sender instanceof Player && command.getName().equalsIgnoreCase("g")) 
		{
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
		
		if(sender instanceof Player && command.getName().equalsIgnoreCase("c")) 
		{
			Player p = (Player) sender;
			if(args.length > 0)
				if(args[0].equalsIgnoreCase("?"))
				{
					if(plugin.getHeroes().getCharacterManager().getHero(p).isInCombat())
						p.sendMessage(ChatColor.GRAY+"Du befindest dich im Kampf");
					else
						p.sendMessage(ChatColor.GRAY+"Du befindest dich NICHT im Kampf");
					if(cmh.isInCombatMode(p))
						p.sendMessage(ChatColor.GRAY+"Dein Kampfmodus ist eingeschalten");
					else
						p.sendMessage(ChatColor.GRAY+"Dein Kampfmodus ist ausgeschalten");
					return true;
				}
			cmh.toggleCombatMode(p);
			if(cmh.isInCombatMode(p))
				p.sendMessage(ChatColor.GRAY+"Du bist jetzt im Kampfmodus!");
			else
				p.sendMessage(ChatColor.GRAY+"Du hast den Kampfmodus verlassen!");
			//TODO add messenger
			return true;
		}
		
		if(sender instanceof Player && command.getName().equalsIgnoreCase("a")) 
		{
			plugin.getHerochat();
			if(plugin.getHerochat() == null) return false;
			Player p = (Player) sender;
			if(!p.hasPermission("herochat.join.Administratoren")) return false;
			Channel c = Herochat.getChannelManager().getChannel("Administratoren");
			if(args.length == 0) 
			{
				if(Herochat.getChatterManager().getChatter(p).getActiveChannel() == c) 
				{
					p.sendMessage(ChatColor.YELLOW+"Du redest bereits im Administratoren Kanal");
					return true;
				}
				Herochat.getChatterManager().getChatter(p).addChannel(c, false, false);
				Herochat.getChatterManager().getChatter(p).setActiveChannel(c , false, false);
				p.sendMessage(ChatColor.YELLOW+"Du redest jetzt im Administratoren Kanal");
				return true;
			} else 
			{
				p.chat("/&4a "+getMessage(args));
				return true;
			}
		}
		
		if(sender instanceof Player && command.getName().equalsIgnoreCase("duell") && args.length > 0)
		{
			Player p = (Player) sender;
			Player r = null;
			Hero pH = plugin.getHeroes().getCharacterManager().getHero(p);
			Hero rH = null;
			
			if(args[0].equalsIgnoreCase("accept") && args.length >= 2)
			{
				if(pH.getParty() != null && pH.getParty().getLeader() != pH)
				{
					p.sendMessage(ChatColor.GRAY+"Du bist nicht der Gruppenleiter!");
					return true;
				}
				
				r = Bukkit.getPlayer(args[1]);
				if(r == null)
				{
					//TODO add messenger
					p.sendMessage(ChatColor.GRAY+"Spieler nicht gefunden!");
					return true;
				}
				rH = plugin.getHeroes().getCharacterManager().getHero(r);
				if(rH.getParty() != null && rH.getParty().getLeader() != rH)
				{
					p.sendMessage(ChatColor.GRAY+"Spieler ist nicht Gruppenleiter!");
					return true;
				}
				
				if(!dh.isInDuell(p, true, false) && !dh.isInDuell(r, true, false) && dh.getDuellState(p) == DuellState.REQUEST && dh.getDuellState(r) == DuellState.REQUEST)
				{
					dh.initDuell(p.getName(), true);
					p.sendMessage(ChatColor.DARK_RED+"Duell gestartet mit "+r.getName());
					r.sendMessage(ChatColor.DARK_RED+"Duell gestartet mit "+p.getName());
					return true;
				}
			}
			else
			{
				if(pH.getParty() != null && pH.getParty().getLeader() != pH)
				{
					p.sendMessage(ChatColor.GRAY+"Du bist nicht der Gruppenleiter!");
					return true;
				}
				
				r = Bukkit.getPlayer(args[0]);
				if(r == null)
				{
					//TODO add messenger
					p.sendMessage(ChatColor.GRAY+"Spieler nicht gefunden!");
					return true;
				}
				rH = plugin.getHeroes().getCharacterManager().getHero(r);
				if(rH.getParty() != null && rH.getParty().getLeader() != rH)
				{
					p.sendMessage(ChatColor.GRAY+"Spieler ist nicht Gruppenleiter!");
					return true;
				}
				
				if(!dh.isInDuell(p, true, false) && !dh.isInDuell(r, true, false))
				{
					dh.initDuellRequest(p, r, true);
					p.sendMessage(ChatColor.GRAY+"Anfrage versendet");
					r.sendMessage(ChatColor.GRAY+"Duell Anfrage von "+p.getName()+" Tippe /duell accept "+p.getName()+" um zu aktzeptieren");
					return true;
				}
			}
		}
		
		if(args.length == 3 && args[0].equalsIgnoreCase("setStage") && command.getName().equalsIgnoreCase("orbia") && sender.hasPermission("orbia.admin"))
		{
			tutHandler.setStage(getPlayer(args[1]), Stage.valueOf(args[2]));
			return true;
		}	
		
		if(command.getName().equalsIgnoreCase("job") && sender instanceof Player)
		{
			Player p = (Player) sender;
			
			p.sendMessage(ChatColor.AQUA+"oOo---------------| Beruf |---------------oOo");
			if(p.hasPermission("orbia.job.miner"))
			{
				p.sendMessage(ChatColor.AQUA+"Du hast den Beruf des Minenarbeiters:");
				p.sendMessage(ChatColor.AQUA+"Du kannst Diamant und Eisenspitzhacken verwenden");
			}
			else if(p.hasPermission("orbia.job.alchemist"))
			{
				p.sendMessage(ChatColor.AQUA+"Du hast den Beruf des Alchemisten");
				p.sendMessage(ChatColor.AQUA+"Du kannst den Braustand verwenden und Tränke brauen");
			}
			else if(p.hasPermission("orbia.job.farmer"))
			{
				p.sendMessage(ChatColor.AQUA+"Du hast den Beruf des Bauern:");
				p.sendMessage(ChatColor.AQUA+"Du kannst Tiere vermehren, melken, Samen sähen und die Harken verwenden");
			}
			else
			{
				p.sendMessage(ChatColor.RED+"Du hast keinen Beruf!");
				return true;
			}
			return true;
		}
		
		if((command.getName().equalsIgnoreCase("help") || command.getName().equalsIgnoreCase("hilfe")) && sender instanceof Player)
		{
			Player p = (Player) sender;
			p.sendMessage(ChatColor.DARK_PURPLE+"oOo---------------| Hilfe |---------------oOo");
			p.sendMessage(ChatColor.DARK_PURPLE+"Kommandos:");
			p.sendMessage(ChatColor.GREEN+"/g, /l, /p "+ChatColor.DARK_PURPLE+"Wechsel in den globalen, lokalen oder Gruppenchat");
			p.sendMessage(ChatColor.GREEN+"/c "+ChatColor.DARK_PURPLE+"Schalte den Kampfmodus ein/aus, du wirst nicht mehr ausversehen Gegenstände fallen lassen");
			p.sendMessage(ChatColor.GREEN+"/hero help "+ChatColor.DARK_PURPLE+"Zeigt dir die Kommandos für deine Klasse & Gruppen");
			p.sendMessage(ChatColor.GREEN+"/hero tools "+ChatColor.DARK_PURPLE+"Gegenstände die du verwenden kannst");
			p.sendMessage(ChatColor.GREEN+"/hero armor "+ChatColor.DARK_PURPLE+"Rüstungen die du tragen kannst");
			p.sendMessage(ChatColor.GREEN+"/lvl "+ChatColor.DARK_PURPLE+"Deine EP und Level");
			p.sendMessage(ChatColor.GREEN+"/skills "+ChatColor.DARK_PURPLE+"Deine Fähigkeiten");
			p.sendMessage(ChatColor.GREEN+"/cprivate "+ChatColor.DARK_PURPLE+"Eine Kiste/Tür abschließen");
			p.sendMessage(ChatColor.GREEN+"/cpublic "+ChatColor.DARK_PURPLE+"Kiste/Tür für alle Zugänglich machen");
			p.sendMessage(ChatColor.GREEN+"/cpassword "+ChatColor.DARK_PURPLE+"Kiste/Tür mit Passwort abschließen");
			p.sendMessage(ChatColor.GREEN+"/cremove "+ChatColor.DARK_PURPLE+"Ein Schloss entfernen");
			p.sendMessage(ChatColor.GREEN+"/mount "+ChatColor.DARK_PURPLE+"Du steigst auf dein Pferd (wenn du eines hast)");
			p.sendMessage(ChatColor.GREEN+"/party invite <Spieler> "+ChatColor.DARK_PURPLE+"Lädt einen Spieler in deine Gruppe ein");
			p.sendMessage(ChatColor.GREEN+"/duell <Spieler> "+ChatColor.DARK_PURPLE+"Lädt einen Spieler zu einem Duell ein");
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
	
    public Player getPlayer(final String name) 
    {
        Player[] players = Bukkit.getOnlinePlayers();
 
        Player found = null;
        String lowerName = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : players) 
        {
            if (player.getName().toLowerCase().startsWith(lowerName)) 
            {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) 
                {
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
