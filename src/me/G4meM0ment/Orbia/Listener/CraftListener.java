package me.G4meM0ment.Orbia.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class CraftListener implements Listener {
	
	private static List<Material> nonCraftables = new ArrayList<Material>();	
	
	public CraftListener()
	{
		nonCraftables.add(Material.STONE_AXE);
		nonCraftables.add(Material.STONE_HOE);
		nonCraftables.add(Material.STONE_PICKAXE);
		nonCraftables.add(Material.STONE_SPADE);
		nonCraftables.add(Material.STONE_SWORD);
		
		nonCraftables.add(Material.LEATHER_BOOTS);
		nonCraftables.add(Material.LEATHER_CHESTPLATE);
		nonCraftables.add(Material.LEATHER_HELMET);
		nonCraftables.add(Material.LEATHER_LEGGINGS);
		
		nonCraftables.add(Material.IRON_AXE);
		nonCraftables.add(Material.IRON_BARDING);
		nonCraftables.add(Material.IRON_BOOTS);
		nonCraftables.add(Material.IRON_CHESTPLATE);
		nonCraftables.add(Material.IRON_HELMET);
		nonCraftables.add(Material.IRON_HOE);
		nonCraftables.add(Material.IRON_LEGGINGS);
		nonCraftables.add(Material.IRON_PICKAXE);
		nonCraftables.add(Material.IRON_SPADE);
		nonCraftables.add(Material.IRON_SWORD);
		
		nonCraftables.add(Material.GOLD_AXE);
		nonCraftables.add(Material.GOLD_BARDING);
		nonCraftables.add(Material.GOLD_BOOTS);
		nonCraftables.add(Material.GOLD_CHESTPLATE);
		nonCraftables.add(Material.GOLD_HELMET);
		nonCraftables.add(Material.GOLD_HOE);
		nonCraftables.add(Material.GOLD_LEGGINGS);
		nonCraftables.add(Material.GOLD_PICKAXE);
		nonCraftables.add(Material.GOLD_SPADE);
		nonCraftables.add(Material.GOLD_SWORD);
		
		nonCraftables.add(Material.DIAMOND_AXE);
		nonCraftables.add(Material.DIAMOND_BARDING);
		nonCraftables.add(Material.DIAMOND_BOOTS);
		nonCraftables.add(Material.DIAMOND_CHESTPLATE);
		nonCraftables.add(Material.DIAMOND_HELMET);
		nonCraftables.add(Material.DIAMOND_HOE);
		nonCraftables.add(Material.DIAMOND_LEGGINGS);
		nonCraftables.add(Material.DIAMOND_PICKAXE);
		nonCraftables.add(Material.DIAMOND_SPADE);
		nonCraftables.add(Material.DIAMOND_SWORD);
		
		nonCraftables.add(Material.BOW);
		nonCraftables.add(Material.FISHING_ROD);
		nonCraftables.add(Material.SHEARS);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerCraft(PrepareItemCraftEvent event)
	{
		if(nonCraftables.contains(event.getRecipe().getResult().getType()))
			event.getRecipe().getResult().setType(Material.AIR);
	}
}
