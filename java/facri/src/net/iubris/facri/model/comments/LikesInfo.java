package net.iubris.facri.model.comments;

import java.io.Serializable;
import java.net.URL;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.iubris.facri.model.adapters.URLAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class LikesInfo implements Serializable {

	private static final long serialVersionUID = 6999503471597138387L;

	@XmlElement(name="href")
	@XmlJavaTypeAdapter(URLAdapter.class)
	private URL likedPostUrl;

	@XmlElement(name="count")
	private int count;
	
	@XmlElementWrapper(name="sample")
//	@XmlElement(name="sample")
	private Set<String> samplesUserIDs;

	@XmlElementWrapper(name="friends")
//	@XmlElement(name="friends")
	private Set<String> friendsUserIDs;
	
//	@XmlElement(name="user_likes")
//	private boolean userLikes;

	public URL getLikedPostUrl() {
		return likedPostUrl;
	}

	public void setLikedPostUrl(URL likedPostUrl) {
		this.likedPostUrl = likedPostUrl;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Set<String> getSamplesUserIDs() {
		return samplesUserIDs;
	}

	public void setSamplesUserIDs(Set<String> samplesUserIDs) {
		this.samplesUserIDs = samplesUserIDs;
	}

	public Set<String> getFriendsUserIDs() {
		return friendsUserIDs;
	}

	public void setFriendsUserIDs(Set<String> friendsUserIDs) {
		this.friendsUserIDs = friendsUserIDs;
	}

//	public boolean isUserLikes() {
//		return userLikes;
//	}
	

	

//		"can_like":"1"
	
	

}
