/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FeedsDirFilenameFilterProvider.java) is part of facri.
 * 
 *     FeedsDirFilenameFilterProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FeedsDirFilenameFilterProvider.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
