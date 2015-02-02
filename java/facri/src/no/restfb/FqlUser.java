/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FqlUser.java) is part of facri.
 * 
 *     FqlUser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FqlUser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.restfb;

import java.net.MalformedURLException;
import java.net.URL;
import com.restfb.Facebook;

public class FqlUser {
	
	@Facebook()
	String id;
	
	@Facebook
	String name;
	
	@Facebook("pic")
	String fql_pic;
	
	URL pic;
	
	@Facebook
	String type;
	
	@Facebook
	String fql_url;
	
	URL url;
	
	@Facebook
	String username;

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public URL getPic() {
		if (pic==null) {
			try {
				pic = new URL(fql_pic);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return pic;
	}

	public String getType() {
		return this.type;
	}

	public URL getUrl() {
		if (url==null) {
			try {
				url = new URL(fql_url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}		
		return url;
	}

	public String getUsername() {
		return this.username;
	}

}
