package me.G4meM0ment.RPGEssentials.Schedule;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.Handler.EventHandler.InventoryHandler;

public class ScheduleHandler {
	
	private RPGItem rpgItem;
	private InventoryHandler invHandler;
	
	private static int seconds;
	private static int minutes;
	private static int hours;
	private static int days;
	
	public ScheduleHandler() {
		rpgItem = new RPGItem();
		invHandler = new InventoryHandler();
	}
	
	public void callSecond() {
		if(seconds >= 60)
			seconds = 0;
		seconds++;
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			invHandler.processArmor(p);
			invHandler.processItem(p);
		}
	}

	public void callMinute() {
		if(minutes >= 60)
			minutes = 0;
		minutes++;
		
		if(minutes == 15 || minutes == 30 || minutes == 45 || minutes == 60)
			rpgItem.saveData();
	}

	public void callHour() {
		if(hours >= 24)
			hours = 0;
		hours++;
	}

	public void callDay() {
		days++;
	}



}
