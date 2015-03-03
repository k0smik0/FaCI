/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FriendoffriendFacriBerkeleyDBAutoCommittableHelper.java) is part of facri.
 * 
 *     FriendoffriendFacriBerkeleyDBAutoCommittableHelper.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FriendoffriendFacriBerkeleyDBAutoCommittableHelper.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.persister.helpers.specialized;

import javax.inject.Inject;

import net.iubris.faci.model.world.users.FriendOfFriend;
import net.iubris.faci.persister._di.annotations.PersisterPrefix;
import net.iubris.faci.persister.helpers.base.FaciBerkeleyDBAutoCommittableHelper;

public class FriendsoffriendsFaciBerkeleyDBAutoCommittableHelper extends FaciBerkeleyDBAutoCommittableHelper<FriendOfFriend> {

	@Inject
	public FriendsoffriendsFaciBerkeleyDBAutoCommittableHelper(@PersisterPrefix String corpusName
//			String silo,
			/*,EntityModel entityModel,
			BerkeleyDBHelperManager berkeleyDBHelperManager*/) {
		super(corpusName, "friends_of_friends"/*, entityModel, berkeleyDBHelperManager*/);
	}
}
