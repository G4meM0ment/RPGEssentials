package me.G4meM0ment.Orbia.Listener;

import me.G4meM0ment.Orbia.Orbia;
import me.G4meM0ment.Orbia.Handler.SIHandler;
import me.G4meM0ment.Orbia.Tutorial.TutorialHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Chatter;
import com.dthielke.herochat.Chatter.Result;
import com.dthielke.herochat.Herochat;

public class PListener implements Listener{
	
	private RPGEssentials plugin;
	private Orbia subplugin;
	private TutorialHandler tutHandler;
	private SIHandler sih;
		
	public PListener(RPGEssentials plugin){
		this.plugin = plugin;
		subplugin = new Orbia();
		tutHandler = new TutorialHandler();
		sih = new SIHandler(subplugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {		
		return;
		/*if(!(event.getEntity() instanceof Player)) return;
		Player p = event.getEntity();
		if(p.getKiller() == null || !(p.getKiller() instanceof Player)) return;
		Player k = p.getKiller();
		UPlayer up = UPlayer.get(p);
		UPlayer uk = UPlayer.get(k);
		Faction faction = up.getFaction();
		Faction faction2 = uk.getFaction();
		
		if(faction.getRelationTo(faction2).isAtLeast(Rel.ENEMY)) {
			up.setPower(up.getPower() - subplugin.getConfig().getDouble("factionsPowerLossOnEnemyKill"));
			up.sendMessage("You lost "+subplugin.getConfig().getDouble("factionsPowerLossOnEnemyKill")+" power!");
		} */
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player p = event.getPlayer();
		if(p == null) return;
		Bukkit.getServer().dispatchCommand(p, "vs disable");
		event.setJoinMessage(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"+"+ChatColor.DARK_GRAY+"] "+p.getName());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				if(plugin.getHerochat() != null) 
				{
					try
					{
						plugin.getHerochat();
						Herochat.getChatterManager().getChatter(p).addChannel(Herochat.getChannelManager().getChannel("Lokal"), false, false);
						Herochat.getChatterManager().getChatter(p).addChannel(Herochat.getChannelManager().getChannel("Global"), false, false);
					}
					catch(NullPointerException e) {}
				}
			}
		}, 20);
		
		if(!tutHandler.finishedTutorial(p))
			tutHandler.startStage(p,tutHandler.getStage(p));
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) 
	{
		final Player p = event.getPlayer();
		if(p == null) return;
		event.setQuitMessage(ChatColor.DARK_GRAY+"["+ChatColor.DARK_RED+"-"+ChatColor.DARK_GRAY+"] "+p.getName());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK || !event.getPlayer().getItemInHand().hasItemMeta()) return;
		if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Bauhammer"))
		{
			sih.changeSubId(event.getClickedBlock());
		}
	}
	
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onChannelChat(ChannelChatEvent event) 
	{
		plugin.getHerochat();
		Channel local = Herochat.getChannelManager().getChannel("Lokal");
		Chatter c = event.getSender();
		if(event.getChannel() == local && !isChatterInRange(c, local))
		{
			event.getSender().getPlayer().sendMessage(ChatColor.YELLOW+"Niemand kann dich hören!");
			event.setResult(Result.FAIL);
		}
	}
	
	private boolean isChatterInRange(Chatter chatter, Channel channel) 
	{
		plugin.getHerochat();
		for(Chatter c : Herochat.getChatterManager().getChatters())
		{
			if(chatter == c)
				continue;
			if(chatter.isInRange(c, channel.getDistance()))
				return true;
		}
		return false;
		
	}
}
