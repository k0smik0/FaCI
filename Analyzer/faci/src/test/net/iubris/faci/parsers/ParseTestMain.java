/*******************************************************************************

 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (ParseTestMain.java) is part of facri.
 * 
 *     ParseTestMain.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     ParseTestMain.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parsers;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.AbstractFriend;
import net.iubris.faci.model.world.users.Ego;
import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.model.world.users.GenericUser;
import net.iubris.faci.model.world.users.User;
import net.iubris.faci.parser._di._guice.module.ParserModule;
import net.iubris.faci.parser.model.posts.Post;
import net.iubris.faci.parser.parsers.GlobalParser;
import net.iubris.faci.utils.Printer;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ParseTestMain {
	
	private final GlobalParser dataParser;
	
	@Inject
	public ParseTestMain(GlobalParser dataParser) {
		this.dataParser = dataParser;
	}

	private void parse() throws JAXBException, XMLStreamException, IOException {
		dataParser.parse();
		World world = dataParser.getResult();
//		world.testData();
		
		DataTester dataTester = new DataTester(world);
		dataTester.test();
	}


	public static void main(String[] args) {
	
		Injector injector = Guice.createInjector( new ParserModule() );
		ParseTestMain debugMain = injector.getInstance(ParseTestMain.class);
		
		try {
			debugMain.parse();
		} catch (JAXBException | XMLStreamException | IOException e) {
			e.printStackTrace();
		}

	}
	
	public static class DataTester {
		
		private final World world;
		private final Ego ego;
		
		private int userCounter = 0;
		
		
		public DataTester(World world) {
			this.world = world;
			this.ego = world.getMyUser();
		}
		
		public void test() {
			System.out.println( ego.getUid()+":\n"
					+"\tfriends: "+ego.getFriendsCount()+"|"+ego.getFriendsIds().size()
					);
			printSomeUserInformations(ego);
			System.out.println("");
			
			world
				.getFriendsMap()
				.forEach( friendoralikeConsumer );
			System.out.println("");
			
			resetCounter();
			world
				.getFriendsOfFriendsMap()
				.forEach( friendoralikeConsumer );
			System.out.println("");
			
			resetCounter();
			world
				.getOtherUsersMap()
				.forEach( new BiConsumer<String, GenericUser>() {
					@Override
					public void accept(String uid, GenericUser gu) {
						String toPrint = "["+userCounter+"] "+uid;
						toPrint = toPrint
						.concat(" (other user):\n")
						.concat("\tall friends: "+gu.getFriendsCount());
						Printer.println(toPrint);
						printSomeUserInformations(gu);
					}
				});
			System.out.println("");
		}
		
		void resetCounter() {
			userCounter = 0;
		}
		
		/*private final BiConsumer<String, AbstractFriend> friendoralikeConsumer = new BiConsumer<String, AbstractFriend>() {
			@Override
			public void accept(String t, AbstractFriend foa) {
					String uid = foa.getUid();
					String toPrint = "["+userCounter+"] "+uid;
					if (ego.isMyFriendById(uid)) {
						toPrint = toPrint
							.concat(" (friend):\n")
							.concat("\tall friends: "+foa.getFriendsCount()+"\n"
									+"\tmutual (ids|count): "+foa.getMutualFriendsIds().size()+"|"+foa.getMutualFriendsCount()+" + ");
						Friend f = (Friend)foa;
						toPrint = toPrint.concat("friends of friends: "+f.getFriendsOfFriendsIds().size());
					} else {
						toPrint = toPrint
							.concat(" (friend of friend):\n")
							.concat("\tall friends: "+foa.getFriendsCount()+"\n")
							.concat("\tmutual (ids|count): "+foa.getMutualFriendsIds().size()+"|"+foa.getMutualFriendsCount());
					}
					Printer.println(toPrint);
					
					printSomeUserInformations(foa);
					userCounter++;
			}
		};*/
		private final BiConsumer<String, AbstractFriend> friendoralikeConsumer = new BiConsumer<String, AbstractFriend>() {
			@Override
			public void accept(String t, AbstractFriend foa) {
					String uid = foa.getUid();
					String toPrint = "["+userCounter+"] "+uid;
					if (foa instanceof Friend) {
						toPrint = toPrint
							.concat(" (friend):\n")
							.concat("\tall friends: "+foa.getFriendsCount()+"\n"
									+"\tmutual (ids|count): "+foa.getMutualFriendsIds().size()+"|"+foa.getMutualFriendsCount()+" + ");
						Friend f = (Friend)foa;
						toPrint = toPrint.concat("friends of friends: "+f.getFriendsOfFriendsIds().size());
					} else {
						toPrint = toPrint
							.concat(" (friend of friend):\n")
							.concat("\tall friends: "+foa.getFriendsCount()+"\n")
							.concat("\tmutual (ids|count): "+foa.getMutualFriendsIds().size()+"|"+foa.getMutualFriendsCount());
					}
					Printer.println(toPrint);
					
					printSomeUserInformations(foa);
					userCounter++;
			}
		};
		
		private void printSomeUserInformations(User user) {
			Set<Post> posts = user.getOwnPosts();
			Comparator<Post> postTimeComparator = new Comparator<Post>() {
				@Override
				public int compare(Post o1, Post o2) {
					return (o1.getCreatedTime().compareTo(o2.getCreatedTime()));
				}
			};
			Optional<Post> eagerPost = posts.stream().parallel().min(postTimeComparator);
			Optional<Post> youngerPost = posts.stream().parallel().max(postTimeComparator);
			Printer.println(
					"\tposts: "+user.getOwnPostsCount()+"("+posts.size()+")"
					);
			Printer.print("\t\teager: ");
					eagerPost.ifPresent(	 c->Printer.print(c.getCreatedTime()) );
			Printer.print("\n\t\tyounger: ");
					youngerPost.ifPresent(c->Printer.print(c.getCreatedTime()) );
			Printer.println(
					 "\n\t\treceived likes: "+user.getOwnLikedPostsCount()
					+"\n\t\treshared: "+user.getOwnPostsResharingCount()					
					+"\n\tout interactions: "+user.getToOtherUsersInteractionsCount()+"("+user.getToOtherUsersInteractions().size()+")"					
					);
			if (user.getToOtherUsersInteractionsCount()<3) {
				
//				user.getToOtherUsersInteractions().entrySet().stream().forEach(c->{
//					String targetUserId = c.getKey();
//					Printer.println(
//							"to: "+targetUserId+" -> "+user.getToOtherUserInteractions( targetUserId ).
//					);
//				});
			}
		}
	}
}
