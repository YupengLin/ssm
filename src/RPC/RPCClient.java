package RPC;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.Cookie;

import com.sessionmanagement.DataBrickManager;
import com.sessionmanagement.ServerID;
import com.sessionmanagement.Session;

public class RPCClient  {
	
	
	public Cookie write(Session session) throws IOException, ClassNotFoundException {
		String callID = UUID.randomUUID().toString();
		String message = callID + "_"  + new Integer(RpcParameter.WRITE).toString() + session.extractInfo();
		DatagramSocket rpcSocket = new DatagramSocket();
		rpcSocket.setSoTimeout(3000);
		byte[] encodeInfo = RpcParameter.convertToBytes(message);
		final int[] numOfwrite = new Random().ints(0,DataBrickManager.getServerNum()).distinct().limit(RpcParameter.W).toArray();
		List<ServerID> serverList = DataBrickManager.getServerID();
		List<ServerID> repliedBricks = new ArrayList<ServerID>();
		for(int index : numOfwrite){
			ServerID s = serverList.get(index);
	        DatagramPacket sendPkt = new DatagramPacket(encodeInfo, encodeInfo.length, s.getIP(), s.getPort());
	        rpcSocket.send(sendPkt);
	        
	        
		}
		session.clearLocation();
		byte[] inBuf = new byte[4096];
		while(repliedBricks.size() < RpcParameter.WQ){
			DatagramPacket recvPkt = new DatagramPacket(inBuf, inBuf.length);
			rpcSocket.receive(recvPkt);
			String response = (String) RpcParameter.convertFromBytes(inBuf);
		    String[] decodeInfo = response.split("_");
		    String returnID = decodeInfo[0];
		    if(returnID.equals(callID)) {
		    	
		    	int portNumber = Integer.parseInt(decodeInfo[2]);
		    	InetAddress  ipAddress = InetAddress.getByName(decodeInfo[1]);
		    	ServerID locationInfo = new ServerID(ipAddress, portNumber);
		    	repliedBricks.add(locationInfo);
		    }
			
		}
		session.updateLocationByOnce(serverList);
		rpcSocket.close();
		return null;
	}
	
	
	/* UDP message format
	 * callerID, operation code, operation code argument
	 * 
	 * */
	
	public Session read(Session session) throws IOException, ClassNotFoundException, EmptyBodyException, NullPointerException{
		String callID = UUID.randomUUID().toString();
		
		String queryMessage = "";
		queryMessage += callID;
		queryMessage += "_";
		queryMessage += new Integer(RpcParameter.READ).toString();
		queryMessage += "_";
		queryMessage += session.generateSessionKey();
		
		DatagramSocket rpcSocket = new DatagramSocket();
		byte[] encodeInfo = RpcParameter.convertToBytes(queryMessage);
		List<ServerID> locations = session.getLocation();
		
		for(ServerID server : locations) {
			DatagramPacket sendPacket = new DatagramPacket(encodeInfo, encodeInfo.length, server.getIP(), server.getPort());
			rpcSocket.send(sendPacket);
		}
		
		byte[] inBuf = new byte[4096];
		DatagramPacket recvPkt = new DatagramPacket(inBuf, inBuf.length);
		rpcSocket.receive(recvPkt);
		String response = (String) RpcParameter.convertFromBytes(inBuf);
		String[] decodeInfo = response.split("_");
		if(response == "" || response == null || !decodeInfo[0].equals(callID)) {
			throw new EmptyBodyException("empty");
		}
		int version = Integer.parseInt(decodeInfo[1]);
		int rebootNum = Integer.parseInt(decodeInfo[2]);
		String sessionMessage = decodeInfo[3];
		
		session.ResetMessage(sessionMessage);
		
		
		
		return session;
	}
	
	public Session read(boolean[] flag, String[] tokens) {
		 //  svrID_rebootNum_sessNum_version_S1_S2 ... Swq
		try {
			ServerID serverID = new ServerID(tokens[0]);
			int rebootNum = Integer.parseInt(tokens[1]);
			int sessionNum = Integer.parseInt(tokens[2]);
			int version = Integer.parseInt(tokens[3]);
			List<ServerID> answeredServerID = new ArrayList<>();
			for(int i = 4; i < 4 + RpcParameter.WQ; i++) {
				answeredServerID.add(new ServerID(tokens[i]));
			}
			Session sessionToBeRead = new Session(serverID, rebootNum, sessionNum, version, answeredServerID);
			this.read(sessionToBeRead);
		
		
		} catch (CorruptedCookieInfoException e) {
			flag[0] = false;
		} catch (ClassNotFoundException e) {
			flag[0] = false;
			e.printStackTrace();
		} catch (IOException e) {
			flag[0] = false;
			e.printStackTrace();
		} catch (EmptyBodyException e) {
			flag[0] = false;
			e.printStackTrace();
		}
		
		
		
		
		
		
		return null;
	}
	

	public void writeTo(Session session, String version, String data, Date discardTime) {
		
	}
	
	public class CorruptedCookieInfoException extends NullPointerException {
		
	}
	
	public class EmptyBodyException extends Exception {
		public EmptyBodyException(String error) {
			super(error);
		}
	}

	
}
