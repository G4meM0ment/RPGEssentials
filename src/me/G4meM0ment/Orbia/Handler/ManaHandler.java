package me.G4meM0ment.Orbia.Handler;

import java.util.HashMap;

import me.G4meM0ment.DeathAndRebirth.Framework.DARPlayer;
import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Gradient;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.herocraftonline.heroes.characters.Hero;

public class ManaHandler {
	
	private RPGEssentials plugin;
	private GhostHandler ghostH;
	
	private static final HashMap<Player, Gradient> widgets = new HashMap<Player, Gradient>();
	private static final HashMap<Player, Texture> overlays = new HashMap<Player, Texture>();
//	private static Color redBar = new Color(0.69f,0.09f,0.12f,1f);
	private static Color blueBar = new Color(0,0,1f,1f);
	
	public ManaHandler(RPGEssentials plugin)
	{
		this.plugin = plugin;
		ghostH = new GhostHandler();
	}
	
	public Gradient getWidget(Player p)
	{
		return widgets.get(p);
	}
	public void setWidget(Player p, Gradient label)
	{
		widgets.put(p, label);
	}
	
	public Texture getOverlay(Player p)
	{
		return overlays.get(p);
	}
	public void setOverlay(Player p, Texture label)
	{
		overlays.put(p, label);
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
		}, 0, 10);
	}
	
	public void updateManaBar(Player p, Hero h)
	{
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		Gradient widget = getWidget(p);
		Texture overlay = getOverlay(p);
		if(h == null)
			h = plugin.getHeroes().getCharacterManager().getHero(p);
		
		Screen s = sp.getMainScreen();
		
		/*
		 * 
		 * the actual bar
		 * 
		 */
		if(widget == null && s.getScreenType().equals(ScreenType.GAME_SCREEN))
		{
			widget = new GenericGradient();
			
			widget.setHeight(4);
			widget.setX(sp.getMainScreen().getHealthBar().getX()+13);
			
			sp.getMainScreen().attachWidget(plugin, widget);
			setWidget(p, widget);
		}
		
		int maxLength = 158;
		double percent = ((double)h.getMana() / (double)h.getMaxMana())*100.0;
		int length = (int) ((percent/100)*maxLength);
		
		widget.setColor(blueBar);
		widget.setX(sp.getMainScreen().getHealthBar().getX()+12);
		widget.setWidth(length);
	
		if(isUnderwater(s.getPlayer()) || hasArmor(s.getPlayer()))
			widget.setY(sp.getMainScreen().getArmorBar().getY()-2);
		else
			widget.setY(sp.getMainScreen().getHealthBar().getY()-3);
		
		widget.updateSize();
		
		/*
		 * 
		 * overlay texture
		 * 
		 */
		if(overlay == null && s.getScreenType().equals(ScreenType.GAME_SCREEN))
		{
			overlay = new GenericTexture();
			overlay.setUrl("https://dl.dropboxusercontent.com/u/96045686/Skins/manabar_overlay.png").setPriority(RenderPriority.Lowest);
			overlay.setDrawAlphaChannel(true);
			overlay.setHeight(widget.getHeight()+2);
			overlay.setWidth(maxLength+3);
			
			sp.getMainScreen().attachWidget(plugin, overlay);
			setOverlay(p, overlay);
		}
		overlay.setX(widget.getX()-1);
		overlay.setY(widget.getY()-1);


		/*
		 * attach if not
		 */
		if(!sp.getMainScreen().getAttachedWidgetsAsSet(true).contains(widget))
			sp.getMainScreen().attachWidget(plugin, widget);
		if(!sp.getMainScreen().getAttachedWidgetsAsSet(true).contains(overlay))
			sp.getMainScreen().attachWidget(plugin, overlay);
		
		/*
		 * make invisible or visible if needed
		 */
		if(isDead(p) || p.getGameMode().equals(GameMode.CREATIVE))
		{
			widget.setVisible(false);
			overlay.setVisible(false);
		}
		else
		{
			widget.setVisible(true);
			overlay.setVisible(true);
		}
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
	private boolean isDead(Player p)
	{
		DARPlayer darP = ghostH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return false;
		if(darP.isDead()) return true;
		return false;
	}
}