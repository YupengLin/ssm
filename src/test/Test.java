package test;

import java.util.Date;

public class Test {
	public static void main(String[] args) {
		Date curr = new Date();
		System.out.println(curr.toString());
		curr.setTime(curr.getTime() + 5000);
		System.out.println(curr.toString());
	}

}
