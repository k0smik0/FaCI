package net.iubris.faci.parser.parsers.infos;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.Ego;
import net.iubris.faci.parser._di.annotations.datafiles.MeInfoFilename;
import net.iubris.faci.parser.parsers.Parser;
import net.iubris.faci.parser.parsers.utils.UserDataParser;
import net.iubris.faci.utils.Printer;

public class InfosParser implements Parser {
	 
	private final String meFile; // me.txt
	
	private final FriendsInfosParser friendsInfosParser;
	private final FriendsOfFriendsInfosParser friendsOfFriendsInfosParser;
	
	private final World world;
	
	@Inject
	public InfosParser(
			@MeInfoFilename
			String meFile,
			FriendsInfosParser friendsInfosParser,
			FriendsOfFriendsInfosParser friendsOfFriendsInfosParser,
			World world) {	
		this.meFile = meFile;
		this.friendsInfosParser = friendsInfosParser;
		this.friendsOfFriendsInfosParser = friendsOfFriendsInfosParser;
		this.world = world;
	}

	@Override
	public void parse(File... userDirs) throws IOException {
		Printer.println("Parsing users infos:");
		
		Printer.print("\tme: ");
		Ego myUser = new UserDataParser<Ego>(Ego.class, meFile).parse().get(0);
		world.setMyUser(myUser);
		Printer.println("ok");
		
		Printer.print("\tmy friends: ");
		friendsInfosParser.parse();
		Printer.println("ok");
		
		Printer.print("\tfriends of my friends: ");
		friendsOfFriendsInfosParser.parse();	
		Printer.println("ok");
	}
}
