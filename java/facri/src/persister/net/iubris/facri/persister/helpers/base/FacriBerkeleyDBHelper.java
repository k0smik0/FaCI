package net.iubris.facri.persister.helpers.base;

import net.iubris.berkeley_persister.core.helper.base.BerkeleyDBHelper;
import net.iubris.facri.model.parser.users.User;

public interface FacriBerkeleyDBHelper<U extends User> extends BerkeleyDBHelper<String, U> {}
