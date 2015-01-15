package net.iubris.facri.persisters;

import javax.inject.Inject;

import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.persisters.base.FacriPersister;
import net.iubris.facri.persisters.helpers.FriendoffriendFacriBerkeleyDBAutoCommittableHelper;

public class FriendsoffriendsPersister extends FacriPersister<FriendOrAlike> {

	@Inject
	public FriendsoffriendsPersister(FriendoffriendFacriBerkeleyDBAutoCommittableHelper friendoffriendFacriBerkeleyDBAutoCommittableHelper) {
		super(friendoffriendFacriBerkeleyDBAutoCommittableHelper, FriendOrAlike.class);
	}
}
