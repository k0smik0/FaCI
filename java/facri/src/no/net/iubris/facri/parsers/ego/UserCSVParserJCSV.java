/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (UserCSVParserJCSV.java) is part of facri.
 * 
 *     UserCSVParserJCSV.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     UserCSVParserJCSV.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.parsers.ego;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class UserCSVParserJCSV<T> {
	
	final String fileRelativePath; 
	final Class<T> clazz;
	
	public UserCSVParserJCSV(String fileRelativePath, Class<T> clazz) {
		this.fileRelativePath = fileRelativePath;
		this.clazz = clazz;
	}

	public List<T> parse() throws IOException {
		// annotation way, not working
		Reader reader = new FileReader(fileRelativePath);

		ValueProcessorProvider provider = new ValueProcessorProvider();
		CSVEntryParser<T> entryParser = new AnnotationEntryParser<T>(clazz, provider);
		CSVReader<T> csvEntryReader = new CSVReaderBuilder<T>(reader).entryParser(entryParser).build();

		return csvEntryReader.readAll();
		
//		Reader reader = new FileReader(fileRelativePath);
//
//		CSVReader<String[]> csvUserReader = CSVReaderBuilder.newDefaultReader(reader);
//		List<String[]> users = csvUserReader.readAll();
	}
	
	/*class FriendOrAlikeEntryParser implements CSVEntryParser<FriendOrAlike> {
      public FriendOrAlike parseEntry(String... data) {
              String firstname = data[0];
              String lastname = data[1];
              int age = Integer.parseInt(data[2]);

              return new FriendOrAlike(firstname, lastname, age);
      }
	}*/
}
