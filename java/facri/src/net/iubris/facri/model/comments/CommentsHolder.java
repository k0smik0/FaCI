package net.iubris.facri.model.comments;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.odysseus.staxon.json.jaxb.JsonXML;

@JsonXML(autoArray=false,virtualRoot=true)
@XmlRootElement
//(name="comments")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentsHolder {
	
	@XmlElement(name = "comments")
	private CommentsData commentsData;
	
	public CommentsData getCommentsData() {
		return commentsData;
	}

	@Override
	public String toString() {
		return hashCode()+": "+commentsData/*+", "+commentsData.size()*/;
	}
}
