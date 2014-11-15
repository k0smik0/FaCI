package net.iubris.facri.parsers.ego;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.parsers.posts.PostsParser;
import net.iubris.facri.parsers.utils.ParsingUtils;

public class EgoDataParser {

	private final String feedsMeDataDir;
	private final String friendsIdsFileRelativePath;
	
	private final MyFriendsParser myFriendsParser;
	private final PostsParser postsParser;
	private final World world;
	
	@Inject
	public EgoDataParser(@Named("data_root_dir_path") String dataRootDirPath, // "output"
			@Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath, // "me"
//			@Named("me_id_file") String meIdFileRelativePath, // me_id.txt
			@Named("friends_ids_file") String friendsIdsFileRelativePath, // friends_ids.txt
			PostsParser postsParser,
			MyFriendsParser myFriendsParser,
			World world
			) {
		this.feedsMeDataDir = dataRootDirPath+File.separatorChar+feedsMeDirRelativePath;
		this.friendsIdsFileRelativePath = dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath;
		this.myFriendsParser = myFriendsParser;
		this.postsParser = postsParser;
		this.world = world;
	}
	
	public void parse() {
System.out.println("Parsing my own feeds:");
		List<File> dirs = ParsingUtils.getDirectories(feedsMeDataDir);
		if (!dirs.isEmpty()) {
			File myUserDirectory = dirs.get(0);
			String myUserId = myUserDirectory.getName();		
			Ego myUser = new Ego(myUserId);
			
			myFriendsParser.parse( new File(friendsIdsFileRelativePath) );
			myUser.addFriendsIds( myFriendsParser.getFriendsIds() );
			
			world.setMyUser(myUser);
			
			postsParser.parse(myUserDirectory/*, owningWallUserId*/);
		}
//		System.out.println(myUser.getId());
//		world = new World( myUser );
//		world.setMyUser(myUser);
//		User myUserRemoved = useridsToUsersMap.remove( myUser );
//		System.out.println(myUserRemoved);
	}
}
