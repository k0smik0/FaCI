package net.iubris.facri._di.providers.parser.filenamefilters;

import java.io.File;
import java.io.FilenameFilter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class CommentsFilenameFilterProvider implements Provider<FilenameFilter> {

	private FilenameFilter filter;
	
	@Inject
	public CommentsFilenameFilterProvider(@Named("comment_file_regex") String postFileRegex) {
	 this.filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith("comments.json")) {
//				if (name.matches(postFileRegex)) {
//					System.out.println(name);
					return true;
				}
				return false;
			}
		};
	}
	
	@Override
	public FilenameFilter get() {
		return filter;
	}
}
