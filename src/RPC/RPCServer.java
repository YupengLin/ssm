package RPC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.sessionmanagement.DataBrickManager;
import com.sessionmanagement.ServerID;
import com.sessionmanagement.Session;
import com.sessionmanagement.SessionManager;

public class RPCServer extends Thread {
	
	public void run() {
		
		
		DatagramSocket rpcSocket;
		try {
			rpcSocket = new DatagramSocket(RpcParameter.portPROJ1BRPC);
		
		while(true) {
			byte[] receiveData = new byte[RpcParameter.sessionLength];
			byte[] sendData = new byte[RpcParameter.sessionLength];
			DatagramPacket recvPkt = new DatagramPacket(receiveData, receiveData.length);
			rpcSocket.receive(recvPkt);
			InetAddress returnAddr = recvPkt.getAddress();
			int returnPort = recvPkt.getPort();
			ServerID clientOrigin = new ServerID(returnAddr, returnPort);
			byte[] outBuf = computeResponseFromRequest(sendData, clientOrigin);
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
	
	private byte[] computeResponseFromRequest(byte[] input, ServerID clientOrigin) throws ClassNotFoundException, IOException, NullPointerException {
		String inputmessage = (String)RpcParameter.convertFromBytes(input);
		String[] tokens = inputmessage.split("_");
		/* Send message format
		 *  callID _ READ _ sessionKey
		 * */
		String callID = tokens[0];
		int operationCode = Integer.parseInt(tokens[1]);
		String replyMessage = "";
		switch(operationCode) {
			case RpcParameter.READ:
				Session retrivedSession = null;
				
				if(tokens.length == 5) {
					
				
				String serverid = tokens[2];
				String rebootNum = tokens[3];
				String sessNum = tokens[4];
				String version = tokens[5];
				
				String key = serverid + "_" + rebootNum + "_" + sessNum + "_" + version;
				retrivedSession = SessionManager.findSessionByID(key);
				String messageFromSession = "";
				replyMessage = callID;
				if(retrivedSession != null) {
					messageFromSession = retrivedSession.getMessage();
					replyMessage = callID + "_" + messageFromSession;
				}
				return RpcParameter.convertToBytes(replyMessage);
				}
			break;
			case RpcParameter.WRITE:
				/* WRITE message format
				 *  callID _ WRITE _ sessionKey_message_discardTime
				 * */
				String serverid = tokens[3];
				String rebootNum = tokens[4];
				String sessNum = tokens[5];
				String version = tokens[6];
				String clientMessage = tokens[7];
				String time = tokens[8];
				
				SessionManager.storeSession(serverid, rebootNum, sessNum, version, clientMessage, time, clientOrigin);
				replyMessage = callID  + "_" + DataBrickManager.getLocalServerID().toString();
				
				return RpcParameter.convertToBytes(replyMessage);
				
			
			default:
			break;
			
		}
		
		return null;
	}

}
