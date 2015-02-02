/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (CacheUtils.java) is part of facri.
 * 
 *     CacheUtils.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     CacheUtils.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.test_main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.inject.Inject;

import net.iubris.facri.model.world.World;

public class CacheUtils {
	
	// this is a singleton
	private final World world;
	
	@Inject
	public CacheUtils(World world) {
		this.world = world;
	}

	public void parseCache(String cacheFilenameToRead) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(cacheFilenameToRead) );
		World worldFromcache = (World) ois.readObject();
		world.setMyUser(worldFromcache.getMyUser());
		world.populateMyFriendsMap(worldFromcache.getMyFriendsMap());
		world.populateOtherUsersMap(worldFromcache.getOtherUsersMap());
		worldFromcache = null;
		ois.close();
	}
	
	public void writeCache(String cacheFilenameToWrite) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( cacheFilenameToWrite) );
		oos.writeObject(world);
		oos.close();
	}
}
