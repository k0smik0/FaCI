package net.iubris.facri.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.iubris.facri.model.adapter.DateAdapter;

public class Comment {

	@XmlElement(name="from_id")
	private String fromId;
	
	@XmlElement(name="text")
	private String text;
	
	@XmlElement(name="time")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date time;

	public final String getFromId() {
		return fromId;
	}

	public final void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public final String getText() {
		return text;
	}

	public final void setText(String text) {
		this.text = text;
	}

	public final Date getTime() {
		return time;
	}

	public final void setTime(Date time) {
		this.time = time;
	}
	
}
