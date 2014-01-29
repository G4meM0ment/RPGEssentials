package me.G4meM0ment.RPGEssentials.DataStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

public class FileHandler {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	
	public FileHandler(RPGEssentials plugin) 
	{
		this.plugin = plugin;
	}
	public FileHandler() 
	{
		
	}
	
	public List<File> getFiles(File directory) 
	{
	    List<File> files = new ArrayList<File>();
		for (final File fileEntry : directory.listFiles()) 
	            files.add(fileEntry);
		return files;
	}

}
