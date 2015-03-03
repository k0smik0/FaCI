/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FriendsPersister.java) is part of facri.
 * 
 *     FriendsPersister.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FriendsPersister.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.persisters.specialized;

import javax.inject.Inject;

import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.persister.helpers.specialized.FriendFaciBerkeleyDBAutoCommittableHelper;
import net.iubris.faci.persister.persisters.base.FaciPersister;

public class FriendsPersister extends FaciPersister<Friend> {

	@Inject
	public FriendsPersister(FriendFaciBerkeleyDBAutoCommittableHelper friendFaciBerkeleyDBAutoCommittableHelper) {
		super(friendFaciBerkeleyDBAutoCommittableHelper, Friend.class);
	}
}
