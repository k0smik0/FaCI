/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (MutualFriendsParser.java) is part of facri.
 * 
 *     MutualFriendsParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MutualFriendsParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.parsers.friends;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.parser.users.Friend;
import net.iubris.facri.model.world.World;
import net.iubris.facri.parsers.Parser;
import net.iubris.facri.utils.Printer;

public class MutualFriendsParser implements Parser {
	
	private final String mutualFriendsFilename;
	private final World world;
	
	private final Pattern pattern = Pattern.compile("[0-9]{4,}");
	
	@Inject
	public MutualFriendsParser(@Named("mutual_friends_filename") String mutualFriendsFilename,
			World world) {
		this.mutualFriendsFilename = mutualFriendsFilename;
		this.world = world;
	}

	public void parse(File... arguments) {
		File userDir = arguments[0];
		
		// old way
//		File[] files = userDir.listFiles( new FilenameFilter() {
//			@Override
//			public boolean accept(File dir, String name) {
//				if (name.equals(mutualFriendsFilename)) {
//					return true;
//				}
//				return false;
//			}
//		});
//		if (files.length>0) {
//			File mutualFriendsFile = files[0];
//			parseWithScanner(mutualFriendsFile, userDir);
//		}
		
		// java 8 way
		try {
			Files.list( FileSystems.getDefault().getPath(userDir.getPath() ) )
				.filter(mutualFriendsFilenamePath->mutualFriendsFilenamePath.getFileName().toString().equals(mutualFriendsFilename))
//				.forEach(
				.findFirst()
				.ifPresent(
						mutualFriendsFilePathEach -> {
//							System.out.println("parsing: "+mutualFriendsFilePathEach.getFileName()); // TODO remove
							parseMutualFriendsFile(mutualFriendsFilePathEach.toFile(), userDir.getName()); 
						}
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseMutualFriendsFile(File mutualFriendsFile, String owningWallFriendId) {
		try {
			Path mutualFriendsFilePath = mutualFriendsFile.toPath();
			List<String> mutualFriendsLines = Files.readAllLines(mutualFriendsFilePath, StandardCharsets.UTF_8);
			if (mutualFriendsLines.size() < 2)
				return;
			
			Friend friend = world.getMyFriendsMap().get(owningWallFriendId);

			// TODO eventually restore old working
//			user.addMutualFriends(
					mutualFriendsLines
					.stream()
					.parallel()
					// new 2
					.map(line->pattern.matcher(line))
//					.map(matcher->matcher.find()? matcher.group(0): "")
//					.filter(word->!word.isEmpty())
//					.collect(Collectors.toSet())
//					)// end add from collector
					.forEach(matcher->{
						if (matcher.find()) {
							String id = matcher.group(0);
//							System.out.println(id);
							friend.addMutualFriendId(id);
						}
					})
				// end new 2
				// new
//				.flatMap(line-> {
//					System.out.println("LINE "+line);
//					return pattern.splitAsStream(line);
//					})
//				.forEach(word->{
//					System.out.println("WORD: "+word);
//					user.addMutualFriend(word);
//				})
				// end new
				
				// old working
				/*.forEach(
						new Consumer<String>() {
							@Override
							public void accept(String t) {
								Matcher matcher = pattern.matcher(t);
								if (matcher.find())
									user.addMutualFriend(matcher.group(0));
							}
						}
					)*/
				;
		} catch (IOException e) {
			Printer.println("errors on "+mutualFriendsFile.getName());
			e.printStackTrace();
		}
	}
}
