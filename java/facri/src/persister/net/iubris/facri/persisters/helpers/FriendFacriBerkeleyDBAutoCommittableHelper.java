package net.iubris.facri.persisters.helpers;

import javax.inject.Inject;

import net.iubris.berkeley_persister.core.helper.base.utils.BerkeleyDBHelperManager;
import net.iubris.facri._di.annotations.grapher.corpus.CorpusPrefix;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.persisters.helpers.base.FacriBerkeleyDBAutoCommittableHelper;

import com.sleepycat.persist.model.EntityModel;

public class FriendFacriBerkeleyDBAutoCommittableHelper extends FacriBerkeleyDBAutoCommittableHelper<FriendOrAlike> {

	@Inject
	public FriendFacriBerkeleyDBAutoCommittableHelper(@CorpusPrefix String corpusName,
//			String silo,
			EntityModel entityModel,
			BerkeleyDBHelperManager berkeleyDBHelperManager) {
		super(corpusName, "friends", entityModel, berkeleyDBHelperManager);
	}
}
