package me.G4meM0ment.Orbia.Framework;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
 
public class StringMetadata implements MetadataValue{
 
	private Object value;
	private Plugin p;
 
	public StringMetadata(Plugin p, Object o) {
		this.p = p;
		this.value = o;
	}
 
	@Override
	public boolean equals(Object obj) {
		return value.equals(obj);
	}
 
	public void set(Object o) {
		this.value = o;
	}
 
	@Override
	public boolean asBoolean() {
		throw new NullPointerException();
	}
 
	@Override
	public byte asByte() {
		throw new NullPointerException();
	}
 
	@Override
	public double asDouble() {
		throw new NullPointerException();
	}
 
	@Override
	public float asFloat() {
		throw new NullPointerException();
	}
 
	@Override
	public int asInt() {
		throw new NullPointerException();
	}
 
	@Override
	public long asLong() {
		throw new NullPointerException();
	}
 
	@Override
	public short asShort() {
		throw new NullPointerException();
		}
 
	@Override
	public String asString() {
	throw new NullPointerException();
	}
 
	@Override
	public Plugin getOwningPlugin() {
		return p;
	}
 
	@Override
	public void invalidate() {
		return;
	}
 
	@Override
	public Object value() {
		return value;
	}
 
}
