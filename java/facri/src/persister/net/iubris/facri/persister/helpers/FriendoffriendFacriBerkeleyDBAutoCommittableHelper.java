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
package net.iubris.facri.persister.helpers;

import javax.inject.Inject;

import net.iubris.berkeley_persister.core.helper.base.utils.BerkeleyDBHelperManager;
import net.iubris.facri.model.parser.users.FriendOrAlike;
import net.iubris.facri.persister._di.annotations.PersisterPrefix;
import net.iubris.facri.persister.helpers.base.FacriBerkeleyDBAutoCommittableHelper;

import com.sleepycat.persist.model.EntityModel;

public class FriendoffriendFacriBerkeleyDBAutoCommittableHelper extends FacriBerkeleyDBAutoCommittableHelper<FriendOrAlike> {

	@Inject
	public FriendoffriendFacriBerkeleyDBAutoCommittableHelper(@PersisterPrefix String corpusName,
//			String silo,
			EntityModel entityModel,
			BerkeleyDBHelperManager berkeleyDBHelperManager) {
		super(corpusName, "friends_of_friends", entityModel, berkeleyDBHelperManager);
	}
}
