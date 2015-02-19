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
package net.iubris.facri.console.actions.graph.utils.cache.persister;


import javax.inject.Inject;

import net.iubris.facri.model.parser.users.Ego;
import net.iubris.facri.model.world.World;
import net.iubris.facri.persisters.specialized.EgoPersister;
import net.iubris.facri.persisters.specialized.FriendsPersister;
import net.iubris.facri.persisters.specialized.FriendsoffriendsPersister;
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

	public void persist() {
		Ego myUser = world.getMyUser();
		
		if (egoPersister.contains(myUser.getUid())) {
			Printer.println("\nParsed data already present - skip.");
			return;
		}		
		
		Printer.println("\nWriting parsed data to cache: ");
		
		Printer.print("\tmy user: ");
				egoPersister.create(myUser);
		Printer.println("ok");		
		
		
		Printer.print("\tmy friends: ");
		world.getMyFriendsMap().values().stream()
		.parallel()
		.forEach(u->{ 
				friendsPersister.create(u);
		});
		Printer.println("ok");
		
		Printer.print("\tfriends of my friends: ");
		world.getOtherUsersMap().values().stream()
		.parallel()
		.forEach(u->{ 
				friendsoffriendsPersister.create(u);
		});
		Printer.println("ok");
		Printer.println("done");
	}
}
