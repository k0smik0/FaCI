package net.iubris.facri._di.providers.parser.filenamefilters;

import java.io.File;
import java.io.FilenameFilter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class FeedsDirFilenameFilterProvider implements Provider<FilenameFilter> {

	private FilenameFilter filter;
	
	@Inject
	public FeedsDirFilenameFilterProvider(@Named("feeds_dir_relative_path") String feedsDirRelativePath) {
	 this.filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.equals(feedsDirRelativePath)) {
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
