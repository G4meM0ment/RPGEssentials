package me.G4meM0ment.Orbia.Handler.Job;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class JobListener implements Listener {
	
	private RPGEssentials plugin;
	
	private static List<Material> miner = new ArrayList<Material>();
	private static List<Material> farmer = new ArrayList<Material>();
	
	public JobListener(RPGEssentials plugin)
	{
		this.plugin = plugin;
		
		miner.add(Material.DIAMOND_PICKAXE);
		miner.add(Material.GOLD_PICKAXE);
		miner.add(Material.IRON_PICKAXE);
		
		farmer.add(Material.WOOD_HOE);
		farmer.add(Material.STONE_HOE);
		farmer.add(Material.IRON_HOE);
		farmer.add(Material.GOLD_HOE);
		farmer.add(Material.DIAMOND_HOE);
		farmer.add(Material.WHEAT);
		farmer.add(Material.CARROT_ITEM);
		farmer.add(Material.SUGAR_CANE);
		farmer.add(Material.POTATO_ITEM);
		farmer.add(Material.SEEDS);
		farmer.add(Material.SHEARS);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
		
		Player p = event.getPlayer();
		if(p == null) return;
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getClickedBlock().getType() == Material.BREWING_STAND)
			{
				if(!p.hasPermission("orbia.job.alchemist"))
				{
					event.setCancelled(true);
					p.sendMessage(ChatColor.GRAY+"Du bist nicht trainiert Tränke zu brauen!");			
				}
			}
		}
		
		if(isMinerTool(p.getItemInHand()))
		{
			if(p.hasPermission("orbia.job.miner"))
				return;
			else
			{
				event.setCancelled(true);
				p.sendMessage(ChatColor.GRAY+"Du bist nicht trainiert eine "+ChatColor.WHITE+p.getItemInHand().getType().toString().replace("_", " ").toLowerCase()+ChatColor.GRAY+" zu verwenden!");
			}
		}
		if(isFarmerTool(p.getItemInHand()) && (!plugin.getHeroes().getCharacterManager().getHero(p).getHeroClass().toString().equalsIgnoreCase("kundschafter") && p.getItemInHand().getType() == Material.SHEARS))
		{
			if(p.hasPermission("orbia.job.farmer"))
				return;
			else
			{
				event.setCancelled(true);
				p.sendMessage(ChatColor.GRAY+"Du bist nicht trainiert eine "+ChatColor.WHITE+p.getItemInHand().getType().toString().replace("_", " ").toLowerCase()+ChatColor.GRAY+" zu verwenden!");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player p = event.getPlayer();
		if(p == null) return;
		if(event.getBlock() == null) return;
		if(isMinerTool(p.getItemInHand()))
		{
			if(p.hasPermission("orbia.job.miner") || (event.getBlock().getType() == Material.LONG_GRASS && plugin.getHeroes().getCharacterManager().getHero(p).getHeroClass().toString().equalsIgnoreCase("kundschafter")))
				return;
			else
			{
				event.setCancelled(true);
				p.sendMessage(ChatColor.GRAY+"Du bist nicht trainiert eine "+ChatColor.WHITE+p.getItemInHand().getType().toString().replace("_", " ").toLowerCase()+ChatColor.GRAY+" zu verwenden!");
			}
		}
		if(isFarmerTool(p.getItemInHand()))
		{
			if(p.hasPermission("orbia.job.farmer"))
				return;
			else
			{
				event.setCancelled(true);
				if(!plugin.getHeroes().getCharacterManager().getHero(p).getHeroClass().toString().equalsIgnoreCase("kundschafter"))
					p.sendMessage(ChatColor.GRAY+"Du bist nicht trainiert eine "+ChatColor.WHITE+p.getItemInHand().getType().toString().replace("_", " ").toLowerCase()+ChatColor.GRAY+" zu verwenden!");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(!(event.getRightClicked() instanceof Animals)) return;

		Player p = event.getPlayer();
		if(p == null) return;
		
		if(isFarmerTool(p.getItemInHand()) || (event.getRightClicked() instanceof Cow && p.getItemInHand().getType() == Material.BUCKET) || (event.getRightClicked() instanceof Sheep && p.getItemInHand().getType() == Material.SHEARS))
		{
			if(p.hasPermission("orbia.job.farmer"))
				return;
			else
			{
				event.setCancelled(true);
				p.sendMessage(ChatColor.GRAY+"Du bist nicht trainiert eine "+ChatColor.WHITE+p.getItemInHand().getType().toString().replace("_", " ").toLowerCase()+ChatColor.GRAY+" zu verwenden!");
			}
		}
	}
	
	public boolean isMinerTool(ItemStack item)
	{
		for(Material m : miner)
		{
			if(m.equals(item.getType()))
				return true;
		}
		return false;
	}
	public boolean isFarmerTool(ItemStack item)
	{
		for(Material m : farmer)
		{
			if(m.equals(item.getType()))
				return true;
		}
		return false;
	}
}
