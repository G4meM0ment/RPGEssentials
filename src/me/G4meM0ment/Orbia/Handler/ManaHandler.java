package me.G4meM0ment.Orbia.Handler;

import java.util.HashMap;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.ScreenType;

import com.herocraftonline.heroes.characters.Hero;

public class ManaHandler {
	
	private RPGEssentials plugin;
	
	public HashMap<Player, GenericLabel> widgets = new HashMap<Player, GenericLabel>();
	
	public ManaHandler(RPGEssentials plugin)
	{
		this.plugin = plugin;
	}
	
	public GenericLabel getWidget(Player p)
	{
		return widgets.get(p);
	}
	public void setWidget(Player p, GenericLabel label)
	{
		widgets.put(p, label);
	}
	
	public void updateManaBar(Player p, Hero h)
	{
		if(p == null) return;
		if(h == null)
			h = plugin.getHeroes().getCharacterManager().getHero(p);
		Screen s = SpoutManager.getPlayer(p).getCurrentScreen();
		
		GenericLabel manaLabel = getWidget(p);
		if(manaLabel == null && s.getScreenType() == ScreenType.GAME_SCREEN)
		{
			manaLabel = new GenericLabel();
			manaLabel.setHeight(10);
			manaLabel.setWidth(35);
//			manaLabel.setMarginBottom(s.getHeight()/4);
//			manaLabel.setMarginLeft(s.getWidth()/2-manaLabel.getWidth()/2);
			manaLabel.setY(s.getHeight()-s.getHeight()/5);
			manaLabel.setX(s.getWidth()/2-manaLabel.getWidth()/2);
			manaLabel.setText(ChatColor.DARK_BLUE+""+h.getMana()+"/"+ h.getMaxMana());
			
			SpoutManager.getPlayer(p).getCurrentScreen().attachWidget(plugin, manaLabel);
			setWidget(p, manaLabel);
			return;
		}
		else if(manaLabel == null)
			return;
		manaLabel.setText(ChatColor.DARK_BLUE+""+h.getMana()+"/"+ h.getMaxMana());
		s.updateWidget(manaLabel);
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
}
