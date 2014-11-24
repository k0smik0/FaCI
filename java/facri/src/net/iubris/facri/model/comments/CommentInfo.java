package net.iubris.facri.model.comments;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author "Massimiliano Leone - maximilianus@gmail.com"
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentInfo implements Serializable {

	private static final long serialVersionUID = -7878220575677233802L;
	// can_comment":"1","comment_count":"0","comment_order":"chronological
	
	@XmlElement(name = "comment_count")
//	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private int commentCount;

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
}
