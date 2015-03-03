/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FacriParserModule.java) is part of facri.
 * 
 *     FacriParserModule.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FacriParserModule.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser._di._guice.module;

import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import net.iubris.faci.parser._di.annotations.datafiles.FeedsFriendsDirname;
import net.iubris.faci.parser._di.annotations.datafiles.FeedsMeDataDirname;
import net.iubris.faci.parser._di.annotations.datafiles.FriendsIdsFilename;
import net.iubris.faci.parser._di.annotations.datafiles.FriendsInfoFilename;
import net.iubris.faci.parser._di.annotations.datafiles.FriendsOfFriendIdsFilename;
import net.iubris.faci.parser._di.annotations.datafiles.FriendsOfFriendsInfoFilename;
import net.iubris.faci.parser._di.annotations.datafiles.MeInfoFilename;
import net.iubris.faci.parser._di.annotations.datafiles.MutualFriendsInfoFilename;
import net.iubris.faci.parser._di.annotations.filenamefilters.CommentsFilenameFilter;
import net.iubris.faci.parser._di.annotations.filenamefilters.FeedsDirFilenameFilter;
import net.iubris.faci.parser._di.annotations.filenamefilters.PostsFilenameFilter;
import net.iubris.faci.parser._di.providers.datafiles.FeedsFriendsDataDirnameProvider;
import net.iubris.faci.parser._di.providers.datafiles.FeedsMeDataDirnameProvider;
import net.iubris.faci.parser._di.providers.datafiles.FriendsIdsFilenameProvider;
import net.iubris.faci.parser._di.providers.datafiles.FriendsInfoFilenameProvider;
import net.iubris.faci.parser._di.providers.datafiles.FriendsOfFriendIdsFilenameProvider;
import net.iubris.faci.parser._di.providers.datafiles.FriendsOfFriendsInfoFilenameProvider;
import net.iubris.faci.parser._di.providers.datafiles.MeInfoFilenameProvider;
import net.iubris.faci.parser._di.providers.datafiles.MutualFriendsInfoFilenameProvider;
import net.iubris.faci.parser._di.providers.filenamefilters.CommentsFilenameFilterProvider;
import net.iubris.faci.parser._di.providers.filenamefilters.FeedsDirFilenameFilterProvider;
import net.iubris.faci.parser._di.providers.filenamefilters.PostsFilenameFilterProvider;
import net.iubris.faci.parser._di.providers.mappers.CommentsHolderMapperProvider;
import net.iubris.faci.parser._di.providers.mappers.PostsMapperProvider;
import net.iubris.faci.parser.model.comments.CommentsHolder;
import net.iubris.faci.parser.model.posts.Posts;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {

		try {
			Properties properties = new Properties();
			properties.load(new FileReader("faci.properties"));
			Names.bindProperties(binder(), properties);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		bind(FilenameFilter.class).annotatedWith(FeedsDirFilenameFilter.class).toProvider(FeedsDirFilenameFilterProvider.class);
		bind(FilenameFilter.class).annotatedWith(PostsFilenameFilter.class).toProvider(PostsFilenameFilterProvider.class);
		bind(FilenameFilter.class).annotatedWith(CommentsFilenameFilter.class).toProvider(CommentsFilenameFilterProvider.class);
		
		// ego
		bind(String.class).annotatedWith(FeedsMeDataDirname.class).toProvider(FeedsMeDataDirnameProvider.class);
		bind(String.class).annotatedWith(MeInfoFilename.class).toProvider(MeInfoFilenameProvider.class);
		bind(String.class).annotatedWith(FriendsInfoFilename.class).toProvider(FriendsInfoFilenameProvider.class);
		bind(String.class).annotatedWith(FriendsIdsFilename.class).toProvider(FriendsIdsFilenameProvider.class);
		bind(String.class).annotatedWith(MutualFriendsInfoFilename.class).toProvider(MutualFriendsInfoFilenameProvider.class);
		bind(String.class).annotatedWith(FeedsFriendsDirname.class).toProvider(FeedsFriendsDataDirnameProvider.class);
		bind(String.class).annotatedWith(FriendsOfFriendsInfoFilename.class).toProvider(FriendsOfFriendsInfoFilenameProvider.class);
		bind(String.class).annotatedWith(FriendsOfFriendIdsFilename.class).toProvider(FriendsOfFriendIdsFilenameProvider.class);
		
		bind( new TypeLiteral<JsonXMLMapper<Posts>>(){}).toProvider(PostsMapperProvider.class);
		bind( new TypeLiteral<JsonXMLMapper<CommentsHolder>>(){}).toProvider(CommentsHolderMapperProvider.class);
		
//		bind(String.class).annotatedWith(CorpusPathPrefix.class).toProvider(CorpusPathPrefixProvider.class);
		
//		install(new GephiGraphModule());
//		install(new InteractiveConsoleModule());
		
//		install(new GraphstreamModule());
	}
}
