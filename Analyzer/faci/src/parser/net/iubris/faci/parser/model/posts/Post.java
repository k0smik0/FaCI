/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Post.java) is part of facri.
 * 
 *     Post.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Post.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.model.posts;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.iubris.faci.parser.model.adapters.DateAdapter;
import net.iubris.faci.parser.model.adapters.TypeAdapter;
import net.iubris.faci.parser.model.adapters.URLAdapter;
import net.iubris.faci.parser.model.comments.CommentData;
import net.iubris.faci.parser.model.comments.CommentInfo;
import net.iubris.faci.parser.model.comments.LikesInfo;

import com.sleepycat.persist.model.Persistent;
/**
 * 
 * @author Massimiliano Leone - maximilianus@gmail.com - http://plus.google.com/+MassimilianoLeone
 *
 * {@link https://developers.facebook.com/docs/reference/fql/stream/}
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Persistent
public class Post implements Serializable, Comparable<Post> {

	private static final long serialVersionUID = -7310605935366057489L;

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
	
//	@XmlElementWrapper(name="tagged_ids")
	@XmlElement(name="tagged_ids")
	private Set<String> taggedIDs;
	
	
//	@XmlElement(name="type")
	@XmlJavaTypeAdapter(TypeAdapter.class)
	private Type type;
	
	@XmlElement(name="updated_time")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date updatedTime;
	
	@XmlElement(name="with_tags")
//	@XmlElementWrapper(name="with_tags")
//	@XmlElement(name="with_tags")
	private Set<String> withTaggedFriendsIDs;

	private List<CommentData> comments;

	/**
	 * @return the post author id
	 */
	public String getActorId() {
		return actorId;
	}

//	public void setActorId(String actorId) {
//		this.actorId = actorId;
//	}

	/**
	 * @return the post id
	 */
	public String getPostId() {
		return postId;
	}

//	public void setPostId(String postId) {
//		this.postId = postId;
//	}

	/**
	 * @return a {@link net.iubris.faci.parser.model.comments.CommentInfo} instance
	 */
	public CommentInfo getCommentInfo() {
		return commentInfo;
	}

//	public void setCommentInfo(CommentInfo commentInfo) {
//		this.commentInfo = commentInfo;
//	}

	public Date getCreatedTime() {
		return createdTime;
	}

//	public void setCreatedTime(Date createdTime) {
//		this.createdTime = createdTime;
//	}

	public boolean isPopular() {
		return isPopular;
	}

//	public void setPopular(boolean isPopular) {
//		this.isPopular = isPopular;
//	}

	public LikesInfo getLikesInfo() {
		return likesInfo;
	}

//	public void setLikesInfo(LikesInfo likesInfo) {
//		this.likesInfo = likesInfo;
//	}

	public URL getPermalink() {
		return permalink;
	}

//	public void setPermalink(URL permalink) {
//		this.permalink = permalink;
//	}

	public int getShareCount() {
		return shareCount;
	}

//	public void setShareCount(int shareCount) {
//		this.shareCount = shareCount;
//	}

	public String getSourceID() {
		return sourceID;
	}

//	public void setSourceID(String sourceID) {
//		this.sourceID = sourceID;
//	}

	public Set<String> getTaggedIDs() {
		return taggedIDs;
	}

//	public void setTaggedIDs(Set<String> taggedIDs) {
//		this.taggedIDs = taggedIDs;
//	}

	public Type getType() {
		return type;
	}

//	public void setType(Type type) {
//		this.type = type;
//	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

//	public void setUpdatedTime(Date updatedTime) {
//		this.updatedTime = updatedTime;
//	}

	public Set<String> getWithTaggedFriendsIDs() {
		return withTaggedFriendsIDs;
	}

	public void setWithTaggedFriendsIDs(Set<String> withTaggedFriendsIDs) {
		this.withTaggedFriendsIDs = withTaggedFriendsIDs;
	}

	public void setComments(List<CommentData> comments) {
		this.comments = comments;		
	}
	public List<CommentData> getComments() {
		return comments;
	}

	@Override
	public int compareTo(Post post) {
		return (post.getPostId().hashCode()>postId.hashCode()? 1: -1);
	}	
}
