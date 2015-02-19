/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FriendsDataParser.java) is part of facri.
 * 
 *     FriendsDataParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FriendsDataParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.parsers.friends;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.world.World;
import net.iubris.facri.parsers.Parser;
import net.iubris.facri.parsers.posts.PostsParser;
import net.iubris.facri.parsers.utils.ParsingUtils;
import net.iubris.facri.utils.Printer;
//import net.iubris.ishtaran.gui._di.annotations.ProgressBarGlobalSize;
import net.iubris.facri.utils.Timing;

public class FriendsDataParser implements Parser {
	
	private final String feedsFriendsDataDir;
	
	private final PostsParser postsParser;
	private final MutualFriendsParser mutualFriendsParser;
	
//	@ProgressBarGlobalSize
	private int usersTotal;
	
	private int userCounter;

	private int percentCounter;

	private final UserFriendsParser userFriendsParser;

	private final World world;
	
	@Inject
	public FriendsDataParser(PostsParser postsParser, 
			MutualFriendsParser mutualFriendsParser,
			UserFriendsParser userFriendsParser,
			World world,
			@Named("data_root_dir_path") String dataRootDirPath, // "output"
			@Named("feeds_friends_dir_relative_path") String feedsFriendsDirRelativePath // "friends"
			) {
		this.postsParser = postsParser;
		this.mutualFriendsParser = mutualFriendsParser;
		this.userFriendsParser = userFriendsParser;
		this.world = world;
		this.feedsFriendsDataDir = dataRootDirPath+File.separatorChar+feedsFriendsDirRelativePath;
	}
	
	
	@Override
	public void parse(File... dummy) {
		List<File> friendsDirectories = ParsingUtils.getDirectories(feedsFriendsDataDir);		
		setUsersTotal( friendsDirectories.size() );
Printer.print("Parsing my friends feeds: ");
		parseUsersDirs( friendsDirectories );		
	}
	private void parseUsersDirs(List<File> usersDirs) {
		Timing timing = new Timing();
		
		// lambda stream
		userCounter = 0;
		usersDirs.stream()
		.parallel()
		.peek( 
			dir->printPercentual( incrementUserCounter() )
		)
		.filter( dir-> { 
			File[] files = dir.listFiles();
			if (files!=null && files.length>0)
				return true;
			return false;
				} /*checkDir(s)*/ 
		)
		.parallel() // parallel on each directory ? // TODO restore
		.forEach( userDir->{
			// always create owning wall user	
			String owningWallUserId = userDir.getName();
//			FriendOrAlike friend = world.isExistentUserOrCreateNew(owningWallUserId);
			world.isExistentFriendOrCreateNew(owningWallUserId);
				
			// this order below is mandatory!
			mutualFriendsParser.parse(userDir);
			postsParser.parse(userDir);
			userFriendsParser.parse(userDir);
		});
		Printer.println(": ok");		
		double finish = timing.getTiming();
		
Printer.println( "Parsed "+usersTotal+" users in: "+ new DecimalFormat("#.##").format(finish) +"s" );
		userCounter = 0;
		percentCounter = 0;
	}
	
	private void printPercentual(int userCounter) {
		double percent = Math.ceil( userCounter*1.0f/usersTotal*100 );
		if (Math.floor(percent)%10 == 0) {
			percentCounter++;
			if (percentCounter==1) {
				int toPrint = (int)percent;
				Printer.print(toPrint);					
				if (toPrint<100) {
					Printer.print("%... ");					
				} else
					Printer.print("%");
			}
			if (percentCounter>9)
				percentCounter=0;
		}
	}
	
	private void setUsersTotal(int usersTotal) {
		this.usersTotal = usersTotal;	
	}
	private int incrementUserCounter() {
		return ++userCounter;
	}
}
