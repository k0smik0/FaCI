package net.iubris.facri.persisters;

import javax.inject.Inject;

import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.persisters.base.FacriPersister;
import net.iubris.facri.persisters.helpers.FriendFacriBerkeleyDBAutoCommittableHelper;

public class FriendsPersister extends FacriPersister<FriendOrAlike> {

	@Inject
	public FriendsPersister(FriendFacriBerkeleyDBAutoCommittableHelper friendFacriBerkeleyDBAutoCommittableHelper) {
		super(friendFacriBerkeleyDBAutoCommittableHelper, FriendOrAlike.class);
	}
}
