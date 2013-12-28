package me.G4meM0ment.UnamedPortalPlugin.Listener;

import me.G4meM0ment.UnamedPortalPlugin.UnnamedPortalPlugin;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PortalHandler;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class CListener implements Listener {
	
	@SuppressWarnings("unused")
	private UnnamedPortalPlugin subplugin;
	private PortalHandler ph;
	
	public CListener(UnnamedPortalPlugin subplugin)
	{
		this.subplugin = subplugin;
		ph = new PortalHandler(subplugin);
	}	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChunkUnload(ChunkUnloadEvent event)
	{
		for(Portal p : ph.getPortals().values())
		{
			Chunk eventChunk = event.getChunk();
			Chunk testChunk = p.getDestination().getChunk();
		 
			if(eventChunk.getX() == testChunk.getX() & eventChunk.getZ() == testChunk.getZ())
			{
				event.setCancelled(true);
			}
		}
	}
}
