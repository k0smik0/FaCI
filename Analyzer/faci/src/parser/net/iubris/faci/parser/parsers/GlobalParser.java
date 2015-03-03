/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (DataParser.java) is part of facri.
 * 
 *     DataParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     DataParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.parsers;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.faci.model.world.World;
import net.iubris.faci.parser.parsers.data.ego.EgoDataParser;
import net.iubris.faci.parser.parsers.data.friends.FriendsDirTreeParser;
import net.iubris.faci.parser.parsers.infos.FriendsOfFriendsInfosParser;
import net.iubris.faci.parser.parsers.infos.InfosParser;

@Singleton
public class GlobalParser implements Parser {

private final EgoDataParser egoDataParser;
private final FriendsDirTreeParser friendsDataParser;
private final FriendsOfFriendsInfosParser friendsOfFriendsDataParser;
private final World world;
private boolean parsed = false;
private InfosParser infosParser;


	@Inject
	public GlobalParser(
			InfosParser infosParser,
			EgoDataParser egoDataParser,
			FriendsDirTreeParser friendsDataParser,
			FriendsOfFriendsInfosParser friendsOfFriendsDataParser,
			World world
			) {
		this.infosParser = infosParser;
		this.egoDataParser = egoDataParser;
		this.friendsDataParser = friendsDataParser;
		this.friendsOfFriendsDataParser = friendsOfFriendsDataParser;
		this.world = world;
	}

	@Override
	public void parse(File... dummy) throws JAXBException, XMLStreamException, IOException {
		if (!parsed) {
			infosParser.parse();
			egoDataParser.parse();
			friendsDataParser.parse();
			friendsOfFriendsDataParser.parse();
			parsed = true;
			world.setParsingDone(true);
		}
	}
	
	public World getResult() {
		return world;
	}
}