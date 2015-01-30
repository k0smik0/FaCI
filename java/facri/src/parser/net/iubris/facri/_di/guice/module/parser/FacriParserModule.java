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
