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
