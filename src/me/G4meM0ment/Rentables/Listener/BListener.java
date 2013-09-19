package me.G4meM0ment.Rentables.Listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.Rentables.Handler.PermHandler;
import me.G4meM0ment.Rentables.Handler.RentableHandler;
import me.G4meM0ment.Rentables.Rentable.Rentable;

public class BListener implements Listener{
	
	private RPGEssentials plugin;
	private RentableHandler rh;
	private PermHandler ph;
	
	public BListener(RPGEssentials plugin) {
		this.plugin = plugin;
		rh = new RentableHandler();
		ph = new PermHandler(plugin);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent event) {
		if(event.getBlock() == null) return;
		
		Player p = event.getPlayer();
		Player owner = event.getPlayer();
		Selection sel = plugin.getWorldEdit().getSelection(p);
		if(p == null || sel == null) return;
		//TODO add messenger
		if(!ph.hasRentablesPerm(p)) return;
		//TODO add messenger
		if(rh.getPlayersAdminModeEnabled().contains(p))
			owner = null;
		
		String header = null, price = null, time = null;
		if(event.getLine(0).equalsIgnoreCase("[Rent]")) {
			for(String s : event.getLines()) {
				if(s.equalsIgnoreCase("[Rent]")) continue;
				if(!s.contains("p:") && !s.contains("t:")) header = s;
				if(s.contains("p:")) {
					price = s.split(":")[1];
				}
				if(s.contains("t:")) {
					time = s.split(":")[1];
				}
			}
			List<String> newLines = rh.proceedRentable((Sign) event.getBlock().getState(), header, price, time, sel, owner);
			if(newLines == null) return;
			else {
				for(int i = 0; i < 4; i++) {
					event.setLine(i, newLines.get(i));
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		if(b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
			Rentable r = rh.getRentableBySign(b);
			if(r != null) {
				if((!r.getOwner().isEmpty() && Bukkit.getPlayerExact(r.getOwner()) == event.getPlayer()) || ph.hasRentablesAdminPerm(event.getPlayer())) {
					rh.removeRentable(r);
				}

			}
		}
	}
}
