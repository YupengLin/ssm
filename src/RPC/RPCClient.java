package RPC;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.UUID;

import javax.servlet.http.Cookie;

import com.sessionmanagement.Session;

public class RPCClient {
	static final int F = 1;
	static final int R = F + 1;
	static final int WQ = R;
	static final int W = 2 * F + 1;
	
	public Cookie write(Session session) {
		String callID = UUID.randomUUID().toString();
		String message = callID + "_" + session.extractInfo();
		byte[] encodeInfo = convertToBytes(message);
		
		
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
	
}
