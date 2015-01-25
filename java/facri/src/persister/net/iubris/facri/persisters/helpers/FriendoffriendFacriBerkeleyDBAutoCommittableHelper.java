package net.iubris.facri.persisters.helpers;

import javax.inject.Inject;

import net.iubris.berkeley_persister.core.helper.base.utils.BerkeleyDBHelperManager;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.persisters._di.annotations.PersisterPrefix;
import net.iubris.facri.persisters.helpers.base.FacriBerkeleyDBAutoCommittableHelper;

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
