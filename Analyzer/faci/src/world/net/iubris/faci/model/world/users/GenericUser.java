package net.iubris.faci.model.world.users;

import com.sleepycat.persist.model.Entity;

@Entity
public class GenericUser extends AbstractUser {
	private static final long serialVersionUID = -87924035343958236L;

	GenericUser() {}

	public GenericUser(String uid) {
		super(uid);
	}
}
