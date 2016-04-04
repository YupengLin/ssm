package com.sessionmanagement;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Session implements Serializable {
	
	private String ID;
	private int version;
	private String sessionMessage;
	private Date lastActive;
	private int maxInterval;
	private Date expirationTime;
	private int rebootNum;
	private List<ServerID> location;
	
	public Session(){
		this.ID = UUID.randomUUID().toString();
		this.version = 1;
		this.sessionMessage = "Hello User";
		this.lastActive = new Date();
		this.maxInterval = 30; // 30 seconds
		this.expirationTime = new Date(this.lastActive.getTime() + 1000 * this.maxInterval);
		

	}
	
	public void incVersion() {
		this.version++;
	}
	
	public void resetVersion(){
		this.version = 1;
	}
	
	public String getID(){
		return this.ID;
	}
	
	public void ResetMessage(String message) {
		this.sessionMessage = message;
	}
	
	public int getVersion() {
		return version;
	}
	
	public String getMessage() {
		return sessionMessage;
	}
	
	public String getExpirationTime() {
		return this.expirationTime.toString();
	}
	
	public Date getLastActiveTime(){
		return this.lastActive;
	}
	
	public Date getExpirationInSecond(){
		return this.expirationTime;
	}
	
	public void setMaxInterval(int second) {
		this.maxInterval = second;
		
		this.expirationTime.setTime(this.lastActive.getTime() + maxInterval * 1000);
	}
	
	public void refreshTimeStamp(){
		lastActive = new Date();
		this.expirationTime.setTime(this.lastActive.getTime() + maxInterval * 1000);
	}
	/**
	 * 
	 */
	public void addLocation() {
		
	}
	/**
	 * 
	 */
	public void incRebootNum() {
		
	}
	public void resetRebootNum() {
		
	}
	
	public String extractInfo() {
		return this.ID + "_" + this.version + "_" + this.rebootNum;
	}
	
	
	
	
	
}
