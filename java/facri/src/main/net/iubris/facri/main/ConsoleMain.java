/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (ConsoleMain.java) is part of facri.
 * 
 *     ConsoleMain.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     ConsoleMain.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
