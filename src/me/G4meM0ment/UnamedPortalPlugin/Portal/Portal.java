package me.G4meM0ment.UnamedPortalPlugin.Portal;

import org.bukkit.Location;

public class Portal {

	private Location max, min, destination;
	private String id;
	
	public Portal(String id, Location max, Location min, Location destination) {
		this.id = id;
		this.max = max;
		this.min = min;
		this.destination = destination;
	}
	
	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}
	
	public Location getMax() {
		return max;
	}
	public void setMax(Location max) {
		this.max = max;
	}

	public Location getMin() {
		return min;
	}
	public void setMin(Location min) {
		this.min = min;
	}

	public Location getDestination() {
		return destination;
	}
	public void setDestination(Location destination) {
		this.destination = destination;
	}
}
