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

	@Override
	public void parse(File... arguments) {
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
			Files.list( FileSystems.getDefault().getPath(userDir.getPath() ) )
				.filter(f->f.getFileName().toString().equals(mutualFriendsFilename))
				.forEach(
						f->parse(f.toFile(), userDir.getName())
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parse(File mutualFriendsFile, String owningWallUserId) {
		try {
			Path mutualFriendsFilePath = mutualFriendsFile.toPath();
			List<String> mutualFriendsLines = Files.readAllLines(mutualFriendsFilePath, StandardCharsets.UTF_8);
			if (mutualFriendsLines.size() < 2)
				return;
			
			FriendOrAlike user = (FriendOrAlike) world.isExistentUserOrCreateNew(owningWallUserId);
			
			mutualFriendsLines
			.stream()
			.parallel()
			.forEach(
				new Consumer<String>() {
					@Override
					public void accept(String t) {
						Matcher matcher = pattern.matcher(t);
						if (matcher.find())
							user.addMutualFriend(matcher.group(0));
					}
				}
			);
		} catch (IOException e) {
			System.out.println("errors on "+mutualFriendsFile.getName());
			e.printStackTrace();
		}
	}
}
