package net.iubris.facri.model;

import java.net.URL;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class LikesInfo {


	@XmlElement(name="likes")
	@XmlJavaTypeAdapter(URLAdapter.class)
	private URL likingFriendsPage;

	private int count;
	
	@XmlElementWrapper(name="sample")
//	@XmlElement(name="sample")
	private Set<String> samplesUserIDs;

	@XmlElementWrapper(name="friends")
//	@XmlElement(name="friends")
	private Set<String> friendsUserIDs;
	
	@XmlElement(name="user_likes")
	private boolean userLikes;

	public URL getLikingFriendsPage() {
		return likingFriendsPage;
	}

	public void setLikingFriendsPage(URL likingFriendsPage) {
		this.likingFriendsPage = likingFriendsPage;
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

	public boolean isUserLikes() {
		return userLikes;
	}

	public void setUserLikes(boolean userLikes) {
		this.userLikes = userLikes;
	}

	

//		"can_like":"1"
	
	

}
