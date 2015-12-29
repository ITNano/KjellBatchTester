package model;

public enum HappeningType{
	ARRIVE,
	TRANSFER,
	LEAVE,
	LEFT;
	
	public static HappeningType getType(String str){
		switch(str){
			case "Arrive":		return ARRIVE;
			case "Transfer":	return TRANSFER;
			case "Leave":		return LEAVE;
			case "Left":		return LEFT;
			default:			throw new IllegalArgumentException("String does not match any type: "+str);
		}
	}
}