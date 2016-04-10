package com.sessionmanagement;

import java.io.Serializable;
import java.util.ArrayList;
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
	private int sessionNumber;
	private List<ServerID> location;
	private ServerID localAddress;
	
	public Session(ServerID localAddress, int rebootNum, int sessionNum){
		this.ID = localAddress.toString();
		this.version = 1;
		this.sessionMessage = "Hello User";
		this.lastActive = new Date();
		this.maxInterval = 30; // 30 seconds
		this.expirationTime = new Date(this.lastActive.getTime() + 1000 * this.maxInterval);
		this.localAddress = localAddress;
		this.rebootNum = rebootNum;
		this.sessionNumber = sessionNum;
	}
	
	public Session(ServerID localServerID, int rebootNum, int sessionNum, int version, List<ServerID> targetAddress) {
		this.localAddress = localServerID;
		this.ID = localServerID.toString();
		this.rebootNum = rebootNum;
		this.sessionNumber = sessionNum;
		this.version = version;
		try{
			this.location.addAll(targetAddress);
		}catch(NullPointerException e){
			this.location = null;
		}
	}
	
	public Session(ServerID localServerID, int rebootNum, int sessionNum, int version) {
		this(localServerID, rebootNum, sessionNum);
		this.version = version;
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
	
	public void setExpirationTime(Date time){
		this.expirationTime = time;
	}
	public void addLocation(ServerID serverId) {
		this.location.add(serverId);
		
	}
	public void clearLocation() {
		this.location.clear();
	}
	public void updateLocationByOnce(List<ServerID> newLocation) {
		this.location.clear();
		this.location.addAll(newLocation);
	}
	/**
	 * 
	 */
	public void incRebootNum() {
		this.rebootNum += 1;
	}
	
	public void setRebootNum(int reboot) {
		this.rebootNum = reboot;
	}
	
	public void resetRebootNum() {
		this.rebootNum = 0;
	}
	
	
	
	public String sessionID() {
		return this.ID + "_" + this.rebootNum + "_" + this.sessionNumber;
	}
	
	public String generateSessionKey(){
		return this.sessionID() + "_" + this.version;
	}
	
	public List<ServerID> getLocation(){
		return this.location;
	}
	
	public void incSessionNumber(){
		this.sessionNumber += 1;
	}
	
	public String generateInfo() {
		StringBuilder sessionKey = new StringBuilder();
		sessionKey.append(ID);
		sessionKey.append("_");
		
		sessionKey.append(this.rebootNum);
		sessionKey.append("_");
		
		sessionKey.append(this.sessionNumber);
		sessionKey.append("_");
		
	    sessionKey.append(this.version);
	    sessionKey.append("_");
	    try{
	    	for(ServerID sid : location) {
	    		sessionKey.append(sid.toString());
	    		sessionKey.append("_");
	    	}
	    }
	    catch(NullPointerException e){
	    	e.printStackTrace();
	    }
	    String res = sessionKey.toString();
	    int len = res.length();
	    return res.substring(0, len-1);
	}
	
	
	
	
}