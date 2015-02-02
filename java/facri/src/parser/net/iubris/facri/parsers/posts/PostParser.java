/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (PostParser.java) is part of facri.
 * 
 *     PostParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     PostParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.parsers.posts;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import net.iubris.facri.model.parser.comments.LikesInfo;
import net.iubris.facri.model.parser.posts.Post;
import net.iubris.facri.model.parser.users.User;
import net.iubris.facri.model.world.World;

public class PostParser {
	
	private final World world;
	
	@Inject
	public PostParser(World world) {
		this.world = world;
	}

	public void parse(Post post, String owningWallUserId) {
		// always owning wall user
		world.isExistentUserOrCreateNew(owningWallUserId);
		
		// the actorId is the post author id
		String actorPostId = post.getActorId();
		
		// we use actorId because a wall contains both posts
		// from owner (actorId == userDir) or other users (actorId != userDir)
		User actorUser = world.isExistentUserOrCreateNew(actorPostId);
		// always add post to its author
		actorUser.addOwnPost(post);

		if (actorPostId.equals(owningWallUserId)) { // post author is the same of the wall owner
			handleAuthorUser(actorUser, post);
//			actorUser.addOwnPost(post);
		} else /*if (actorPostId != owningWallUserId)*/ {
			handleTargetUser(actorUser, post, owningWallUserId);
		}

		handleTags(post, actorUser, post.getTaggedIDs());
		handleTags(post, actorUser, post.getWithTaggedFriendsIDs());

//		handleLikes(post.getLikesInfo().getFriendsUserIDs(), actorUser, owningWallUserId);
//		handleLikes(post.getLikesInfo().getSamplesUserIDs(), actorUser, owningWallUserId);
		handleLikes(post.getLikesInfo(), actorUser, owningWallUserId);
	}
	
	private void handleAuthorUser(User actorUser, Post post) {
		actorUser.addOwnPostOnOwnWall(post);
		int shareCount = post.getShareCount();
		if (shareCount>0)
			actorUser.incrementOwnPostResharing(shareCount); // node attribute
	}
	
	/**
	 * handle post as interaction between post author user and target user (= owning wall user) 
	 * @param actorUser
	 * @param post
	 * @param targetUserId
	 */
	private void handleTargetUser(User actorUser, Post post, String targetUserId) {
		actorUser.getToOtherUserInteractions(targetUserId).addPost(post);
	}
	
	private void handleTags(Post post, User actorUser, Set<String> taggedIds) {
		for (String taggedId : post.getTaggedIDs()) {
			actorUser.getToOtherUserInteractions(taggedId).incrementTags();
		}
	}
	
	/**
	 * here we handle the post likes: maintain a counter for interacting user, that is:
	 * "if a user like a post, then he's interacting with an other user (friends or not) which wall 
	 * is containing the post; in addition, a counter for liked post will be incremented in actor user instance   
	 * @param likingUsersIds
	 * @param wallOwnerId
	 */
	private void handleLikes(LikesInfo likesInfo,/*Set<String> likingUsersIds,*/ User actorUser, String wallOwnerId) {
		// interactingUsersIds could be friends of user owning wall - it depends on wall privacy		
		int howLikes = likesInfo.getCount();
		actorUser.incrementOwnLikedPosts(howLikes);
//				likingUsersIds.size();
		Set<String> friendsUserIDs = likesInfo.getFriendsUserIDs();
		Set<String> samplesUserIDs = likesInfo.getSamplesUserIDs();
		Set<String> allUser = new HashSet<>();
		allUser.addAll(friendsUserIDs);
		allUser.addAll(samplesUserIDs);
//		if (howLikes > 0) {
			for (String interactingUserId: allUser) {
				// create owner user if it doesn't exist
				world.isExistentUserOrCreateNew(wallOwnerId);
				
				// create interacting user if it doesn't exist
				world
					.isExistentUserOrCreateNew(interactingUserId)
						// then use for its likes, that is a link attribute
						.getToOtherUserInteractions(wallOwnerId).incrementLikes();
			}
			actorUser.incrementOwnLikedPosts(howLikes); // node attribute
//		}
	}

}
