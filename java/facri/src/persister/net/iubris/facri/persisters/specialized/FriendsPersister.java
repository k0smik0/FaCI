package net.iubris.facri.persisters.specialized;

import javax.inject.Inject;

import net.iubris.facri.model.parser.users.FriendOrAlike;
import net.iubris.facri.persister.helpers.FriendFacriBerkeleyDBAutoCommittableHelper;
import net.iubris.facri.persister.persisters.base.FacriPersister;

public class FriendsPersister extends FacriPersister<FriendOrAlike> {

	@Inject
	public FriendsPersister(FriendFacriBerkeleyDBAutoCommittableHelper friendFacriBerkeleyDBAutoCommittableHelper/*, HelpersHolder helpersHolder*/) {
		super(friendFacriBerkeleyDBAutoCommittableHelper, FriendOrAlike.class/*, helpersHolder*/);
	}
}
