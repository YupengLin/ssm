package com.sessionmanagement;

import java.util.ArrayList;
import java.util.List;

public class DataBrickManager {
	
	public static boolean localTest = true;
	public static List<ServerID> getServerID(){
		if(localTest) {
			List<ServerID> localServer = new ArrayList<ServerID>();
			localServer.add(new ServerID("10.128.138.113:5300"));
			localServer.add(new ServerID("10.128.128.186:5300"));
			return localServer;
			
		}
		return null;
	}
	
	public static List<ServerID> getServerIdByNum(int num) {
		return null;
	}
	
	public static int getServerNum() {
		return -1;
	}
	
	public boolean addSimpleDB() {
		return false;
	}
	
	public static ServerID getLocalServerID() {
		if(localTest) {
			return new ServerID("10.128.128.186:5300");
		}
		return null;
	}
	

}
