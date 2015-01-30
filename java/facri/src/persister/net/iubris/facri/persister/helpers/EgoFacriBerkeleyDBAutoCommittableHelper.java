package net.iubris.facri.persister.helpers;

import javax.inject.Inject;

import net.iubris.berkeley_persister.core.helper.base.utils.BerkeleyDBHelperManager;
import net.iubris.facri.model.parser.users.Ego;
import net.iubris.facri.persister._di.annotations.PersisterPrefix;
import net.iubris.facri.persister.helpers.base.FacriBerkeleyDBAutoCommittableHelper;

import com.sleepycat.persist.model.EntityModel;

public class EgoFacriBerkeleyDBAutoCommittableHelper extends FacriBerkeleyDBAutoCommittableHelper<Ego> {

	@Inject
	public EgoFacriBerkeleyDBAutoCommittableHelper(@PersisterPrefix String corpusName, 
//			String silo, 
			EntityModel entityModel,
			BerkeleyDBHelperManager berkeleyDBHelperManager) {
		super(corpusName, "ego", entityModel, berkeleyDBHelperManager);
	}
}
