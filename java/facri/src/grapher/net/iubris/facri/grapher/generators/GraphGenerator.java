package net.iubris.facri.grapher.generators;

public interface GraphGenerator {

	void generateMyFriends();

	void generateMyFriendsAndFriendOfFriends();

	void generateMeWithMyFriendsAndTheirFriends();

	void generateMeWithMyFriends();
	
	void clear();

	void generateFriendOfFriends();
}
