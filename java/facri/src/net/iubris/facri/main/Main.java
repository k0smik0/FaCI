package net.iubris.facri.main;

import java.io.FileNotFoundException;
import java.util.function.BiConsumer;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.module.FacriModule;
import net.iubris.facri.graph.GephiGraphGenerator;
import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;
import net.iubris.facri.parsers.DataParser;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class Main {
	
	void doStuff(Injector... injectors) {
		try {
			dataParser.parse();
			
			World world = dataParser.getResult();
			
			System.out.println("");
			
			graphGenerator.generate(world);
			graphGenerator.testGraph();
//			gg.exportGraphToGraphML();
			
		} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	void printData(World world) {		
		BiConsumer<String, User> friendConsumer = new BiConsumer<String, User>() {
			@Override
			public void accept(String t, User u) {
				if (u instanceof FriendOrAlike) {
					FriendOrAlike f = (FriendOrAlike) u;
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
	
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}

	static void checkMemory() {
	// Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Used memory is bytes: " + memory);
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory));
	}
	
	private final DataParser dataParser;
	private final GephiGraphGenerator graphGenerator;
	
	@Inject
	public Main(DataParser dataParser, GephiGraphGenerator graphGenerator) {
		this.dataParser = dataParser;
		this.graphGenerator = graphGenerator;
	}
	
	public static void main(String[] args) {
//		checkMemory();
		Injector injector = Guice.createInjector( new FacriModule() );
		Main main = injector.getInstance(Main.class);
		main.doStuff();
//		checkMemory();
	}

}
