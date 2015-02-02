/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Printer.java) is part of facri.
 * 
 *     Printer.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Printer.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
