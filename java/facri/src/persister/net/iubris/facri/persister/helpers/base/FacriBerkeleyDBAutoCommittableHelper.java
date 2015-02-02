package net.iubris.facri.persister.helpers.base;

import net.iubris.berkeley_persister.core.helper.BerkeleyDBAutoCommittableHelper;
import net.iubris.berkeley_persister.core.helper.base.utils.BerkeleyDBHelperManager;
import net.iubris.facri.model.parser.users.User;

import com.sleepycat.persist.model.EntityModel;

public class FacriBerkeleyDBAutoCommittableHelper<U extends User> 
extends BerkeleyDBAutoCommittableHelper<String,U> 
implements FacriBerkeleyDBHelper<U> {

//	@Inject
	public FacriBerkeleyDBAutoCommittableHelper(String corpusName,
			String silo,
			EntityModel entityModel,
			BerkeleyDBHelperManager berkeleyDBHelperManager) {
		super(corpusName, silo, entityModel, berkeleyDBHelperManager);
	}
	
	@Override
	public void closeStorage() {
//		System.out.print("closing " + this.getClass().getName() + " storages ");
		super.closeStorage();
//		System.out.println(".");
	}
}