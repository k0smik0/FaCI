package net.iubris.facri.main;

import java.io.Console;

import net.iubris.facri._di.guice.module.parser.FacriModule;
import net.iubris.heimdall.InteractiveConsole;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestConsoleMain {
	private static final String NO_CONSOLE = "Error: Console unavailable";
	
	public static Injector injector;
//	private static final String TITLE = "FaCRI";

	public static void main(String[] args) {
		Console console = System.console();
		if (console != null) {
//			new HelpAction().exec(console, TITLE);
			
			injector = Guice.createInjector( new FacriModule() );
			
			InteractiveConsole interactiveConsole = injector.getInstance(InteractiveConsole.class);
			interactiveConsole.execCommandLoop(console);
		}
		else {
			throw new RuntimeException(NO_CONSOLE);
		}
	}

    /*private static boolean login(Console console) {
        console.printf(GREETINGS);

        boolean accessGranted = false;
        int attempts = 0;
        while (!accessGranted && attempts < 3) {
            String name = console.readLine(USER_PROMPT, new Date());
            char[] passdata = console.readPassword(PASS_PROMPT, new Date(), name);
            if (USER.equals(name) && PASS.equals(new String(passdata))) {
                attempts = 0;
                accessGranted = true;
                break;
            }
            console.printf(DENIED_ATTEMPT, ++attempts);
        }

        if (! accessGranted) {
            console.printf(ACCESS_DENIED);
            return false;
        }

        console.printf(ACCESS_GRANTED);
        return true;
    }*/
	
}
