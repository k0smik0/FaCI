package net.iubris.facri.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CommentsData {

	@XmlElement(name = "post_id")
	private String postId;
	
	@XmlElement(name = "comment_data")
	private List<Comment> comments;

	public final String getPostId() {
		return postId;
	}
	
	public final List<Comment> getCommentsData() {
		return comments;
	}
	
	@Override
	public String toString() {
		return postId+", "+comments;
	}
}
