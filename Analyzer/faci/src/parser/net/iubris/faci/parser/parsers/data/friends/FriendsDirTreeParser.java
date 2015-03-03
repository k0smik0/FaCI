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
package net.iubris.faci.parser.parsers.data.friends;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.faci.parser._di.annotations.datafiles.FeedsFriendsDirname;
import net.iubris.faci.parser.parsers.Parser;
import net.iubris.faci.parser.parsers.posts.PostsParser;
import net.iubris.faci.parser.parsers.utils.ParsingUtils;
import net.iubris.faci.utils.Percentualizer;
import net.iubris.faci.utils.Printer;
import net.iubris.faci.utils.Timing;
//import net.iubris.ishtaran.gui._di.annotations.ProgressBarGlobalSize;

public class FriendsDirTreeParser implements Parser {
	
	private final PostsParser postsParser;
	private final MutualFriendsParser mutualFriendsParser;
	private final FriendsOfFriendIdsParser friendsOfFriendIdsParser;
	
	private final String feedsFriendsDirname;
	private final World world;
	
//	@ProgressBarGlobalSize
	private int friendsTotal;
	private Percentualizer percentualizer;
	
	@Inject
	public FriendsDirTreeParser(PostsParser postsParser, 
			MutualFriendsParser mutualFriendsParser,
			FriendsOfFriendIdsParser friendsOfFriendsIdsParser,
			@FeedsFriendsDirname String feedsFriendsDirname,
			World world) {
		this.postsParser = postsParser;
		this.mutualFriendsParser = mutualFriendsParser;
		this.friendsOfFriendIdsParser = friendsOfFriendsIdsParser;

		this.feedsFriendsDirname = feedsFriendsDirname;
		this.world = world;
	}	
	
	@Override
	public void parse(File... dummy) {
		List<File> friendsDirectories = ParsingUtils.getDirectories(feedsFriendsDirname);		
		friendsTotal = friendsDirectories.size();
		percentualizer = new Percentualizer(friendsTotal);
		parseUsersDirs( friendsDirectories );		
	}
	private void parseUsersDirs(List<File> friendsDirectories) {
		Printer.print("Parsing my friends feeds: ");
		Timing timing = new Timing();
		
		friendsDirectories.stream()
		.parallel()
		.peek(
			dir-> percentualizer.printPercentual()
		)
		.filter( dir-> { 
			File[] files = dir.listFiles();
			if (files!=null && files.length>0)
				return true;
			return false;
				} /*checkDir(s)*/ 
		)
		.parallel() // parallel on each directory ? // TODO restore
		.forEach( friendDir->{
			// always create owning wall user	
			String owningWallUserId = friendDir.getName();
//			FriendOrAlike friend = world.isExistentUserOrCreateNew(owningWallUserId);
			world.isExistentFriendOrCreateNew(owningWallUserId);
				
			// this order below is mandatory!
			mutualFriendsParser.parse(friendDir);
			postsParser.parse(friendDir);
			friendsOfFriendIdsParser.parse(friendDir);
		});
		Printer.println(": ok");
		double finish = timing.getTiming();
		
		Printer.println( "Parsed "+friendsTotal+" friends in: "+ new DecimalFormat("#.##").format(finish) +"s" );

	}
}
