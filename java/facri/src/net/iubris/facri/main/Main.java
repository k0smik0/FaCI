package net.iubris.facri.main;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.module.FacriModule;
import net.iubris.facri.model.User;
import net.iubris.facri.parsers.GlobalParser;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class Main {

	public static void main(String[] args) {
		
		Injector injector = Guice.createInjector( new FacriModule() );
		
		GlobalParser jsonParser = injector.getInstance(GlobalParser.class);
		try {
			jsonParser.parse();
			
			Map<String, User> useridToUserMap = jsonParser.getUseridToUserMap();
			useridToUserMap.forEach( new BiConsumer<String, User>() {
				@Override
				public void accept(String t, User u) {
					
					int mfs = u.getMutualFriends().size();
					if (mfs >0)
						System.out.println(t+": "+u.getId()+" "+u.howOwnPosts()+","
							+u.howUserInteracted()+","+u.getMutualFriends().size());
				}
			});
		} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
			e.printStackTrace();
		}
	}
}
