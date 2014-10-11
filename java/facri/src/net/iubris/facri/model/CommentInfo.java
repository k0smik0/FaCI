package net.iubris.facri.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author "Massimiliano Leone - maximilianus@gmail.com"
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentInfo {

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
