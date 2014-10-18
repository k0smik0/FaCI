package net.iubris.facri._di.provider.filenamefilter;

import java.io.File;
import java.io.FilenameFilter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class CommentFilenameFilterProvider implements Provider<FilenameFilter> {

	private FilenameFilter filter;
	
	@Inject
	public CommentFilenameFilterProvider(@Named("comment_file_regex") String postFileRegex) {
	 this.filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.matches(postFileRegex))
					return true;
				return false;
			}
		};
	}
	
	@Override
	public FilenameFilter get() {
		return filter;
	}
}
