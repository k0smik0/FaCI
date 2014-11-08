package net.iubris.facri._di.providers.filenamefilters;

import java.io.File;
import java.io.FilenameFilter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class PostsFilenameFilterProvider implements Provider<FilenameFilter> {

	private FilenameFilter filter;
	
	@Inject
	public PostsFilenameFilterProvider(@Named("post_file_regex") String postFileRegex) {
	 this.filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.startsWith("posts") && name.endsWith(".json")) {
//				if (name.matches(postFileRegex)) {
//					System.out.print("\t["+dir.getName()+" "+name+" "+postFileRegex+": ");
//					System.out.println(true+"]");
					return true;
				}
//				System.out.println("]");
				return false;
			}
		};
	}
	
	@Override
	public FilenameFilter get() {
		return filter;
	}
}
