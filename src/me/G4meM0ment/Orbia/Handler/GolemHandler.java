package me.G4meM0ment.Orbia.Handler;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class GolemHandler {
	
	RPGEssentials plugin;
	
	public GolemHandler(RPGEssentials plugin)
	{
		this.plugin = plugin;
		start();
	}
	
	private void start()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				for(World w : Bukkit.getWorlds())
				{
					if(w.getPlayers().size() > 0)
					{
						for(Player p : w.getPlayers())
						{
							if(p.getGameMode() == GameMode.CREATIVE)
								continue;
							for(Entity e : p.getNearbyEntities(30, 30, 30))
							{
								if(e instanceof IronGolem)
								{
									IronGolem i = (IronGolem) e;
									if(!(i.getTarget() instanceof Player))
									{
										i.setPlayerCreated(false);
										i.damage(0.0, p);
										i.setTarget((LivingEntity) p);
									}
								}	
							}
						}
					}
				}
			}
		}, 0, 40);
	}

}
