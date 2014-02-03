package me.G4meM0ment.Orbia.Handler;

import java.util.HashMap;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.Gradient;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.herocraftonline.heroes.characters.Hero;

public class ManaHandler {
	
	private RPGEssentials plugin;
	
	private static final HashMap<Player, Gradient> widgets = new HashMap<Player, Gradient>();
//	private static Color redBar = new Color(0.69f,0.09f,0.12f,1f);
	private static Color blueBar = new Color(0,0,1f,1f);
	
	public ManaHandler(RPGEssentials plugin)
	{
		this.plugin = plugin;
	}
	
	public Gradient getWidget(Player p)
	{
		return widgets.get(p);
	}
	public void setWidget(Player p, Gradient label)
	{
		widgets.put(p, label);
	}
	
	public void startUpdater()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
			@Override
			public void run() 
			{
				for(Player p : Bukkit.getOnlinePlayers())
					if(plugin.getHeroes().getCharacterManager().getHero(p).getHeroClass().getName().equalsIgnoreCase("Novize"))
						updateManaBar(p, null);
			}
		}, 0, 5);
	}
	
	public void updateManaBar(Player p, Hero h)
	{
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		Gradient widget = getWidget(p);
		if(h == null)
			h = plugin.getHeroes().getCharacterManager().getHero(p);
		Screen s = sp.getMainScreen();
		
		if(widget == null && s.getScreenType().equals(ScreenType.GAME_SCREEN))
		{
			widget = new GenericGradient();
			
			widget.setHeight(4);
			widget.setX(sp.getMainScreen().getHealthBar().getX()+10);
			
			sp.getMainScreen().attachWidget(plugin, widget);
			setWidget(p, widget);
		}
		
		int maxLength = 160;
		double percent = ((double)h.getMana() / (double)h.getMaxMana())*100.0;
		int length = (int) ((percent/100)*maxLength);
		
		widget.setColor(blueBar);
		widget.setX(sp.getMainScreen().getHealthBar().getX()+10);
		widget.setWidth(length);
	
		if(isUnderwater(s.getPlayer()) || hasArmor(s.getPlayer()))
			widget.setY(sp.getMainScreen().getArmorBar().getY()-2);
		else
			widget.setY(sp.getMainScreen().getHealthBar().getY()-2);
		
		widget.updateSize();

		if(!sp.getMainScreen().getAttachedWidgetsAsSet(true).contains(widget))
			sp.getMainScreen().attachWidget(plugin, widget);
	}
	
	private boolean hasArmor(Player p)
	{
		if(p == null) return false;
		if(p.getInventory().getHelmet() == null 
				&& p.getInventory().getChestplate() == null 
				&& p.getInventory().getLeggings() == null
				&& p.getInventory().getBoots() == null)
			return false;
		return true;
	}
	private boolean isUnderwater(Player p)
	{
		if(p == null) return false;
		if(p.getLocation().getBlock().getRelative(BlockFace.UP).getType().equals(Material.WATER) || p.getLocation().getBlock().getRelative(BlockFace.UP).getType().equals(Material.STATIONARY_WATER))
			return true;
		return false;
	}
}