package net.iubris.facri.persisters.base;

import net.iubris.berkeley_persister.core.Persister;
import net.iubris.berkeley_persister.core.helper.base.BerkeleyDBHelper;
import net.iubris.facri.model.users.User;

public class FacriPersister<U extends User> extends Persister<String, U> {

	public FacriPersister(BerkeleyDBHelper<String, U> berkeleyDBHelper, Class<U> valueClass) {
		super(berkeleyDBHelper, String.class, valueClass);
	}
}
