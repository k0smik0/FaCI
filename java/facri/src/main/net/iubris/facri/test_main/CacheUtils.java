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
