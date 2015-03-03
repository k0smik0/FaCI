/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (EgoDataParser.java) is part of facri.
 * 
 *     EgoDataParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     EgoDataParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.parsers.data.ego;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.faci.parser._di.annotations.datafiles.FeedsMeDataDirname;
import net.iubris.faci.parser.parsers.Parser;
import net.iubris.faci.parser.parsers.posts.PostsParser;
import net.iubris.faci.parser.parsers.utils.ParsingUtils;
import net.iubris.faci.utils.Printer;

public class EgoDataParser implements Parser {

	private final String feedsMeDataDirname;
//	private final String friendsIdsFile;
	
//	private final FriendsInfosParser friendsInfosParser;
	private final PostsParser postsParser;
//	private final World world;
//	private String meFile;
	
	@Inject
	public EgoDataParser(
//			@Named("data_root_dir_path") String dataRootDirPath, // "output"

//			@Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath, // "me" // old
			@FeedsMeDataDirname String feedsMeDataDirname,
			
//			@Named("me_id_file") String meIdFileRelativePath, // me_id.txt // useless
//			
//			@Named("me_info_file")
//			@MeFile
//			String meFile, // me.txt
			
//			@Named("friends_ids_file")
//			@FriendsIdsFile
//			String friendsIdsFile, // friends_ids.txt

			PostsParser postsParser,
//			FriendsInfosParser friendsInfosParser,
			World world
			) {
		this.feedsMeDataDirname = 
//				dataRootDirPath+File.separatorChar+feedsMeDirRelativePath;
				feedsMeDataDirname;
//		this.meFile = 
////				dataRootDirPath+File.separatorChar+meFileRelativePath;
//				meFile;
//		this.friendsIdsFile = 
////				dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath;
//				friendsIdsFile;
		
//		this.friendsInfosParser = friendsInfosParser;
		this.postsParser = postsParser;
//		this.world = world;
	}
	
	@Override
	public void parse(File... userDirs) throws IOException {
Printer.print("Parsing my user feeds:");
		List<File> dirs = ParsingUtils.getDirectories(feedsMeDataDirname);
		if (!dirs.isEmpty()) {
			File myUserDirectory = dirs.get(0);
//			Ego myUser = new UserDataParser<Ego>(Ego.class, meFile).parse().get(0);
			
//			friendsInfosParser.parse( new File(friendsIdsFile) );
//			myUser.addFriendsIds( friendsInfosParser.getFriendsIds() );
//			
//			world.setMyUser(myUser);
//			friendsInfosParser.getFriends()
//			.forEach(friend-> {
//				world.getMyFriendsMap().put(friend.getUid(), friend);
//			});
			
			postsParser.parse(myUserDirectory);
		}
		Printer.println(" ok");
	}
}
