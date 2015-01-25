package net.iubris.facri.utils;


public class Printer {

//	private static Console console;
	
//	public static void setConsole(Console console) {
////		Printer.console = console;
//	}
	
	public static void println(String string) {
//		console.printf(string+"\n");
		System.out.println(string);
	}
	public static void println(Object object) {
//		console.printf(object+"\n");
		System.out.println(object);
	}
	
	public static void print(String string) {
//		console.printf(string);
		System.out.print(string);
	}
	public static void print(Object object) {
//		console.printf(object.toString());
		System.out.print(object.toString());
	}
}
