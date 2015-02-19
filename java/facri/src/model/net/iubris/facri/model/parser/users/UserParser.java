/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (UserParser.java) is part of facri.
 * 
 *     UserParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     UserParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.model.parser.users;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class UserParser<T extends User> {
	
	private final Class<T> clazz; 
	private final String file;
	
	public UserParser(Class<T> clazz, String file) {
		this.clazz = clazz;
		this.file = file;
	}
	@SuppressWarnings("unchecked")
	public List<T> parse() throws IOException {
		// opencsv parse
		CSVReader csvReader = new CSVReader( new FileReader(file) );
		List<String[]> csvRows = csvReader.readAll();
		csvRows.remove(0);
		
		List<T> ts = new ArrayList<>();
		
		if (Ego.class.isAssignableFrom(clazz)) { // ego
			String[] egoFields = csvRows.get(0);
			Ego ego = new Ego(
				egoFields[0], // uid
				egoFields[1], // name
				getInt(egoFields[2]), // friendcount 
				new URL( egoFields[4] ), // picsmall
				new URL( egoFields[5] ), // profileurl
				getSex( egoFields[6] ), // sex
				egoFields[7] // significantotherid
			);
			ts.add((T) ego);
		} else if (Friend.class.isAssignableFrom(clazz)) { // Friend or FriendOfFriend
			csvRows.forEach(csvRow->{
					// old working
					/*try {
						friendOrAlike = new GenericFriend(
								csvRow[0], // uid
								csvRow[1], // name
								getInt(csvRow[2]), // friendcount
								getInt(csvRow[3]), // mutualfriendcount
								new URL(csvRow[4]), // picsmall
								new URL(csvRow[5]), // profileurl
								getSex(csvRow[6]), // sex
								csvRow[7] // significantotherid
								);
					} catch (NumberFormatException | MalformedURLException e) {
						e.printStackTrace();
					}*/
				try {
				Friend friend = buildFriend(csvRow);
					ts.add((T) friend);
				} catch ( MalformedURLException e) {
					e.printStackTrace();
				}
			});
		} else if (FriendOfFriend.class.isAssignableFrom(clazz)) {
			csvRows.forEach(csvRow->{
				try {
					FriendOfFriend friendOfFriend = buildFriendOfFriend(csvRow);
					ts.add((T) friendOfFriend);
				} catch ( MalformedURLException e) {
					e.printStackTrace();
				}
			});
			
		}
		csvReader.close();
		return ts;
	}
	
	private Friend buildFriend(String[] csvRow) throws MalformedURLException {
		return new Friend(
			csvRow[0], // uid
			csvRow[1], // name
			getInt(csvRow[2]), // friendcount
			getInt(csvRow[3]), // mutualfriendcount
			new URL(csvRow[4]), // picsmall
			new URL(csvRow[5]), // profileurl
			getSex(csvRow[6]), // sex
			csvRow[7] // significantotherid
		);
	}
	
	private FriendOfFriend buildFriendOfFriend(String[] csvRow) throws MalformedURLException {
		return new FriendOfFriend(
				csvRow[0], // uid
				csvRow[1], // name
				getInt(csvRow[2]), // friendcount
				getInt(csvRow[3]), // mutualfriendcount
				new URL(csvRow[4]), // picsmall
				new URL(csvRow[5]), // profileurl
				getSex(csvRow[6]), // sex
				csvRow[7] // significantotherid
			);
	}
	
	private int getInt(String csvField) {
		String asString = csvField;
		int integerValue = 0;
		if (!asString.isEmpty())
			integerValue = Integer.parseInt( asString );
		return integerValue;
	}
	private User.Sex getSex(String csvField) {
		String sexAsString = csvField;
		User.Sex sex = User.Sex.unknown;
		if (!sexAsString.isEmpty())
			sex = User.Sex.valueOf(sexAsString);
		return sex;
	}
}
