package net.iubris.facri.parsers.friends;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.World;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.parsers.Parser;

public class MutualFriendsParser implements Parser {
	
	private final String mutualFriendsFilename;
	private final World world;
	
	private final Pattern pattern = Pattern.compile("[0-9]{4,}");
	
	@Inject
	public MutualFriendsParser(@Named("mutual_friends_filename") String mutualFriendsFilename,
			World world) {
		this.mutualFriendsFilename = mutualFriendsFilename;
		this.world = world;
	}

	public void parse(File... arguments/*,String owningWallUserId,*//* Map<String, User> useridToUserMap*/) {
		File userDir = arguments[0];
		
		// old way
//		File[] files = userDir.listFiles( new FilenameFilter() {
//			@Override
//			public boolean accept(File dir, String name) {
//				if (name.equals(mutualFriendsFilename)) {
//					return true;
//				}
//				return false;
//			}
//		});
//		if (files.length>0) {
//			File mutualFriendsFile = files[0];
//			parseWithScanner(mutualFriendsFile, userDir);
//		}
		
		// java 8 way
		try {
//			Stream<Path> list = 
					Files.list( FileSystems.getDefault().getPath(userDir.getPath() ) )
//					;
//			Iterator<Path> iterator = list.iterator();
//			while ( iterator.hasNext()) {
//				System.out.println( iterator.next().toString() );
//			}
//				Stream<Path> filteredList = list
					.filter(f->f.getFileName().toString().equals(mutualFriendsFilename))
//					;					
//				filteredList
				.forEach(
					/*new Consumer<Path>() {
						@Override
						public void accept(Path t) {
							System.out.println("acting on "+t.getFileName());
							parse(t.toFile(), userDir.getName());
						}
					}*/
							f->parse(f.toFile(), userDir.getName())
					);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parse(File mutualFriendsFile /*File userDir*/, String owningWallUserId) {
//		File mutualFriendsFile = files[0];
//System.out.println("parsing "+mutualFriendsFilename+" of "+owningWallUserId);
		try {
			Path mutualFriendsFilePath = mutualFriendsFile.toPath();
//			System.out.println("opening "+mutualFriendsFilePath);
			List<String> mutualFriendsLines = Files.readAllLines(mutualFriendsFilePath,
					StandardCharsets.UTF_8
					);
			if (mutualFriendsLines.size() < 2)
				return;
			
//			String owningWallUserId = userDir.getName();
//			FriendOrAlike user = (FriendOrAlike) ParsingUtils.isExistentFriendUserOrCreateEmpty(owningWallUserId, useridToUserMap);
			FriendOrAlike user = (FriendOrAlike) world.isExistentUserOrCreateNew(owningWallUserId);
			
			/*String header = mutualFriendsLines.remove(0);
			String first = mutualFriendsLines.remove(0);
			System.out.println("first:\n"+first);
			String firstTabbed = first.split("  ")[2];
			System.out.println("first tabbed:\n"+firstTabbed);
			user.addMutualFriend(firstTabbed);
			*/
			
			
			
			mutualFriendsLines
				.stream()
				.parallel()
//				.filter( s->pattern.matcher(s).find() )
//				.count();
//			System.out.print( count +" ");
				.forEach(
						new Consumer<String>() {
							@Override
							public void accept(String t) {
								Matcher matcher = pattern.matcher(t);
								if (matcher.find())
//									System.out.println( matcher.group(0) );
									user.addMutualFriend(matcher.group(0));
							}
						}
//						s->System.out.println( p.matcher(s).group(0) )
//						s->System.out.print( "" )
					);
			
//			Predicate<String> p = s->pattern.matcher((CharSequence) s).matches();
//			
//			mutualFriendsLines
//			.stream()
////			.parallel()
//			.filter( p )
//			.forEach(
//					s->System.out.println( pattern.matcher(s).group(0) )
//					);
//			System.out.println("");
//			
/*			mutualFriendsLines
			.stream()
			.filter( s->p.matcher(s).find() )
			.forEach( 
//					s->user.addMutualFriend( p.matcher(s).group(0) )
					s->System.out.println( p.matcher(s).group(0) )
//					s->System.out.println( s ) 
					);*/
		} catch (IOException e) {
			System.out.println("errors on "+mutualFriendsFile.getName());
			e.printStackTrace();
		}
	}
}
