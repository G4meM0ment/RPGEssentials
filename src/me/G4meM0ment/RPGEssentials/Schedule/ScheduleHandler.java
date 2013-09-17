package me.G4meM0ment.RPGEssentials.Schedule;

public class ScheduleHandler {
	
	private static int seconds;
	private static int minutes;
	private static int hours;
	private static int days;
	
	public ScheduleHandler() {
	}
	
	public void callSecond() {
		if(seconds >= 60)
			seconds = 0;
		seconds++;

	}

	public void callMinute() {
		if(minutes >= 60)
			minutes = 0;
		minutes++;

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
