package net.iubris.facri.parsers.ego;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import net.iubris.facri.parsers.Parser;

public class MyFriendsParser implements Parser {
	
	private Set<String> friendsIds = new ConcurrentSkipListSet<String>();
	
	@Override
	public void parse(File... arguments) {
		File friendsIdsFile = arguments[0];
		try {
			friendsIds.addAll( Files.readAllLines(friendsIdsFile.toPath(), Charset.defaultCharset()) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Set<String> getFriendsIds() {
		return friendsIds;
	}

}
