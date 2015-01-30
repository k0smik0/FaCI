package net.iubris.facri.model.parser.posts;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Type {
	
	GROUP_CREATED(11),
	EVENT_CREATED(12),
	STATUS_UPDATE(46),
	POST_ON_WALL_FROM_ANOTHER_USER(56),
	NOTE_CREATED(66),
	LINK_POSTED(80),
	VIDEO_POSTED(128),
	PHOTOS_POSTED(247),
	APP_STORY(237),
	COMMENT_CREATED(257),
	APP_STORY_2(272),
	CHECKIN_TO_A_PLACE(285),
	POST_IN_GROUP(308);

	
	Type(int code){
		this.code=code;
	}
	protected int code;

	public int getCode() {
		return this.code;
	}
	
	private static final Map<Integer,Type> lookup = new HashMap<>();
	static {
		for (Type s : EnumSet.allOf(Type.class))
			lookup.put(s.getCode(), s);
	}
	
	public static Type get(int code) {
		return lookup.get(code);
	}
}
