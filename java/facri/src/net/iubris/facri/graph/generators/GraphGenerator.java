package net.iubris.facri.graph.generators;

public interface GraphGenerator {

	void generateMyFriends();

	void generateMyFriendsAndFriendOfFriends();

	void generateMeWithMyFriendsAndTheirFriends();

	void generateMeWithMyFriends();
	
	void clear();
}
