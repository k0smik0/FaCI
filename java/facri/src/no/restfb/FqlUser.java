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
