package net.iubris.facri.parsers;

import java.util.Map;
import java.util.Set;

import net.iubris.facri.model.Post;
import net.iubris.facri.model.User;

public class PostParser {

	public void parse(Post post, String owningWallUserId, /*int i,*/ Map<String, User> useridToUserMap) {
// System.out.print("\tpost ["+i+"]: "+post.getPostId()+", ");
		// the actorId is the post author id
		String actorPostId = post.getActorId();

		// we use actorId because a wall contains both posts
		// from owner (actorId == userDir) or other users (actorId != userDir)
		User user = ParsingUtils.isExistentUserOrCreateEmpty(actorPostId, useridToUserMap);
		user.addOwnPost(post); // always add post to its author

		if (actorPostId == owningWallUserId) { // post author is the wall owner
			handleActorUser(user, post);
		} else if (actorPostId != owningWallUserId) {
			handleTargetUser(user, post, owningWallUserId);
		}

		handleTags(post, user, post.getTaggedIDs());
		handleTags(post, user, post.getWithTaggedFriendsIDs());

		handleLikes(post.getLikesInfo().getFriendsUserIDs(), user, owningWallUserId, useridToUserMap);
		handleLikes(post.getLikesInfo().getSamplesUserIDs(), user, owningWallUserId, useridToUserMap);
	}
	
	private void handleActorUser(User user, Post post) {
		int shareCount = post.getShareCount();
		if (shareCount >0)
			user.incrementOwnPostResharing(shareCount);
	}
	
	private void handleTargetUser(User user, Post post, String targetUserId) {
//		user.addToSomeoneElsePost(targetUserId, post);
		user.getOtherUserMapInteractions(targetUserId).addPost(post);
	}
	
	private void handleTags(Post post, User user, Set<String> taggedIds) {
		for (String taggedId : post.getTaggedIDs()) {
			user.getOtherUserMapInteractions(taggedId).incrementTags();
		}
	}
	
	/**
	 * here we handle the post likes: maintain a counter for interacting user, that is:
	 * "if a user like a post, then he interacts with an other user (friends or not) which wall 
	 * is containing the post; in addition, a counter for liked post is incremented, in actor user instance   
	 * @param likingUsersIds
	 * @param wallOwnerId
	 */
	private void handleLikes(Set<String> likingUsersIds, User actorUser, String wallOwnerId, Map<String, User> useridToUserMap) {
		// interactingUsersIds are owning wall user's friends or not - it depends on wall privacy		
		int howLikes = likingUsersIds.size();
		if (howLikes >0) {
			for (String interactingUserId : likingUsersIds) {
				User interactingUser = ParsingUtils.isExistentUserOrCreateEmpty(interactingUserId, useridToUserMap);
				useridToUserMap.put(interactingUserId, interactingUser);
				
				// create user if it doesn't exist
				if (!useridToUserMap.containsKey(wallOwnerId))
					useridToUserMap.put(wallOwnerId, ParsingUtils.isExistentUserOrCreateEmpty(wallOwnerId, useridToUserMap));
				
				interactingUser.getOtherUserMapInteractions(wallOwnerId).incrementLikes();
			}
			actorUser.incrementOwnPostsLiking(howLikes);
		}
	}

}
