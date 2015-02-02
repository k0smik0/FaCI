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
package net.iubris.facri._di.guice.module.parser;

import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import net.iubris.facri._di.annotations.grapher.corpus.CorpusPathPrefix;
import net.iubris.facri._di.annotations.parser.filenamefilters.CommentsFilenameFilter;
import net.iubris.facri._di.annotations.parser.filenamefilters.FeedsDirFilenameFilter;
import net.iubris.facri._di.annotations.parser.filenamefilters.PostsFilenameFilter;
import net.iubris.facri._di.providers.parser.CorpusPathPrefixProvider;
import net.iubris.facri._di.providers.parser.filenamefilters.CommentsFilenameFilterProvider;
import net.iubris.facri._di.providers.parser.filenamefilters.FeedsDirFilenameFilterProvider;
import net.iubris.facri._di.providers.parser.filenamefilters.PostsFilenameFilterProvider;
import net.iubris.facri._di.providers.parser.mappers.CommentsHolderMapperProvider;
import net.iubris.facri._di.providers.parser.mappers.PostsMapperProvider;
import net.iubris.facri.model.parser.comments.CommentsHolder;
import net.iubris.facri.model.parser.posts.Posts;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class FacriParserModule extends AbstractModule {

	@Override
	protected void configure() {

		try {
			Properties properties = new Properties();
			properties.load(new FileReader("facri.properties"));
			Names.bindProperties(binder(), properties);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		bind(FilenameFilter.class).annotatedWith(FeedsDirFilenameFilter.class).toProvider(FeedsDirFilenameFilterProvider.class);
		bind(FilenameFilter.class).annotatedWith(PostsFilenameFilter.class).toProvider(PostsFilenameFilterProvider.class);
		bind(FilenameFilter.class).annotatedWith(CommentsFilenameFilter.class).toProvider(CommentsFilenameFilterProvider.class);
		
		bind( new TypeLiteral<JsonXMLMapper<Posts>>(){}).toProvider(PostsMapperProvider.class);
		bind( new TypeLiteral<JsonXMLMapper<CommentsHolder>>(){}).toProvider(CommentsHolderMapperProvider.class);
		
		bind(String.class).annotatedWith(CorpusPathPrefix.class).toProvider(CorpusPathPrefixProvider.class);
		
//		install(new GephiGraphModule());
//		install(new InteractiveConsoleModule());
		
//		install(new GraphstreamModule());
	}
}
