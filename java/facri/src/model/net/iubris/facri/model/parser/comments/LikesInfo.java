package net.iubris.facri.model.parser.comments;

import java.io.Serializable;
import java.net.URL;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sleepycat.persist.model.Persistent;

import net.iubris.facri.model.parser.adapters.URLAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Persistent
public class LikesInfo implements Serializable {

	private static final long serialVersionUID = 6999503471597138387L;

	@XmlElement(name="href")
	@XmlJavaTypeAdapter(URLAdapter.class)
	private URL likedPostUrl;

	@XmlElement(name="count")
	private int count;
	
	@XmlElement(name="sample")
//	@XmlElementWrapper(name="sample")
//	@XmlElement(name="sample")
	private Set<String> samplesUserIDs;

	@XmlElement(name="friends")
//	@XmlElementWrapper(name="friends")
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

//	public void setSamplesUserIDs(Set<String> samplesUserIDs) {
//		this.samplesUserIDs = samplesUserIDs;
//	}

	public Set<String> getFriendsUserIDs() {
		return friendsUserIDs;
	}

//	public void setFriendsUserIDs(Set<String> friendsUserIDs) {
//		this.friendsUserIDs = friendsUserIDs;
//	}
	
	@Override
	public String toString() {
		return count+" "+friendsUserIDs.size()+" "+samplesUserIDs.size();
	}

//	public boolean isUserLikes() {
//		return userLikes;
//	}
	

	

//		"can_like":"1"
	
	

}
