package net.iubris.facri.utils;

public class Pauser {
	
	public static void sleep(int ms) {
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
