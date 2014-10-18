package net.iubris.facri.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.odysseus.staxon.json.jaxb.JsonXML;

@JsonXML(autoArray=true,virtualRoot=true)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Comments {
	
	@XmlElement(name = "post_id")
	private String postId;
	
	@XmlElement(name = "comment_data")
	private List<Comment> comments;

	public final String getPostId() {
		return postId;
	}

	public final void setPostId(String postId) {
		this.postId = postId;
	}

	public final List<Comment> getComments() {
		return comments;
	}

	public final void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
}
