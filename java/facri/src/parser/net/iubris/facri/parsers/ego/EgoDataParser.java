package net.iubris.facri.parsers.ego;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.UserParser;
import net.iubris.facri.model.world.World;
import net.iubris.facri.parsers.Parser;
import net.iubris.facri.parsers.posts.PostsParser;
import net.iubris.facri.parsers.utils.ParsingUtils;
import net.iubris.facri.utils.Printer;

public class EgoDataParser implements Parser {

	private final String feedsMeDataDir;
	private final String friendsIdsFileRelativePath;
	
	private final MyFriendsParser myFriendsParser;
	private final PostsParser postsParser;
	private final World world;
	private String meFileRelativePath;
	
	@Inject
	public EgoDataParser(@Named("data_root_dir_path") String dataRootDirPath, // "output"
			@Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath, // "me"
			@Named("me_id_file") String meIdFileRelativePath, // me_id.txt
			@Named("me_file") String meFileRelativePath, // me.txt
			@Named("friends_ids_file") String friendsIdsFileRelativePath, // friends_ids.txt
			PostsParser postsParser,
			MyFriendsParser myFriendsParser,
			World world
			) {
		this.meFileRelativePath = dataRootDirPath+File.separatorChar+meFileRelativePath;
		this.feedsMeDataDir = dataRootDirPath+File.separatorChar+feedsMeDirRelativePath;
		this.friendsIdsFileRelativePath = dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath;
		this.myFriendsParser = myFriendsParser;
		this.postsParser = postsParser;
		this.world = world;
	}
	
	@Override
	public void parse(File... userDirs) throws IOException {
Printer.print("Parsing my own feeds:");
		List<File> dirs = ParsingUtils.getDirectories(feedsMeDataDir);
		if (!dirs.isEmpty()) {
			File myUserDirectory = dirs.get(0);
//			String myUserId = myUserDirectory.getName();
			Ego myUser = 
//					new Ego(myUserId);
					new UserParser<Ego>(Ego.class, meFileRelativePath).parse().get(0);
			
//			System.out.println("\nEgo: "+myUser);
			
			myFriendsParser.parse( new File(friendsIdsFileRelativePath) );
			myUser.addFriendsIds( myFriendsParser.getFriendsIds() );
			
			world.setMyUser(myUser);
			myFriendsParser.getFriends().forEach(f->world.getMyFriendsMap().put(f.getUid(), f));
//			world.setMyFriendsMap(myFriendsMap);
			
			postsParser.parse(myUserDirectory/*, owningWallUserId*/);
		}
		Printer.println(" ok");
	}
}
