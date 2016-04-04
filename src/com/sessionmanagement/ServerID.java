package com.sessionmanagement;

import java.net.InetAddress;

public class ServerID {
	private InetAddress ip;
	private int port;
	
	public ServerID(InetAddress ipaddress, int port) {
		this.ip = ipaddress;
		this.port = port;
	}
	public InetAddress getIP(){
		return ip;
	}
	
	public int getPort(){
		return port;
	}
	
	
}
