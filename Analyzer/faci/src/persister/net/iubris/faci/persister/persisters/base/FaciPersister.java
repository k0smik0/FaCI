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
package net.iubris.faci.persister.persisters.base;

import net.iubris.faci.console.Console;
import net.iubris.faci.model.world.users.User;
import net.iubris.faci.persister.helpers.base.FaciBerkeleyDBHelper;
import net.iubris.jbbp.core.PersisterAutoCommittable;

public class FaciPersister<U extends User> extends PersisterAutoCommittable<String, U> {
	
	public FaciPersister(FaciBerkeleyDBHelper<U> berkeleyDBHelper, Class<U> valueClass) {
		super(berkeleyDBHelper, String.class, valueClass);
		Console.interactiveConsole.addPreExit( berkeleyDBHelper::closeStorage );
	}
}
