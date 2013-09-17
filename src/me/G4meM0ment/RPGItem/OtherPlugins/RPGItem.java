package me.G4meM0ment.RPGItem.OtherPlugins;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import net.dandielo.citizens.traders_v3.core.exceptions.InvalidItemException;
import net.dandielo.citizens.traders_v3.core.exceptions.attributes.AttributeValueNotFoundException;
import net.dandielo.citizens.traders_v3.utils.NBTUtils;
import net.dandielo.citizens.traders_v3.utils.items.Attribute;
import net.dandielo.citizens.traders_v3.utils.items.ItemFlag;
import net.dandielo.citizens.traders_v3.utils.items.flags.Abstract;


@Attribute(name="RpgItem", key=".rpg")
public class RPGItem extends ItemFlag{
	
	private CustomItemHandler customItemHandler;
	private ItemHandler itemHandler;
	
	private int rpgID;
	
	public RPGItem(String key) {
		super(key);
		customItemHandler = new CustomItemHandler();
		itemHandler = new ItemHandler();
	}
	
	@Override
	public void onAssign(ItemStack item, boolean endItem) throws InvalidItemException 
	{
		//IF THE ITEM IS ABSTRACT THEN WE check the endItem boolean
	    //{
			// IT IT'S FALSE then we dont generate a new value, we set the lore with <undefined> and DONT use the saved value (that will prevent generating ID's when the item needs just to be displayed in the traders stock)
			//...
			//...
	
			//ELSE we need to generate a new ID and and set it at the end of the lore
			//...
		    //...
	    //}
	
	    //ELSE IF THE ITEM IS NOT ABSTRACT THEN
	    //We add att the end of the lore the Saved ID 
	    //...
	    //...
	
		//get the existing lore
		ItemMeta meta = item.getItemMeta();
		if (meta.getLore() == null )
			meta.setLore(new ArrayList<String>());
		
        if ( this.item.hasFlag(Abstract.class) )
        {
            if ( endItem ) 
                meta.getLore().add(ChatColor.BLACK+Integer.toString(customItemHandler.getFreeId((ChatColor.stripColor(meta.getDisplayName())))));
            else
                meta.getLore().add(ChatColor.BLACK+"undefined");
        }
        //if it's not abstract
        else
            meta.getLore().add(ChatColor.BLACK+Integer.toString(this.rpgID));
		//save the new lore
		item.setItemMeta(meta);
	}
	
	public void onFactorize(ItemStack item) throws AttributeValueNotFoundException
	{
		//n lore = no RPG item
		if ( !item.getItemMeta().hasLore() )
			throw new AttributeValueNotFoundException();
		
		//get the lore without any dtlTrader lore lines
		//REMOVE ANY LINE THAT WAS ADDED BY DTLTRADER PLUGIN
		List<String> cleanedLore = NBTUtils.getLore(item);
	
		//CHECK IF THE LORE STILL HAS ENTRIES
		if ( cleanedLore.isEmpty() )
			throw new AttributeValueNotFoundException();
	
		
		if(itemHandler.isCustomItem(item)) 
		{
			if(this.item.hasFlag(Abstract.class))
				this.rpgID = -1;
			else
				this.rpgID = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1)));
		} else 
			throw new AttributeValueNotFoundException();
		//Read the last line and check it if it's a RPG item
		//...
		//...
		//...
		//if not then throw this exception: throw new AttributeValueNotFoundException();
	
		//Now we check if the ITEM is ABSTRACT, that means the item is only in the traders stock or is only used for a short time to compare items
		//IF the items is ABSTRACT then we set the id to -1 (that means we can forget it and it's not needed)
		//...
		//...
	
		//ELSE we save the items ID from the lore, and remove the LAST LORE LINE
		//...
		//...
	}
	
	@Override
	public boolean equalsStrong(ItemFlag that) 
	{
		//NOW WE CHECK ITEMS, if both items will ahve value -1 then its a check againts an item in the traders stock and an item the player tries to sell
		//If the ID of THIS item IS NOT -1 THEN IT WILL FAIL AGAINST OTHER ITEMS IN THE PLAYERS INVENTORY AND ADDED AS A SEPARATE ITEM (new STACK)
		return ((RPGItem) that).rpgID == this.rpgID;
	}
	
	@Override
	public boolean equalsWeak(ItemFlag that) 
	{
		return equalsStrong(that);
	}
}
