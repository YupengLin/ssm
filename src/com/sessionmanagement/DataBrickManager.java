package com.sessionmanagement;

import java.util.ArrayList;
import java.util.List;

public class DataBrickManager {
	
	
	public static boolean localTest = true;
	public static List<ServerID> getServerID(){
		if(localTest) {
			List<ServerID> localServer = new ArrayList<ServerID>();
			localServer.add(new ServerID("127.0.0.1:5300"));
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
			return new ServerID("127.0.0.1:5300");
		}
		return null;
	}
	

}
