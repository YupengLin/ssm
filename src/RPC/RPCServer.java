package RPC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.sessionmanagement.Session;
import com.sessionmanagement.SessionManager;

public class RPCServer extends Thread {
	
	public void run() {
		
		
		DatagramSocket rpcSocket;
		try {
			rpcSocket = new DatagramSocket(RpcParameter.portPROJ1BRPC);
		
		while(true) {
			byte[] receiveData = new byte[4096];
			byte[] sendData = new byte[4096];
			DatagramPacket recvPkt = new DatagramPacket(receiveData, receiveData.length);
			rpcSocket.receive(recvPkt);
			InetAddress returnAddr = recvPkt.getAddress();
			int returnPort = recvPkt.getPort();
			byte[] outBuf = computeResponseFromRequest(sendData);
			DatagramPacket sendPkt = new DatagramPacket(outBuf,outBuf.length, returnAddr, returnPort);
			rpcSocket.send(sendPkt);
			
		}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	private byte[] computeResponseFromRequest(byte[] input) throws ClassNotFoundException, IOException {
		String inputmessage = (String)RpcParameter.convertFromBytes(input);
		String[] tokens = inputmessage.split("_");
		int operationCode = Integer.parseInt(tokens[1]);
		switch(operationCode) {
			case RpcParameter.READ:
				Session retrivedSession = null;
				String replyMessage = "";
				if(tokens.length == 5) {
					
				
				String serverid = tokens[2];
				String rebootNum = tokens[3];
				String sessNum = tokens[4];
				String version = tokens[5];
				
				String key = serverid + rebootNum + sessNum + version;
				retrivedSession = SessionManager.findSessionByID(key);
				
				}
			break;
			case RpcParameter.WRITE:
				
			break;
			default:
			break;
			
		}
		
		return null;
	}

}
