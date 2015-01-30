package net.iubris.facri.main;

import java.io.Console;

import net.iubris.facri._di.guice.module.main.FacriModule;
import net.iubris.heimdall.InteractiveConsole;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ConsoleMain {
	private static final String NO_CONSOLE = "Error: Console unavailable";
	
	public static Injector injector;
//	private static final String TITLE = "FaCRI";
	
//	public static InteractiveConsole interactiveConsole; 

	public static void main(String[] args) {
		Console console = System.console();
		if (console != null) {
//			new HelpAction().exec(console, TITLE);
//			Printer.setConsole(console);
			
			console.printf("loading...");
			injector = Guice.createInjector( new FacriModule() );
			
			net.iubris.facri.console.Console.interactiveConsole = injector.getInstance(InteractiveConsole.class);
			console.printf(" done\n");
			net.iubris.facri.console.Console.interactiveConsole.execCommandLoop(console);
		}
		else {
			throw new RuntimeException(NO_CONSOLE);
		}
	}
	
}
