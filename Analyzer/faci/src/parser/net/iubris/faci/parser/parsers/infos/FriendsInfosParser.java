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
package net.iubris.faci.parser.parsers.infos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.parser._di.annotations.datafiles.FriendsIdsFilename;
import net.iubris.faci.parser._di.annotations.datafiles.FriendsInfoFilename;
import net.iubris.faci.parser.parsers.Parser;
import net.iubris.faci.parser.parsers.utils.UserDataParser;

public class FriendsInfosParser implements Parser {
	
	
	private final String friendsInfoFilename;
	private final String friendsIdsFilename;
	
//	private final Set<String> friendsIds = new ConcurrentSkipListSet<>();
//	private final List<Friend> friends = new CopyOnWriteArrayList<>();
	private final World world;
	
	
	@Inject
	public FriendsInfosParser(
//			@Named("data_root_dir_path") String dataRootDirPath, // "output"
			
//			@Named("friends_data_file")
			@FriendsInfoFilename
			String friendsInfoFilename, // friends.txt
			
//			,@Named("friends_ids_file")
			@FriendsIdsFilename
			String friendsIdsFilename, // friends_ids.txt
			World world
			) {
		this.friendsIdsFilename = friendsIdsFilename; // friends_ids.txt
		this.friendsInfoFilename = friendsInfoFilename; // friends.txt
		this.world = world;
//		this.friendsIdsFileRelativePath = dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath;
	}
	
	@Override
	public void parse(File... dummyArguments) {
//		File friendsIdsFile = arguments[0];
		try {
			world.getMyUser().addFriendsIds(
//			this.friendsIds.addAll( 
					Files.readAllLines(Paths.get(friendsIdsFilename), Charset.defaultCharset()) 
					);
//			this.friends.addAll
			new UserDataParser<Friend>(Friend.class, friendsInfoFilename).parse()
			.forEach(friend-> {
				world.getFriendsMap().put(friend.getUid(), friend);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public List<Friend> getFriends() {
//		return friends;
//	}
//	
//	public Set<String> getFriendsIds() {
//		return friendsIds;
//	}
}
