package net.iubris.facri.parsers.ego;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.parsers.Parser;
import net.iubris.facri.parsers.posts.PostsParser;
import net.iubris.facri.parsers.utils.ParsingUtils;

public class EgoDataParser implements Parser {

	private final String feedsMeDataDir;
//	private final String friendsIdsFileRelativePath;
	
	private final MyFriendsParser myFriendsParser;
	private final PostsParser postsParser;
	private final World world;
	private final String dataRootDirPath;
	protected int i = 0;
//	private final String friendsFileRelativePath;
	
	@Inject
	public EgoDataParser(@Named("data_root_dir_path") String dataRootDirPath, // "output"
			@Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath, // "me"
//			@Named("me_id_file") String meIdFileRelativePath, // me_id.txt
//			@Named("friends_ids_file") String friendsIdsFileRelativePath, // friends_ids.txt
//			@Named("friends_file") String friendsFileRelativePath, // friends.txt
			PostsParser postsParser,
			MyFriendsParser myFriendsParser,
			World world
			) {
		this.dataRootDirPath = dataRootDirPath;
		//		this.friendsFileRelativePath = friendsFileRelativePath;
		this.feedsMeDataDir = dataRootDirPath+File.separatorChar+feedsMeDirRelativePath;
//		this.friendsIdsFileRelativePath = dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath;
		this.myFriendsParser = myFriendsParser;
		this.postsParser = postsParser;
		this.world = world;
	}
	
	@Override
	public void parse(File... userDirs) throws IOException {
System.out.print("Parsing my friends:");
		List<File> dirs = ParsingUtils.getDirectories(feedsMeDataDir);
		if (!dirs.isEmpty()) {
			File myUserDirectory = dirs.get(0);
			String myUserId = myUserDirectory.getName();
			Ego myUser = new Ego(myUserId);
			
//			myFriendsParser.parse( new File(friendsIdsFileRelativePath) );
			myFriendsParser.parse();
			
//			final Map<String, FriendOrAlike> myFriendsMap = world.getMyFriendsMap();
//			myFriendsParser.
//			getFriends()
//			.stream()
//			.parallel()
//			.forEach(
////				f->world.getMyFriendsMap().put(f.getUId(), f)
//					new Consumer<FriendOrAlike>() {
//						@Override
//						public void accept(FriendOrAlike f) {
//							if (f!=null) {
//								System.out.println(f.getUId());
//								myFriendsMap
//								.put(
//										f.getUId(), f
//								);
//							}
//						}
//					}
//			);			
			
			Set<String> friendsIds = myFriendsParser.getFriendsIds();
			System.out.println(friendsIds.size());
			myUser.addFriendsIds( friendsIds );
//			int i= 0;
			myUser.getFriendsIds().forEach(
					new Consumer<String>() {
						@Override
						public void accept(String t) {
							increment();
							System.out.println(i+ " "+t);
						}
					}
				);
			
			Ego me = 
//					new UserCSVParser<Ego>(dataRootDirPath+File.separator+"me.txt", Ego.class).parse().get(0);
					new Ego(myUserId);
			world.setMyUser(me);
			
			System.out.print("Parsing posts on my wall: ");
			postsParser.parse(myUserDirectory);
		}
		System.out.println(" ok");
	}
	
	private void increment() {
		i++;
//		return i;
	}
}
