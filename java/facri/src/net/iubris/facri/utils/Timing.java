package net.iubris.facri.utils;

import java.util.Date;

public class Timing {

	private final Date start;
	
	public Timing() {
		start = new Date();
	}
	
	public double getTiming() {
		Date end = new Date();
		double finish = (end.getTime()-start.getTime())/1000f;
		return finish;
	}
}
