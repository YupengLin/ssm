package test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	}

}
