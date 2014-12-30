package net.iubris.facri.restfb;

import java.util.List;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String MY_ACCESS_TOKEN = "CAACEdEose0cBALNpbIwJwMBWcTCd0tZBfntMyFKsWh9wrYGZB0cZAYk5ZAZAmgmZCCwsmRFoiiQRqveSs3LthdMjKv8LF60DoXzIxkROSouOfANAWCBqgESNBbxhRoef0CYNY6clHkCsp0wMsJ88P0KoPao9VsYYIWZAPxs5OsaAF3NZBqmZAZCH5rOddLjZBc13VEMExfMYZASwJTfxfriwJ4SSidmGgZBt8bFgZD";
		String MY_APP_ID = "307394272798646";
		String MY_APP_SECRET = "6187b2ccecd3af6855c4151de1c52f2f";

		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN,MY_APP_SECRET);

		User me = facebookClient.fetchObject("me", User.class);

//		Connection<User> myFriendsConnection = facebookClient.fetchConnection("me/friends", User.class);
//
//		while (myFriendsConnection.hasNext())
//			for (List<User> myFriendsConnectionPage : myFriendsConnection) {
//				System.out.println("adding "+myFriendsConnectionPage.size());
//				for (User friend : myFriendsConnectionPage) {
//					friends.add(friend);
//				}
//			}
//
//		// String friendsConnectionNextPageUrl = null;
//		// do {
//		// friendsConnectionNextPageUrl = myFriendsConnection.getNextPageUrl();
//		// myFriendsConnection.
//		// } while (friendsConnectionNextPageUrl!=null);
//		
		String fqlMeQuery = "SELECT uid2 FROM friend WHERE uid1=me()";
		String fqlFriendsQuery = "SELECT id, name, pic, type, url, username FROM profile WHERE id IN (SELECT uid2 FROM friend WHERE uid1=me())";
		List<FqlUser> friends = facebookClient.executeFqlQuery(fqlFriendsQuery, FqlUser.class);
	
		
//		
					
		System.out.println(me.getName() + " has " + friends.size() + " friends");
		for (FqlUser friend : friends) {
			System.out.println(friend.getId() + " " + friend.getName());
		}
	}

}
