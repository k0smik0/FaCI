/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FacriBerkeleyDBAutoCommittableHelper.java) is part of facri.
 * 
 *     FacriBerkeleyDBAutoCommittableHelper.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FacriBerkeleyDBAutoCommittableHelper.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.persister.helpers.base;

import net.iubris.faci.model.world.users.User;
import net.iubris.jbbp.core.helper.BerkeleyDBAutoCommittableHelper;

public class FaciBerkeleyDBAutoCommittableHelper<U extends User> 
extends BerkeleyDBAutoCommittableHelper<String,U> 
implements FaciBerkeleyDBHelper<U> {

	public FaciBerkeleyDBAutoCommittableHelper(String corpusName, String silo) {
		super(corpusName, silo);
	}
	
	/*@Override
	public void closeStorage() {
//		System.out.print("closing " + this.getClass().getName() + " storages ");
		super.closeStorage();
//		System.out.println(".");
	}*/
}
