/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (MyFriendsParser.java) is part of facri.
 * 
 *     MyFriendsParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyFriendsParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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

import net.iubris.facri.model.parser.users.Friend;
import net.iubris.facri.model.parser.users.UserParser;
import net.iubris.facri.parsers.Parser;

public class MyFriendsParser implements Parser {
	
	private final Set<String> friendsIds = new ConcurrentSkipListSet<String>();
	private final String friendsFileRelativePath;
	private List<Friend> friends;
	
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
			this.friends = new UserParser<Friend>(Friend.class, friendsFileRelativePath).parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Friend> getFriends() {
		return friends;
	}
	
	public Set<String> getFriendsIds() {
		return friendsIds;
	}
}
