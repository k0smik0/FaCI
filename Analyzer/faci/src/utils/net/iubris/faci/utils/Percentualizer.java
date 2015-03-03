package net.iubris.faci.utils;

public class Percentualizer {

	private final int total;
	
	private int percentCounter;
	private int counter = 0;
	private int actualToPrint = 0;
	
	public Percentualizer(int total) {
		this.total = total;
	}

	public void printPercentual() {
		counter++;
		double percent = Math.ceil( counter*1.0f/total*100 );
		if (Math.floor(percent)%10 == 0) {
			percentCounter++;
			if (percentCounter==1) {
				int toPrint = (int)percent;
				if (toPrint != actualToPrint) {
					actualToPrint  = toPrint;
					Printer.print(actualToPrint);
					if (toPrint<100) {
						Printer.print("%... ");					
					} else
						Printer.print("%");
				}
			}
			if (percentCounter>9)
				percentCounter=0;
		}
	}
}
