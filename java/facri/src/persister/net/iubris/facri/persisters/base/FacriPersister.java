package net.iubris.facri.persisters.base;

import net.iubris.berkeley_persister.core.Persister;
import net.iubris.facri.model.users.User;
import net.iubris.facri.persisters.helpers.base.FacriBerkeleyDBHelper;

public class FacriPersister<U extends User> extends Persister<String, U> {

	public FacriPersister(FacriBerkeleyDBHelper<U> berkeleyDBHelper, Class<U> valueClass) {
		super(berkeleyDBHelper, String.class, valueClass);
	}
}
