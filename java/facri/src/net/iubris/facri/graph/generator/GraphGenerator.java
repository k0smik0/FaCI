package net.iubris.facri.graph.generator;

public interface GraphGenerator {

	void generateMyFriends();

	void generateMyFriendsAndFriendOfFriends();

	void generateMeWithMyFriendsAndTheirFriends();

	void generateMeWithMyFriends();
	
	void clear();
}
