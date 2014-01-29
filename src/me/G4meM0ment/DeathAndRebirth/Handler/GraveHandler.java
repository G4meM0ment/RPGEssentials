package me.G4meM0ment.DeathAndRebirth.Handler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import me.G4meM0ment.DeathAndRebirth.Types.DARPlayer;
import me.G4meM0ment.DeathAndRebirth.Types.Grave;

public class GraveHandler {
	
	private ConfigHandler cH;
	private GhostHandler gH;
	
	public GraveHandler()
	{
		cH = new ConfigHandler();
		gH = new GhostHandler();
	}
	
	/**
	 * To check click events
	 * @param p
	 * @param loc
	 * @return
	 */
	public boolean isPlayersGrave(DARPlayer p, Location loc)
	{
		if(p.getGrave().getLocation().distance(loc) <= cH.getMaxGraveDistance())
			return true;
		return false;
	}
	
	/**
	 * For definite location checks
	 * @param loc
	 * @return
	 */
	public boolean isGrave(Location loc)
	{
		for(DARPlayer p : gH.getDARPlayers(loc.getWorld()))
		{
			if(p.getGrave() == null) continue;
			if(p.getGrave().getLocation().equals(loc))
				return true;
		}
		return false;
	}
	
	public void placeSign(Grave g, Block b)
	{
		if(g == null || b == null) return;
		
		Material m = b.getType();
		
		/*
		 * TODO algorithm to place sign next to the water 
		 */
		if(m.equals(Material.LAVA) || m.equals(Material.WATER)) return;

		g.setBlockMaterial(b.getType());
		
		b.setType(Material.SIGN_POST);
		Sign sign = (Sign) b.getState();
		sign.setLine(1, cH.getSignText());
		sign.setLine(2, g.getPlayer().getName());
		/*
		 * TODO add offline line
		 */
		sign.update(true);
		
		g.setSign(sign);
	}
	
	public void removeSign(Grave g)
	{
		if(g == null) return;
		
		g.getSign().getBlock().setType(g.getBlockMaterial());
	}

}
