package net.iubris.facri.console.actions.graph.utils.cache.persister;


import javax.inject.Inject;

import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.world.World;
import net.iubris.facri.persisters.EgoPersister;
import net.iubris.facri.persisters.FriendsPersister;
import net.iubris.facri.persisters.FriendsoffriendsPersister;
import net.iubris.facri.utils.Printer;

public class WorldPersisterService {

	private final EgoPersister egoPersister;
	private final FriendsPersister friendsPersister;
	private final FriendsoffriendsPersister friendsoffriendsPersister;
	private final World world;
	
	@Inject
	public WorldPersisterService(
			EgoPersister egoPersister
			, FriendsPersister friendPersister
			, FriendsoffriendsPersister friendoffriendPersister 
			, World world) {
		this.egoPersister = egoPersister;
		this.friendsPersister = friendPersister;
		this.friendsoffriendsPersister = friendoffriendPersister;
		this.world = world;
	}
	
	public void populate() {
		Printer.println("\nReading parsed data from cache: ");
		Printer.print("\tmy user: ");
		egoPersister.asMap().values().stream().findFirst().ifPresent(e->world.setMyUser(e));
		Printer.println("ok");
		
		Printer.print("\tmy friends: ");
		friendsPersister.valuesIterator().forEachRemaining(
				f->world.getMyFriendsMap().put(f.getUid(), f));
		Printer.println("ok");
		
//		try {
//			world.testData();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		
		Printer.print("\tfriends of my friends: ");
		friendsoffriendsPersister.valuesIterator().forEachRemaining(
				fof->world.getOtherUsersMap().put(fof.getUid(), fof));
		Printer.println("ok");
	}

	public void persist() /*throws PersisterException*/ {
		Printer.println("\nWriting parsed data to cache: ");
		Ego myUser = world.getMyUser();
		Printer.print("\tmy user: ");
//		Boolean created = 
				egoPersister.create(myUser);
		Printer.println("ok");		
//		if (!created)
//			throw new PersisterException("Ego: "+myUser.getUid()+" already existant");
		
//		List<User> failedUsers = new ArrayList<>();  
		
		Printer.print("\tmy friends: ");
		world.getMyFriendsMap().values().stream()
		.parallel()
		.forEach(u->{ 
//			try {
				friendsPersister.create(u);
//			} catch (ExistantValueException e) {
//				failedUsers.add(f);
////				e.printStackTrace();
//			}
//			if (!friendsPersister.create(u).booleanValue())
//				failedUsers.add(u);
		});
		Printer.println("ok");
		
//		int size = failedUsers.size();
//		if (size>0)
//			throw new PersisterException("failed persisting friends: "+size);
		
		Printer.print("\tfriends of my friends: ");
		world.getOtherUsersMap().values().stream()
		.parallel()
		.forEach(u->{ 
//			try {
				friendsoffriendsPersister.create(u);
//			} catch (ExistantValueException e) {
//				failedUsers.add(f);
//				e.printStackTrace();
//			}
//			if (!friendsoffriendsPersister.create(u).booleanValue())
//				failedUsers.add(u);
		});
		Printer.println("ok");
		Printer.println("done");
		
//		size = failedUsers.size();
//		if (size>0)
////			throw new ExistantValueException("failed persisting friends of friends: "+size);
//			throw new PersisterException("failed persisting friends of friends: "+size);
	}
}
