package me.G4meM0ment.RPGEssentials.Schedule;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;

public class Schedule implements Runnable {

	private RPGEssentials plugin;
	private ScheduleHandler sh;
	
	private static boolean isRunning = true;
	
	public Schedule(RPGEssentials plugin) {
		this.plugin = plugin;
		sh = new ScheduleHandler();
	}

	public void startSchedule() {
		// setupSchedule();
		run();
		isRunning = true;
	}
	public void stopSchedule() {
		isRunning = false;
	}

	@Override
	public void run() {
		
		int sleepTime = 50;
		int ticks = 0;
		int seconds = 0;
		int minutes = 0;
		int hours = 0;

		while(isRunning) {
			
			if(plugin.getRPGItem().isDisabling())
				break;
			
			try {
			if(ticks >= 20) {
				seconds++;
				ticks = 0;
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						sh.callSecond();
					}
				});
			}
			if(seconds >= 60) {
				minutes++;
				seconds = 0;
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						sh.callMinute();
					}
				});
			}
			if(minutes >= 60) {
				hours++;
				minutes = 0;
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						sh.callHour();
					}
				});
			}
			if(hours >= 24) {
				hours = 0;
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						sh.callDay();
					}
				});
			}
			} catch(IllegalPluginAccessException e) {}
			
			try {
				Thread.currentThread();
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ticks++;
		}
	}
}
