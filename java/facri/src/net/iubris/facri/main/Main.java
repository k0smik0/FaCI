package net.iubris.facri.main;

import java.io.FileNotFoundException;
import java.util.function.BiConsumer;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.module.FacriModule;
import net.iubris.facri.graph.GraphGenerator;
import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;
import net.iubris.facri.parsers.FacriDataParser;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class Main {

	public static void main(String[] args) {
		
//		Date start = new Date();
		
		Injector injector = Guice.createInjector( new FacriModule() );
		

		try {
			FacriDataParser parser = injector.getInstance(FacriDataParser.class);
			parser.parse();
			
			World result = parser.getResult();
			
			System.out.println("\nPrinting data");
//			printData(result);
			
			
			
			GraphGenerator gg = new GraphGenerator();
			gg.generate(result);			
			gg.exportGraphToGraphML();
			
		} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
			e.printStackTrace();
		}
		
//		Date end = new Date();
//		double finish = (end.getTime()-start.getTime())/1000f;
//System.out.println( "parsed in: "+finish+"s" );
		
	}
	
	static void printData(World world) {
		
		BiConsumer<String, User> friendConsumer = new BiConsumer<String, User>() {
			@Override
			public void accept(String t, User u) {
				if (u instanceof FriendOrAlike) {
					FriendOrAlike f = (FriendOrAlike) u;
//					int mfs = f.getMutualFriends().size();
					if (f.getMutualFriends().size() >0)
						System.out.println(u.getId()+" "+u.howOwnPosts()+","+u.howUserInteracted()+","+f.getMutualFriends().size());
				}
			}
		};
		
		Ego ego = world.getMyUser();
		System.out.println(ego.getId()+" "+ego.howOwnPosts()+","
				+ego.howUserInteracted()+","+ego.getFriendsIds().size());
		System.out.println("");

		world
			.getMyFriendsMap()
			.forEach( friendConsumer );
		System.out.println("");
		
		world
			.getOtherUsersMap()
			.forEach( friendConsumer );
	}
}
