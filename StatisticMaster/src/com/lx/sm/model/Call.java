package com.lx.sm.model;

public class Call {
	
	private String name;
	private String number;
	private long date;
	private int type;
	private int   duration;
	private long ID;
	
	public void setID(long id){
		this.ID = id;
	}
	public long getID(){
		return this.ID;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
	
	public String getNumber(){
		return this.number;
	}
	
	public void setDate(long date){
		this.date = date;
	}
	
	public long getDate(){
		return this.date;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
	
	public void setDuration(int duration){
		this.duration = duration;
	}
	
	public int getDuration(){
		return this.duration;
	}

}
