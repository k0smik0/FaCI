/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (WorldPersisterService.java) is part of facri.
 * 
 *     WorldPersisterService.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     WorldPersisterService.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.cache.persister;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.Ego;
import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.model.world.users.FriendOfFriend;
import net.iubris.faci.model.world.users.GenericUser;
import net.iubris.faci.model.world.users.User;
import net.iubris.faci.persister.persisters.base.FaciPersister;
import net.iubris.faci.persisters.specialized.EgoPersister;
import net.iubris.faci.persisters.specialized.FriendsPersister;
import net.iubris.faci.persisters.specialized.FriendsoffriendsPersister;
import net.iubris.faci.persisters.specialized.OtherUsersPersister;
import net.iubris.faci.utils.Percentualizer;
import net.iubris.faci.utils.Printer;

public class WorldPersisterService {

	private final EgoPersister egoPersister;
	private final FriendsPersister friendsPersister;
	private final FriendsoffriendsPersister friendsoffriendsPersister;
	private final OtherUsersPersister otherUsersPersister;
	private final World world;
	
	@Inject
	public WorldPersisterService(
			EgoPersister egoPersister
			, FriendsPersister friendPersister
			, FriendsoffriendsPersister friendoffriendPersister
			, OtherUsersPersister otherUsersPersister
			, World world) {
		this.egoPersister = egoPersister;
		this.friendsPersister = friendPersister;
		this.friendsoffriendsPersister = friendoffriendPersister;
		this.otherUsersPersister = otherUsersPersister;
		this.world = world;
	}
	
	public void populate() {
		Printer.println("\nReading parsed data from cache: ");
		Printer.print("\tmy user: ");
		egoPersister.asMap().values().stream().findFirst().ifPresent(e->world.setMyUser(e));
		Printer.println("ok");
		
		Printer.print("\tmy friends: ");
		Map<String, Friend> friendsMap = world.getFriendsMap();
		friendsPersister.valuesIterator().forEachRemaining(
				f->friendsMap.put(f.getUid(), f));
		Printer.println("ok ("+friendsMap.size()+" friends)");
		
		Printer.print("\tfriends of my friends: ");
		Map<String, FriendOfFriend> friendsOfFriendsMap = world.getFriendsOfFriendsMap();
		friendsoffriendsPersister
			.valuesIterator()
			.forEachRemaining(
				fof->friendsOfFriendsMap.put(fof.getUid(), fof)
			);
		Printer.println("ok ("+friendsOfFriendsMap.size()+" friends of friends)");
		
		Printer.print("\tothers users: ");
		Map<String, GenericUser> otherUsersMap = world.getOtherUsersMap();
		otherUsersPersister.valuesIterator().forEachRemaining(
				ou->otherUsersMap.put(ou.getUid(), ou));
		Printer.println("ok ("+otherUsersMap.size()+" other users)");
	}

	public void persist() {
		Ego myUser = world.getMyUser();
		
//		if (egoPersister.contains(myUser.getUid())) {
//			Printer.println("\nParsed data already present - skip.");
//			return;
//		}
		
		Printer.println("\nWriting parsed data to cache: ");
		
		Printer.print("\tmy user: ");
		egoPersister.create(myUser);
		Printer.println("ok");		
		
		
//		Printer.print("\tmy friends: ");
		Collection<Friend> friends = world.getFriendsMap().values();
		persist(friends, friendsPersister, "my friends");
		/*Percentualizer friendsPercentualizer = new Percentualizer(friends.size());
		Iterator<Friend> friendsIterator = friends.iterator();
		.forEachRemaining( 
			f->friendsPersister.create(f)
		);
		Printer.println("ok");*/
		
		/*Printer.print("\tfriends of my friends: ");
		world.getFriendsOfFriendsMap().values().iterator()
		.forEachRemaining(u->{ 
				friendsoffriendsPersister.create(u);
		});
		Printer.println("ok");*/
		persist(world.getFriendsOfFriendsMap().values(), friendsoffriendsPersister, "friends of my friends");
		
		/*Printer.print("\tother users: ");
		world.getOtherUsersMap().values().iterator()
		.forEachRemaining(u->{ 
				otherUsersPersister.create(u);
		});
		Printer.println("ok");*/
		persist(world.getOtherUsersMap().values(), otherUsersPersister, "other users");
		
		Printer.println("done");
	}
	
	private <FoU extends User> void persist(Collection<FoU> users, FaciPersister<FoU> persister, String prefixToPrint) {
		int size = users.size();
		Percentualizer percentualizer = new Percentualizer(size);
		Printer.print("\t"+prefixToPrint+" ("+size+") [");
		Iterator<FoU> iterator = users.iterator();
		while (iterator.hasNext()) {
			FoU next = iterator.next();
			persister.create(next);
			percentualizer.printPercentual();
		}
		Printer.println("]: ok");
	}
}
