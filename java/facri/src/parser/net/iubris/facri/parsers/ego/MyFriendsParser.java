package net.iubris.facri.parsers.ego;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.UserParser;
import net.iubris.facri.parsers.Parser;

public class MyFriendsParser implements Parser {
	
	private final Set<String> friendsIds = 
			new ConcurrentSkipListSet<String>();
//			new CopyOnWriteArraySet<String>();
	private final String friendsFileRelativePath;
	private List<FriendOrAlike> friends;
//	private final String friendsIdsFileRelativePath;
	
	@Inject
	public MyFriendsParser(
			@Named("data_root_dir_path") String dataRootDirPath, // "output"
			@Named("friends_file") String friendsFileRelativePath // friends.txt
//			,@Named("friends_ids_file") String friendsIdsFileRelativePath // friends_ids.txt
			) {
		this.friendsFileRelativePath = dataRootDirPath+File.separatorChar+friendsFileRelativePath;
//		this.friendsIdsFileRelativePath = dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath;
	}
	
	@Override
	public void parse(File... arguments) {
		File friendsIdsFile = arguments[0];
		try {
			friendsIds.addAll( Files.readAllLines(friendsIdsFile.toPath(), Charset.defaultCharset()) );
			
//			CsvToBean<FriendOrAlike> csvToBean = new CsvToBean<>();
//	      CSVReader csvReader = new CSVReader(new FileReader(friendsFileRelativePath));
//	      
//	      List<FriendOrAlike> friends = csvToBean.parse(setColumMapping(), csvReader);

			this.friends = new UserParser<FriendOrAlike>(FriendOrAlike.class, friendsFileRelativePath).parse();
//	      for (FriendOrAlike friend : friends) {
//	          System.out.println(friend);
//	      }

			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<FriendOrAlike> getFriends() {
		return friends;
	}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
	/*private static ColumnPositionMappingStrategy<FriendOrAlike> setColumMapping() {
		ColumnPositionMappingStrategy<FriendOrAlike> strategy = new ColumnPositionMappingStrategy<>();
		strategy.setType(FriendOrAlike.class);
		// UID,NAME,FRIEND_COUNT,MUTUAL_FRIEND_COUNT,PIC_SMALL,PROFILE_URL,SEX,SIGNIFICANT_OTHER_ID
		String[] columns = new String[] { "uid", "name", "friendCount", "mutualFriendCount","picSmall", "profileURL", "sex", "significantOtherId" };
		strategy.setColumnMapping(columns);
		return strategy;
	}*/
	
	public Set<String> getFriendsIds() {
		return friendsIds;
	}
}
