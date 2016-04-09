package test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import RPC.RpcParameter;

public class Test {
	public static void main(String[] args) {
		Date curr = new Date();
		System.out.println(curr.toString());
		//curr.setTime(curr.getTime() + 5000);
		//System.out.println(curr.toString());
		
		DateFormat dateFormatter = new SimpleDateFormat ( "E MMM dd HH:mm:ss Z yyyy" );
		String d = curr.toString();
		try {
			
			System.out.println(dateFormatter.parse(d).toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String hello = "hello";
		byte[] byteInfo = RpcParameter.convertToBytes(hello);
		
		try {
			String back = (String) RpcParameter.convertFromBytes(byteInfo);
			System.out.println(back);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
