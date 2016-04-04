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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.Cookie;

import com.sessionmanagement.DataBrickManager;
import com.sessionmanagement.ServerID;
import com.sessionmanagement.Session;

public class RPCClient {
	static final int F = 1;
	static final int R = F + 1;
	static final int WQ = R;
	static final int W = 2 * F + 1;
	static final int READ = 1;
	static final int WRITE = 1;
	
	public Cookie write(Session session) throws IOException, ClassNotFoundException {
		String callID = UUID.randomUUID().toString();
		String message = callID + "_"  + new Integer(WRITE).toString() + session.extractInfo();
		DatagramSocket rpcSocket = new DatagramSocket();
		rpcSocket.setSoTimeout(3000);
		byte[] encodeInfo = convertToBytes(message);
		final int[] numOfwrite = new Random().ints(0,DataBrickManager.getServerNum()).distinct().limit(W).toArray();
		List<ServerID> serverList = DataBrickManager.getServerID();
		List<ServerID> repliedBricks = new ArrayList<ServerID>();
		for(int index : numOfwrite){
			ServerID s = serverList.get(index);
	        DatagramPacket sendPkt = new DatagramPacket(encodeInfo, encodeInfo.length, s.getIP(), s.getPort());
	        rpcSocket.send(sendPkt);
	        
	        
		}
		session.clearLocation();
		byte[] inBuf = new byte[4096];
		while(repliedBricks.size() < WQ){
			DatagramPacket recvPkt = new DatagramPacket(inBuf, inBuf.length);
			rpcSocket.receive(recvPkt);
			String response = (String) convertFromBytes(inBuf);
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
	
	
	
	public DatagramPacket read(Session session){
		
		return null;
	}
	/**
	 * http://stackoverflow.com/a/30968827
	 * @return
	 */
	private byte[] convertToBytes(Object object) {
		 try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
		         ObjectOutput out = new ObjectOutputStream(bos)) {
		        out.writeObject(object);
		        return bos.toByteArray();
		    }  catch  (IOException  e){
		    	System.err.print(e);
		    }
		 return null;
	}
	/**
	http://stackoverflow.com/a/30968827
	*/
	private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
	    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	         ObjectInput in = new ObjectInputStream(bis)) {
	        return in.readObject();
	    } 
	}
	
}
