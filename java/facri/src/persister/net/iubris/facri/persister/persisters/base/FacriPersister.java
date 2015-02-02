/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FacriPersister.java) is part of facri.
 * 
 *     FacriPersister.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FacriPersister.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.persister.persisters.base;

import net.iubris.berkeley_persister.core.Persister;
import net.iubris.berkeley_persister.core.helper.base.BerkeleyDBHelper;
import net.iubris.facri.console.Console;
import net.iubris.facri.model.parser.users.User;
import net.iubris.facri.persister.helpers.base.FacriBerkeleyDBHelper;

public class FacriPersister<U extends User> extends Persister<String, U> {
	
//	@Inject
//	private final HelpersHolder helpersHolder;

	public FacriPersister(FacriBerkeleyDBHelper<U> berkeleyDBHelper, Class<U> valueClass/*, HelpersHolder helpersHolder*/) {
		super(berkeleyDBHelper, String.class, valueClass);
//		System.out.println("Persister!");
//		this.helpersHolder = helpersHolder;
//		this.helpersHolder.addHelper(berkeleyDBHelper);
		
//		helpersHolder.getHelpers().stream().forEach(h-> {
//			preExits.add( h::closeStorage);
			
		Console.interactiveConsole.addPreExit( berkeleyDBHelper::closeStorage );
	}
	
	public BerkeleyDBHelper<String, U> getHelper() {
		return berkeleyDBHelper;
	}
	
	/*@Singleton
	@SuppressWarnings("rawtypes")
	public static class HelpersHolder {
		private Set<FacriBerkeleyDBHelper> helpers = new HashSet<>();
		
		public Set<FacriBerkeleyDBHelper> getHelpers() {
			return helpers;
		}
		public void addHelper(FacriBerkeleyDBHelper facriBerkeleyDBHelper) {
			System.out.println("adding ");
			this.helpers.add(facriBerkeleyDBHelper);
		}
	}*/
}
