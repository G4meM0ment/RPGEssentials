package me.G4meM0ment.Rentables.Listener;

import me.G4meM0ment.Rentables.DataStorage.LabelConfig;
import me.G4meM0ment.Rentables.Handler.RentableHandler;
import me.G4meM0ment.Rentables.Rentable.Rentable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PListener implements Listener{
	
	private RentableHandler rentHandler;
	private LabelConfig labelConfig;
	
	public PListener() {
		rentHandler = new RentableHandler();
		labelConfig = new LabelConfig();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		if(!(event.getClickedBlock().getType() == Material.WALL_SIGN) && !(event.getClickedBlock().getType() == Material.SIGN_POST)) return;
		Sign sign = (Sign) event.getClickedBlock().getState();
		if(!rentHandler.isRentableSign(event.getClickedBlock().getLocation())) return;
		Rentable rent = rentHandler.getRentableBySign(event.getClickedBlock());
		if(!rent.getHeader().equalsIgnoreCase(sign.getLine(0))) return;
		Player p = null;
		try {
			p = Bukkit.getPlayerExact(rent.getRenter());
		} catch (NullPointerException e) {}
		
		if(p == event.getPlayer() && event.getPlayer().isSneaking())
			rentHandler.unrentRentable(rent);
		else {
			for(String s : labelConfig.getConfig().getKeys(false)){
				if(s.equalsIgnoreCase(rent.getHeader().replace("[", "").replace("]", ""))) {
					int rentedLabels = 0;
					for(Rentable r : rentHandler.getRentables().values()) {
						try {
							p = Bukkit.getPlayerExact(r.getRenter());
						} catch (NullPointerException e) {}
						if(p == event.getPlayer()) {
							rentedLabels++;
						}
					}
					if(rentedLabels < labelConfig.getConfig().getInt(s)) {
						rentHandler.rentRentable(rent, event.getPlayer());							
					} else {
						p.sendMessage("Rented to many "+s);
						//TODO add messenger
					}
				}
			}
		}
	}
}
