/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Command.java) is part of facri.
 * 
 *     Command.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Command.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.console;

import java.io.Console;
import java.util.List;

import net.iubris.facri.console.actions.QuitAction;
import net.iubris.heimdall.actions.Action;
import net.iubris.heimdall.listeners.ExceptionListener;

public enum Command {
	q(new QuitAction()),
	h(new Action() {
		@Override
		public void exec(Console console, List<String> params) throws Exception {
			String newLine = "\n";
			String quitCommandChar = "q";
			String helpCommandChar = "h";
			String analyzeCommandChar = "a";
			
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder
				.append("Facri: available commands:").append(newLine)
					.append(tab(1)).append("'"+quitCommandChar+"': exit").append(newLine)
					.append(tab(1)).append("'"+helpCommandChar+"': display this help").append(newLine)
					.append(tab(1)).append("'"+analyzeCommandChar+"': analyze [world] [analysis type]").append(newLine)
					.append(tab(2)).append("analyze command needs two arguments:").append(newLine)
					.append(tab(2)).append("first argument select 'world' to analyze:").append(newLine)
					.append(tab(3)).append("'f': analyze friendships 'world'").append(newLine)
					.append(tab(3)).append("'i': analyze interactions 'world'").append(newLine)
					.append(tab(2)).append("second argument select analysis type:").append(newLine)
					.append(tab(3)).append("'mf': me and my friends").append(newLine)
					.append(tab(3)).append("'ft': my friends and their friends (friends of friends)").append(newLine)
					.append(tab(3)).append("'tt': friends of my friends").append(newLine)
					.append(tab(3)).append("'mft': me, my friends, their friends").append(newLine)
					.append(tab(1)).append("example: 'a i mf': analyze interactions between me and my friends").append(newLine);
			
			console.printf(stringBuilder.toString());
		}
		private String tab(int many) {
			String tab = "";
			for (int i=1;i<=many;i++) {
				tab+="\t";
			}
			return tab;
		}
	})
	,a(new Action() {
		@Override
		public void exec(Console console, List<String> params) throws Exception {
			
		}
	})
	,DETAILS(new Action() {
		@Override
		public void exec(Console c, List<String> params) throws Exception {
			int detailsLevel = 1;
			try {
				detailsLevel = Integer.parseInt(params.get(0));
			}
			catch (NumberFormatException e) {
				// ignore
			}

			for (int i = 1; i <= detailsLevel; i++) {
				c.printf("Detail number %1$X%n", i);
			}
		}
	});

	private Action action;

	private Command(Action a) {
		this.action = a;
	}

	public void exec(final Console console, final List<String> params, final ExceptionListener listener) {
		try {
			action.exec(console, params);
		} catch (Exception e) {
			listener.handleException(e);
		}
	}
	public void exec(final Console console, final ExceptionListener listener) {
		try {
			action.exec(console,null);
		} catch (Exception e) {
			listener.handleException(e);
		}
	}
}
