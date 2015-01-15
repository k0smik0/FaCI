package net.iubris.facri.console.actions.graph.utils.cache.persister;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.iubris.berkeley_persister.dao.base.exception.ExistantValueException;
import net.iubris.facri.console.actions.graph.utils.cache.persister.exception.PersisterException;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.User;
import net.iubris.facri.model.world.World;
import net.iubris.facri.persisters.EgoPersister;

public class WorldPersisterService {

	private final EgoPersister egoPersister;
//	private final FriendsPersister friendsPersister;
//	private final FriendsoffriendsPersister friendsoffriendsPersister;
	private final World world;
	
	@Inject
	public WorldPersisterService(
			EgoPersister egoPersister, 
//			FriendsPersister friendPersister,
//			FriendsoffriendsPersister friendoffriendPersister, 
		World world) {
	this.egoPersister = egoPersister;
//	this.friendsPersister = friendPersister;
//	this.friendsoffriendsPersister = friendoffriendPersister;
	this.world = world;
}

	public void persist() throws PersisterException {
		Ego myUser = world.getMyUser();
		egoPersister.create(myUser);
		
		List<User> failedUsers = new ArrayList<>();  
		
		world.getMyFriendsMap().values().stream()
		.parallel()
		.forEach(f->{ 
//			try {
//				friendsPersister.create(f);
//			} catch (ExistantValueException e) {
//				failedUsers.add(f);
////				e.printStackTrace();
//			}
		});
		
		int size = failedUsers.size();
		if (size>0)
			throw new PersisterException("failed persisting friends: "+size);
		
		world.getOtherUsersMap().values().stream()
		.parallel()
		.forEach(f->{ 
//			try {
//				friendsoffriendsPersister.create(f);
//			} catch (ExistantValueException e) {
//				failedUsers.add(f);
//				e.printStackTrace();
//			}
		});
		
		size = failedUsers.size();
		if (size>0)
			throw new ExistantValueException("failed persisting friends of friends: "+size);
	}
	
	public void populate() {
		egoPersister.asMap().values().stream().findFirst().ifPresent(e->world.setMyUser(e));
		
//		friendsPersister.valuesIterator().forEachRemaining(
//				f->world.getMyFriendsMap().put(f.getUid(), f));
//		
//		friendsoffriendsPersister.valuesIterator().forEachRemaining(
//				f->world.getOtherUsersMap().put(f.getUid(), f));
	}
}
