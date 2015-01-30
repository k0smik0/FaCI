package net.iubris.facri.persisters.specialized;

import javax.inject.Inject;

import net.iubris.facri.model.parser.users.FriendOrAlike;
import net.iubris.facri.persister.helpers.FriendoffriendFacriBerkeleyDBAutoCommittableHelper;
import net.iubris.facri.persister.persisters.base.FacriPersister;

public class FriendsoffriendsPersister extends FacriPersister<FriendOrAlike> {

	@Inject
	public FriendsoffriendsPersister(FriendoffriendFacriBerkeleyDBAutoCommittableHelper friendoffriendFacriBerkeleyDBAutoCommittableHelper/*, HelpersHolder helpersHolder*/) {
		super(friendoffriendFacriBerkeleyDBAutoCommittableHelper, FriendOrAlike.class/*, helpersHolder*/);
	}
}
