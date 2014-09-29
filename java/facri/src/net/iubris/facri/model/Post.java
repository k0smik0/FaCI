package net.iubris.facri.model;

import java.net.URL;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/**
 * 
 * @author Massimiliano Leone - maximilianus@gmail.com - http://plus.google.com/+MassimilianoLeone
 *
 * {@link https://developers.facebook.com/docs/reference/fql/stream/}
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Post {

	@XmlElement(name = "actor_id")
	private String actorId;
	
	@XmlElement(name = "post_id")
	private String postId;
	
	@XmlElement(name = "comment_info")
	private CommentInfo commentInfo;

	
	@XmlElement(name="created_time")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date createdTime;
	
	@XmlElement(name="is_popular")
	private boolean isPopular;
	
	@XmlElement(name="likes")		
	private LikesInfo likesInfo;

	
//	"permalink":"https:\/\/www.facebook.com\/album.php?fbid=10154629114300099&id=836460098&aid=1073741877",
	@XmlElement(name="permalink")
	@XmlJavaTypeAdapter(URLAdapter.class)
	private URL permalink;
	
	/*"privacy": {
		"description": "Public",
		"value":"EVERYONE",
		"friends":"",
		"networks":"",
		"allow":"","deny":""
	},*/
	
	@XmlElement(name="share_count")
	private int shareCount;
	
	@XmlElement(name="source_id")
	private String sourceID;	
	
	@XmlElementWrapper(name="tagged_ids")
//	@XmlElement(name="tagged_ids")
	private Set<String> taggedIDs;
	
	
//	@XmlElement(name="type")
	@XmlJavaTypeAdapter(TypeAdapter.class)
	private Type type;
	
	@XmlElement(name="updated_time")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date updatedTime;
	
	@XmlElementWrapper(name="with_tags")
//	@XmlElement(name="with_tags")
	private Set<String> withTaggedFriendsIDs;

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public CommentInfo getCommentInfo() {
		return commentInfo;
	}

	public void setCommentInfo(CommentInfo commentInfo) {
		this.commentInfo = commentInfo;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isPopular() {
		return isPopular;
	}

	public void setPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}

	public LikesInfo getLikesInfo() {
		return likesInfo;
	}

	public void setLikesInfo(LikesInfo likesInfo) {
		this.likesInfo = likesInfo;
	}

	public URL getPermalink() {
		return permalink;
	}

	public void setPermalink(URL permalink) {
		this.permalink = permalink;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public Set<String> getTaggedIDs() {
		return taggedIDs;
	}

	public void setTaggedIDs(Set<String> taggedIDs) {
		this.taggedIDs = taggedIDs;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Set<String> getWithTaggedFriendsIDs() {
		return withTaggedFriendsIDs;
	}

	public void setWithTaggedFriendsIDs(Set<String> withTaggedFriendsIDs) {
		this.withTaggedFriendsIDs = withTaggedFriendsIDs;
	}
	
}
