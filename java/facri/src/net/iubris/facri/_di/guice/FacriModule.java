package net.iubris.facri._di.guice;

import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import net.iubris.facri._di.annotation.filenamefilter.CommentFilenameFilter;
import net.iubris.facri._di.annotation.filenamefilter.PostFilenameFilter;
import net.iubris.facri._di.provider.filenamefilter.CommentFilenameFilterProvider;
import net.iubris.facri._di.provider.filenamefilter.PostFilenameFilterProvider;
import net.iubris.facri._di.provider.mapper.CommentsMapperProvider;
import net.iubris.facri._di.provider.mapper.PostsMapperProvider;
import net.iubris.facri.model.Comments;
import net.iubris.facri.model.Posts;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class FacriModule extends AbstractModule {

	@Override
	protected void configure() {

		try {
			Properties properties = new Properties();
			properties.load(new FileReader("facri.properties"));
			Names.bindProperties(binder(), properties);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		bind(FilenameFilter.class).annotatedWith(PostFilenameFilter.class).toProvider(PostFilenameFilterProvider.class);
		bind(FilenameFilter.class).annotatedWith(CommentFilenameFilter.class).toProvider(CommentFilenameFilterProvider.class);
		
		bind( new TypeLiteral<JsonXMLMapper<Posts>>(){}).toProvider(PostsMapperProvider.class);
		bind( new TypeLiteral<JsonXMLMapper<Comments>>(){}).toProvider(CommentsMapperProvider.class);

	}

}
