package net.iubris.facri.persisters.base;

import java.net.MalformedURLException;
import java.net.URL;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PersistentProxy;

@Persistent(proxyFor=URL.class)
public class UrlPersisterProxy implements PersistentProxy<URL> {

	private String urlString;

	@Override
	public URL convertProxy() {
		try {
			return new URL(urlString);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public void initializeProxy(URL url) {
		urlString = url.toString();
	}
}
