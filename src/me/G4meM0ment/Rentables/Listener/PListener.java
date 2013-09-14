package me.G4meM0ment.Rentables.Listener;

import me.G4meM0ment.Rentables.Handler.RentableHandler;
import me.G4meM0ment.Rentables.Rentable.Rentable;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PListener implements Listener{
	
	private RentableHandler rentHandler;
	
	public PListener() {
		rentHandler = new RentableHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		if(!(event.getClickedBlock().getType() == Material.WALL_SIGN) && !(event.getClickedBlock().getType() == Material.SIGN_POST)) return;
		Sign sign = (Sign) event.getClickedBlock().getState();
		if(!rentHandler.isRentableSign(event.getClickedBlock())) return;
		Rentable rent = rentHandler.getRentableBySign(event.getClickedBlock());
		if(!rent.getHeader().equalsIgnoreCase(sign.getLine(0))) return;
		
		rentHandler.rentRentable(rent, event.getPlayer());
	}
}
