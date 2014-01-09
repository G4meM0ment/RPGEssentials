package me.G4meM0ment.RPGItem.OtherPlugins;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import net.dandielo.citizens.traders_v3.tNpcStatus;
import net.dandielo.citizens.traders_v3.core.exceptions.InvalidItemException;
import net.dandielo.citizens.traders_v3.core.exceptions.attributes.AttributeValueNotFoundException;
import net.dandielo.citizens.traders_v3.utils.NBTUtils;
import net.dandielo.citizens.traders_v3.utils.items.Attribute;
import net.dandielo.citizens.traders_v3.utils.items.ItemFlag;
import net.dandielo.citizens.traders_v3.utils.items.flags.Abstract;


@Attribute(name="RpgItem", key=".rpg")
public class RPGItem extends ItemFlag{
	/* Init both handlers by instances? Singleton system */
	private /*static*/ CustomItemHandler customItemHandler; // = CustomItemHandler.getInstance();
	private /*static*/ ItemHandler itemHandler; // = ItemHandler.getInstance();
	private me.G4meM0ment.RPGItem.RPGItem subplugin;
	
	private int rpgID;
	
	public RPGItem(String key) {
		super(key);
		
		//I think it would be better to change both CustomItemHandler and ItemHandler into singletons
		//and get instances of them
		//
		//customItemHandler = CustomItemHandler.getInstance();
		//itemHandler = ItemHandler.getInstance();
		
		subplugin = new me.G4meM0ment.RPGItem.RPGItem();
		customItemHandler = new CustomItemHandler();
		itemHandler = new ItemHandler();
		
		rpgID = -1;
	}
	
	@Override
	public void onAssign(ItemStack item, boolean endItem) throws InvalidItemException 
	{
		if(!subplugin.getConfig().getBoolean("useIDs")) return;
		
		//get the existing lore
		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = meta.getLore();
		if ( itemLore == null ) //If no lore exists create a new list for it
			itemLore = new ArrayList<String>();
		
		//Check if the item is Abstract = In traders stock or used for copmaring when selling that item
	        if ( this.item.hasFlag(Abstract.class) )
	        {
			if ( endItem ) 
				customItemHandler.registerItem(item, 0);
			else
				itemLore.add(ChatColor.BLACK+"undefined");
	        }
	        //if it's not abstract use the old ID
	        else
				customItemHandler.registerItem(item, this.rpgID);
		
	}
	
	public void onFactorize(ItemStack item) throws AttributeValueNotFoundException
	{
		if(!subplugin.getConfig().getBoolean("useIDs")) return;
		
		//n lore = no RPG item
		if ( !item.getItemMeta().hasLore() )
			throw new AttributeValueNotFoundException();
		
		//Get only the Items Lore
		List<String> cleanedLore = NBTUtils.getLore(item);
	
		//enough lore to be an RPG item?
		if ( cleanedLore.isEmpty() )
			throw new AttributeValueNotFoundException();
			
		//Create a clone of the item and set the cleaned lore to it (needs to be done when selling an item)
		ItemStack itemClone = item.clone();
		itemClone.getItemMeta().setLore(cleanedLore);
		
		//Is this a custom RPG item?
		if(!itemHandler.isCustomItem(itemClone)) 
			throw new AttributeValueNotFoundException();
			
		//Do we need to save the ID
		if(this.item.hasFlag(Abstract.class))
			this.rpgID = -1;
		else
		//get the last lore lone from the cleaned lore
			this.rpgID = Integer.parseInt(ChatColor.stripColor(cleanedLore.get(cleanedLore.size()-1)));
		
		//remove the last line from the lore
		cleanedLore.remove(cleanedLore.size()-1);
		
		//set back the lore
		ItemMeta meta = item.getItemMeta();
		meta.setLore(cleanedLore); //dunno if this might make some problems later, for now its ok :) 
		item.setItemMeta(meta);
	}
	
	@Override
	public boolean equalsStrong(ItemFlag that) 
	{
		return ((RPGItem) that).rpgID == this.rpgID;
	}
	
	@Override
	public boolean equalsWeak(ItemFlag that) 
	{
		return equalsStrong(that);
	}
	
	@Override
	public void onStatusLoreRequest(tNpcStatus status, List<String> lore)
	{
		lore.add(ChatColor.BLACK + "undefined");
	}
}
