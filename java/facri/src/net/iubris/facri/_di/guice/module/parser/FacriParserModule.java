package net.iubris.facri._di.guice.module.parser;

import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import net.iubris.facri._di.annotations.corpus.CorpusPrefix;
import net.iubris.facri._di.annotations.filenamefilters.CommentsFilenameFilter;
import net.iubris.facri._di.annotations.filenamefilters.FeedsDirFilenameFilter;
import net.iubris.facri._di.annotations.filenamefilters.PostsFilenameFilter;
import net.iubris.facri._di.guice.module.graphgenerators.gephi.GephiGraphModule;
import net.iubris.facri._di.providers.corpusprefix.CorpusPrefixProvider;
import net.iubris.facri._di.providers.filenamefilters.CommentsFilenameFilterProvider;
import net.iubris.facri._di.providers.filenamefilters.FeedsDirFilenameFilterProvider;
import net.iubris.facri._di.providers.filenamefilters.PostsFilenameFilterProvider;
import net.iubris.facri._di.providers.mappers.CommentsHolderMapperProvider;
import net.iubris.facri._di.providers.mappers.PostsMapperProvider;
import net.iubris.facri.model.comments.CommentsHolder;
import net.iubris.facri.model.posts.Posts;

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
		
		bind(String.class).annotatedWith(CorpusPrefix.class).toProvider(CorpusPrefixProvider.class);
		
//		install(new GephiGraphModule());
	}
}
