/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (CommentsParser.java) is part of facri.
 * 
 *     CommentsParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     CommentsParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.parsers.posts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.AbstractFriend;
import net.iubris.faci.model.world.users.Ego;
import net.iubris.faci.parser._di.annotations.filenamefilters.CommentsFilenameFilter;
import net.iubris.faci.parser.model.comments.CommentData;
import net.iubris.faci.parser.model.comments.CommentsHolder;
import net.iubris.faci.parser.model.posts.Post;
import net.iubris.faci.utils.Printer;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class CommentsParser {
	
	private final JsonXMLMapper<CommentsHolder> commentsMapper;
	private final FilenameFilter commentFilesFilenameFilter;
	private final World world;

	@Inject
	public CommentsParser(JsonXMLMapper<CommentsHolder> commentsMapper, 
			@CommentsFilenameFilter FilenameFilter commentFilesFilenameFilter,
			World world) {
		this.commentsMapper = commentsMapper;
		this.commentFilesFilenameFilter = commentFilesFilenameFilter;
		this.world = world;
	}

	public void parse(File userDir, String owningWallUserId, Post post) {
		world.isExistentUserOrCreateNew(owningWallUserId); // TODO redundant ?
		File[] commentsFiles = userDir.listFiles(commentFilesFilenameFilter);
		if (commentsFiles.length>0) {
			File commentsJsonFile = commentsFiles[0];
			try {
				CommentsHolder comments = commentsMapper.readObject( new FileReader(commentsJsonFile));
				
				List<CommentData> commentsData = comments.getCommentsData().getCommentsData();
				
				for (CommentData commentData: commentsData) {
					String commentingUserId = commentData.getFromId();
					AbstractFriend genericFriend = null;
					// if commentingUserId is Ego or Friend, the user receiving comments is obviously my friend
					Ego myUser = world.getMyUser();
					if (myUser.getUid().equals(commentingUserId)|| myUser.isMyFriendById(commentingUserId)) {
						genericFriend = world.isExistentFriendOrCreateNew(commentingUserId);
					} else {
						genericFriend = world.isExistentFriendOfFriendOrCreateNew(commentingUserId);
					}
//					genericFriend.getToOtherUserInteractions(owningWallUserId).incrementCommentsCounter();
					genericFriend.getToOtherUserInteractions(owningWallUserId).addCommentData(commentData);
				}
			} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
				Printer.println("error for file: "+commentsJsonFile.getName());
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * test
	 * @param args
	 */
	/*public static void main(String[] args) {
		String commentsJsonFile = "../../fbcmd/output/feeds/friends/1529169343/10203765980702095_comments.json";
		
		Injector injector = Guice.createInjector( new FaciParserModule() );
		
		JsonXMLMapper<CommentsHolder> commentsMapper = injector.getInstance(CommentsHolderMapperProvider.class).get();
		
		try {
			CommentsHolder commentsHolder = commentsMapper.readObject( new FileReader(commentsJsonFile));
			
//			System.out.println(commentsHolder);
			List<Comment> commentsData = commentsHolder.getCommentsData().getCommentsData();
//			
			for (Comment commentData: commentsData) {
//	System.out.println(commentData);
				String commentingUserId = commentData.getFromId();
	Printer.println("\t\t\tcomment: "+commentingUserId);
//	System.out.println("here");
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}*/
}
