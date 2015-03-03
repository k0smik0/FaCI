/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FriendsoffriendsPersister.java) is part of facri.
 * 
 *     FriendsoffriendsPersister.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FriendsoffriendsPersister.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.persisters.specialized;

import javax.inject.Inject;

import net.iubris.faci.model.world.users.GenericUser;
import net.iubris.faci.persister.helpers.specialized.OtherUsersFaciBerkeleyDBAutoCommittableHelper;
import net.iubris.faci.persister.persisters.base.FaciPersister;

public class OtherUsersPersister extends FaciPersister<GenericUser> {

	@Inject
	public OtherUsersPersister(OtherUsersFaciBerkeleyDBAutoCommittableHelper otherusersFaciBerkeleyDBAutoCommittableHelper) {
		super(otherusersFaciBerkeleyDBAutoCommittableHelper, GenericUser.class);
	}
}
