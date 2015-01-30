package net.iubris.facri.persister.helpers;

import javax.inject.Inject;

import net.iubris.berkeley_persister.core.helper.base.utils.BerkeleyDBHelperManager;
import net.iubris.facri.model.parser.users.FriendOrAlike;
import net.iubris.facri.persister._di.annotations.PersisterPrefix;
import net.iubris.facri.persister.helpers.base.FacriBerkeleyDBAutoCommittableHelper;

import com.sleepycat.persist.model.EntityModel;

public class FriendFacriBerkeleyDBAutoCommittableHelper extends FacriBerkeleyDBAutoCommittableHelper<FriendOrAlike> {

	@Inject
	public FriendFacriBerkeleyDBAutoCommittableHelper(@PersisterPrefix String corpusName,
//			String silo,
			EntityModel entityModel,
			BerkeleyDBHelperManager berkeleyDBHelperManager) {
		super(corpusName, "friends", entityModel, berkeleyDBHelperManager);
	}
}
