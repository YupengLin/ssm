package RPC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.sessionmanagement.DataBrickManager;
import com.sessionmanagement.ServerID;
import com.sessionmanagement.Session;
import com.sessionmanagement.SessionManager;

public class RPCServer extends Thread {
	
	public void run() {
		
		
		DatagramSocket rpcSocket = null;
		try {
			rpcSocket = new DatagramSocket(RpcParameter.portPROJ1BRPC);
<<<<<<< HEAD
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DatagramPacket recvPkt = null;
		while(true) {
			try {
		
			byte[] receiveData = new byte[RpcParameter.sessionLength];
			byte[] sendData = new byte[RpcParameter.sessionLength];
				recvPkt = new DatagramPacket(receiveData, receiveData.length);
			
=======
			rpcSocket.setSoTimeout(3000);
		while(true) {
			byte[] receiveData = new byte[RpcParameter.sessionLength];
			byte[] sendData = new byte[RpcParameter.sessionLength];
			DatagramPacket recvPkt = new DatagramPacket(receiveData, receiveData.length);
			try{
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
				rpcSocket.receive(recvPkt);
				InetAddress returnAddr = recvPkt.getAddress();
				int returnPort = recvPkt.getPort();
				ServerID clientOrigin = new ServerID(returnAddr, returnPort);
<<<<<<< HEAD
			
				byte[] outBuf = computeResponseFromRequest(receiveData, clientOrigin);
				for(byte byteInfo : receiveData) {
					System.out.print(byteInfo);
				}
				DatagramPacket sendPkt = new DatagramPacket(outBuf,outBuf.length, returnAddr, returnPort);
				rpcSocket.send(sendPkt);
			
		
		
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
=======
		//	for(byte byteInfo : receiveData) {
		//		System.out.print(byteInfo);
	//		}
				byte[] outBuf = computeResponseFromRequest(receiveData, clientOrigin);
				DatagramPacket sendPkt = new DatagramPacket(outBuf,outBuf.length, returnAddr, returnPort);
				rpcSocket.send(sendPkt);
			}catch(SocketTimeoutException e){
				break;
			}
		}
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
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
<<<<<<< HEAD
		}
=======
		
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
	}
	
	private byte[] computeResponseFromRequest(byte[] input, ServerID clientOrigin) throws ClassNotFoundException, IOException, NullPointerException {
		String inputmessage = (String)RpcParameter.convertFromBytes(input);
		System.out.println("server receive : " + inputmessage);
		String[] tokens = inputmessage.split("_");
		/* Send message format
		 *  callID _ READ _ sessionKey
		 * */
		String callID = tokens[0];
		int operationCode = Integer.parseInt(tokens[1]);
		String replyMessage = "";
		switch(operationCode) {
			case RpcParameter.READ:
				System.out.println("server process read");
				Session retrivedSession = null;
				
<<<<<<< HEAD
				if(tokens.length == 6) {
=======
				if(tokens.length == 5) {
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
					
				
				String serverid = tokens[2];
				String rebootNum = tokens[3];
				String sessNum = tokens[4];
				String version = tokens[5];
				
				String key = serverid + "_" + rebootNum + "_" + sessNum + "_" + version;
<<<<<<< HEAD
				//System.out.println("search key assem in server:" + key);
				retrivedSession = SessionManager.findSessionByID(key);
				//System.out.println("retrived session info : " + retrivedSession.generateInfo());
=======
				retrivedSession = SessionManager.findSessionByID(key);
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
				String messageFromSession = "";
				replyMessage = callID;
				if(retrivedSession != null) {
					messageFromSession = retrivedSession.getMessage();
					replyMessage = callID + "_" + messageFromSession;
				}
<<<<<<< HEAD
				System.out.println("reply message in read:" + replyMessage);
=======
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
				return RpcParameter.convertToBytes(replyMessage);
				}
			break;
			case RpcParameter.WRITE:
				/* WRITE message format
				 *  callID _ WRITE _ sessionKey_message_discardTime
				 * */
<<<<<<< HEAD
				System.out.println("write the session");
=======
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
				String serverid = tokens[2];
				String rebootNum = tokens[3];
				String sessNum = tokens[4];
				String version = tokens[5];
				String clientMessage = tokens[6];
				String time = tokens[7];
				
				SessionManager.storeSession(serverid, rebootNum, sessNum, version, clientMessage, time, clientOrigin);
				replyMessage = callID  + "_" + DataBrickManager.getLocalServerID().toString();
<<<<<<< HEAD
				System.out.println("reply message in read:" + replyMessage);
=======
				
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
				return RpcParameter.convertToBytes(replyMessage);
				
			
			default:
			break;
			
		}
		
		return null;
	}

}