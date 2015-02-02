package net.iubris.facri.persister.persisters.base;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import net.iubris.facri.persister.helpers.base.FacriBerkeleyDBHelper;

@SuppressWarnings("rawtypes")
@Singleton
public class HelpersHolder {

	private Set<FacriBerkeleyDBHelper> helpers = new HashSet<>();

	public Set<FacriBerkeleyDBHelper> getHelpers() {
		return helpers;
	}

	public void addHelper(FacriBerkeleyDBHelper facriBerkeleyDBHelper) {
		System.out.println("adding ");
		this.helpers.add(facriBerkeleyDBHelper);
	}
}