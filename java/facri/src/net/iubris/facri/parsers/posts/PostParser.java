package net.iubris.facri.parsers.posts;

import java.util.Set;

import javax.inject.Inject;

import net.iubris.facri.model.World;
import net.iubris.facri.model.posts.Post;
import net.iubris.facri.model.users.User;

public class PostParser {

	private final World world;
	
	@Inject
	public PostParser(World world) {
		this.world = world;
	}

	public void parse(Post post, String owningWallUserId /*, int i,*/ /*Map<String, User> useridToUserMap*/) {
// System.out.print("\tpost ["+i+"]: "+post.getPostId()+", ");
		// the actorId is the post author id
		String actorPostId = post.getActorId();

		// we use actorId because a wall contains both posts
		// from owner (actorId == userDir) or other users (actorId != userDir)
		User actorUser = 
//				ParsingUtils.isExistentFriendUserOrCreateEmpty(actorPostId, useridToUserMap);
				world.isExistentUserOrCreateNew(actorPostId);
		actorUser.addOwnPost(post); // always add post to its author

		if (actorPostId == owningWallUserId) { // post author is the same of the wall owner
			handleAuthorUser(actorUser, post);
		} else /*if (actorPostId != owningWallUserId)*/ {
			handleTargetUser(actorUser, post, owningWallUserId);
		}

		handleTags(post, actorUser, post.getTaggedIDs());
		handleTags(post, actorUser, post.getWithTaggedFriendsIDs());

		handleLikes(post.getLikesInfo().getFriendsUserIDs(), actorUser, owningWallUserId/*, useridToUserMap*/);
		handleLikes(post.getLikesInfo().getSamplesUserIDs(), actorUser, owningWallUserId/*, useridToUserMap*/);
	}
	
	private void handleAuthorUser(User actorUser, Post post) {
		int shareCount = post.getShareCount();
		if (shareCount>0)
			actorUser.incrementOwnPostResharing(shareCount);
	}
	
	/**
	 * handle post as interaction between post author user and target user (= owning wall user) 
	 * @param actorUser
	 * @param post
	 * @param targetUserId
	 */
	private void handleTargetUser(User actorUser, Post post, String targetUserId) {
//		user.addToSomeoneElsePost(targetUserId, post);
		actorUser.getOtherUserInteractions(targetUserId).addPost(post);
	}
	
	private void handleTags(Post post, User user, Set<String> taggedIds) {
		for (String taggedId : post.getTaggedIDs()) {
			user.getOtherUserInteractions(taggedId).incrementTags();
		}
	}
	
	/**
	 * here we handle the post likes: maintain a counter for interacting user, that is:
	 * "if a user like a post, then he's interacting with an other user (friends or not) which wall 
	 * is containing the post; in addition, a counter for liked post will be incremented in actor user instance   
	 * @param likingUsersIds
	 * @param wallOwnerId
	 */
	private void handleLikes(Set<String> likingUsersIds, User actorUser, String wallOwnerId/*, Map<String, User> useridToUserMap*/) {
		// interactingUsersIds could be friends of user owning wall - it depends on wall privacy		
		int howLikes = likingUsersIds.size();
		if (howLikes > 0) {
			for (String interactingUserId: likingUsersIds) {
				// create owner user if it doesn't exist
//				if (!useridToUserMap.containsKey(wallOwnerId))
//					useridToUserMap.put(wallOwnerId, ParsingUtils.isExistentUserOrCreateEmpty(wallOwnerId, useridToUserMap));
//				ParsingUtils.isExistentFriendUserOrCreateEmpty(wallOwnerId, useridToUserMap);
//				User wallOwnerUser = 
						world.isExistentUserOrCreateNew(wallOwnerId);
				
				// create interacting user if it doesn't exist
//				User interactingUser = 
//						ParsingUtils.isExistentFriendUserOrCreateEmpty(interactingUserId, useridToUserMap);
						world.isExistentUserOrCreateNew(interactingUserId)
//						;
//				useridToUserMap.put(interactingUserId, interactingUser);
				
//				interactingUser
				.getOtherUserInteractions(wallOwnerId).incrementLikes();
			}
			actorUser.incrementOwnPostsLiked(howLikes);
		}
	}

}