package net.iubris.facri.parsers.posts;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;

import net.iubris.facri.model.World;
import net.iubris.facri.model.comments.LikesInfo;
import net.iubris.facri.model.posts.Post;
import net.iubris.facri.model.users.User;

public class PostParser {
	
	private final World world;
	
	@Inject
	public PostParser(World world) {
		this.world = world;
	}

	public void parse(Post post, String owningWallUserId) {
		// always owning wall user
		if (owningWallUserId.isEmpty())
			System.out.println("NO_A");
//		User existentUserOrCreateNew = 
				world.isExistentUserOrCreateNew(owningWallUserId);
//		System.out.println(existentUserOrCreateNew.getUId()+" "+existentUserOrCreateNew.getClass() );
//		System.out.println(world.getMyFriendsMap().size());
		if (world.getMyFriendsMap().containsKey(owningWallUserId))
			System.out.println(owningWallUserId+": friend");

		// the actorId is the post author id
		String actorPostId = post.getActorId();
		
		// we use actorId because a wall contains both posts
		// from owner (actorId == userDir) or other users (actorId != userDir)
		if (actorPostId.isEmpty())
			System.out.println("NO_B");
		User actorUser = world.isExistentUserOrCreateNew(actorPostId);
		// always add post to its author
		actorUser.addOwnPost(post);

		if (actorPostId == owningWallUserId) { // post author is the same of the wall owner
			handleAuthorUser(actorUser, post);
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
	private void handleLikes(LikesInfo likesInfo, User actorUser, String wallOwnerId) {
		// interactingUsersIds could be friends of user owning wall - it depends on wall privacy		
		int howLikes = likesInfo.getCount();
		actorUser.incrementOwnLikedPosts(howLikes);
//				likingUsersIds.size();
		Set<String> friendsUserIDs = likesInfo.getFriendsUserIDs();
		Set<String> samplesUserIDs = likesInfo.getSamplesUserIDs();
		Set<String> allUsers = new HashSet<>();
		allUsers.addAll(friendsUserIDs);
		allUsers.addAll(samplesUserIDs);
//		if (howLikes > 0) {
//		for (String interactingUserId: allUsers) {
		allUsers.forEach( 
				new Consumer<String>() {
					public void accept(String interactingUserId) {
			
				// create owner user if it doesn't exist
				if (wallOwnerId.isEmpty())
					System.out.println("NO");
				world.isExistentUserOrCreateNew(wallOwnerId);
				
				// create interacting user if it doesn't exist
				if (!interactingUserId.isEmpty()) {
//					System.out.println("NO2");
					world
						.isExistentUserOrCreateNew(interactingUserId)
							// then use for its likes, that is a link attribute
							.getToOtherUserInteractions(wallOwnerId).incrementLikes();
				}
			}
				});
			actorUser.incrementOwnLikedPosts(howLikes); // node attribute
//		}
	}

}
