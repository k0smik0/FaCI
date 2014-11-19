package net.iubris.facri.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

// TODO migrate world to multiverse 
@Singleton
public class Multiverse {
	
	private final Map<String,World> universesMap = new ConcurrentHashMap<>();
	
	public Map<String, World> getUniversesMap() {
		return universesMap;
	}
	
	public World getUniverse(String egoUserId) {
		return universesMap.get(egoUserId);
	}

}
