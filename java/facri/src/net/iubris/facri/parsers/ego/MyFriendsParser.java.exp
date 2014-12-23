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
import net.iubris.facri.parsers.Parser;

public class MyFriendsParser implements Parser {
	
	private final Set<String> friendsIds = new ConcurrentSkipListSet<String>();
	private final String friendsFileRelativePath;
	private final String friendsIdsFileRelativePath;
	private List<FriendOrAlike> friends;

	@Inject
	public MyFriendsParser(
			@Named("data_root_dir_path") String dataRootDirPath, // "output"
			@Named("friends_file") String friendsFileRelativePath // friends.txt
			,@Named("friends_ids_file") String friendsIdsFileRelativePath // friends_ids.txt
			) {

		this.friendsFileRelativePath = dataRootDirPath+File.separatorChar+friendsFileRelativePath;
		this.friendsIdsFileRelativePath = dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath;
	}
	
	@Override
	public void parse(File... arguments) throws IOException {
//		File friendsIdsFile = arguments[0];
		File friendsIdsFile = new File(friendsIdsFileRelativePath);
//		File friendsFile = new File(friendsFileRelativePath);
//		try {
			friendsIds.addAll( Files.readAllLines(friendsIdsFile.toPath(), Charset.defaultCharset()) );
//			System.out.println("friendsIds: "+friendsIds.size());
			
//			Reader reader = new FileReader(friendsFileRelativePath);
//
//			ValueProcessorProvider provider = new ValueProcessorProvider();
//			CSVEntryParser<FriendOrAlike> entryParser = new AnnotationEntryParser<FriendOrAlike>(FriendOrAlike.class, provider);
//			CSVReader<FriendOrAlike> csvPersonReader = new CSVReaderBuilder<FriendOrAlike>(reader).entryParser(entryParser).build();
//
//			friends = csvPersonReader.readAll();

//			friends = new UserCSVParser<FriendOrAlike>(friendsFileRelativePath, FriendOrAlike.class).parse();
//			System.out.println( "friends: "+friends.size() );
			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public Set<String> getFriendsIds() {
		return friendsIds;
	}
	
	public List<FriendOrAlike> getFriends() {
		return friends;
	}
}
