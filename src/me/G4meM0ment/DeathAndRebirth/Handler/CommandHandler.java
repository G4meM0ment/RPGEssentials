package me.G4meM0ment.DeathAndRebirth.Handler;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class CommandHandler {

	private DeathAndRebirth subplugin;
	private ShrineHandler shrineH;
	
	public CommandHandler()
	{
		subplugin = new DeathAndRebirth();
		shrineH = new ShrineHandler();
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) 
	{
		return false;
	}
}
