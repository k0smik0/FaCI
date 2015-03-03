/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Type.java) is part of facri.
 * 
 *     Type.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Type.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.model.posts;

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
