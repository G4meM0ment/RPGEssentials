package me.G4meM0ment.DeathAndRebirth.DataStorage;

public enum Message {
	
	died						("You've died and become a ghost"),
	resurrected					("You've been resurrected");
	
	private String msg;
	
	Message(String msg)
	{
		this.msg = msg;
	}
	
	public String msg() 
	{
		return msg;
	}
	
	public void loadMessages()
	{
		
	}
}
