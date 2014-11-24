package net.iubris.facri.graph.generator;

public interface GraphGenerator {

	void generateMyFriends();

	void generateMyFriendsAndFriendOfFriends();

	void generateAll();

	void generateMeWithMyFriends();
	
	void clear();
}
