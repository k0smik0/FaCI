package net.iubris.facri.model.users;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.iubris.facri.model.users.AbstractUser.Sex;

import com.googlecode.jcsv.annotations.ValueProcessor;
import com.opencsv.CSVReader;

public class UserParser<T extends User> {
	
	private final Class<T> clazz; 
	private final String file;
	
	public UserParser(Class<T> clazz, String file) {
		this.clazz = clazz;
		this.file = file;
	}
	@SuppressWarnings("unchecked")
	public List<T> parse(/*Class<T> clazz, String file*/) throws IOException {
		// opencsv mapping
		/*
		ColumnPositionMappingStrategy<T> strat = new ColumnPositionMappingStrategy<T>();
		strat.setType(clazz);
		String[] columns = new String[] {"UID","NAME","FRIEND_COUNT","MUTUAL_FRIEND_COUNT","PIC_SMALL","PROFILE_URL","SEX","SIGNIFICANT_OTHER_ID"}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);

//		CsvToBean<T> csv = new CsvToBean<T>();
		 */
		
		// opencsv parse
		CSVReader csvReader = new CSVReader( new FileReader(file) );
		List<String[]> csvRows = csvReader.readAll();
		csvRows.remove(0);
		
		List<T> ts = new ArrayList<>();
		
		if (Ego.class.isAssignableFrom(clazz)) { // ego
			String[] egoFields = csvRows.get(0);
//			System.out.println(egoFields[2]);
			Ego ego = new Ego(
				egoFields[0], 
				egoFields[1], 
				getInt(egoFields[2]),
				new URL( egoFields[4] ), 
				new URL( egoFields[5] ), 
				getSex( egoFields[6] ), 
				egoFields[7] 
			);
			ts.add((T) ego);
		} else if (FriendOrAlike.class.isAssignableFrom(clazz)) { // FriendOrAlike
			csvRows.forEach(new Consumer<String[]>() {
				@Override
				public void accept(String[] csvRow) {
					FriendOrAlike friendOrAlike = null;
					
//					String friendCountsString = csvRow[2];
//					int friendCounts = 0;
//					if (!friendCountsString.isEmpty())
//						Integer.parseInt( friendCountsString );
//					
//					String mutualFriendCountsString = csvRow[3];
//					int mutualFriendCounts = 0;
//					if (!mutualFriendCountsString.isEmpty())
//						Integer.parseInt( mutualFriendCountsString );
					
//					String sexAsString = csvRow[6];
//					Sex sex = Sex.unknown;
//					if (sexAsString.isEmpty())
//						sex = Sex.valueOf(sexAsString);
					
					try {
						friendOrAlike = new FriendOrAlike(
								csvRow[0],
								csvRow[1],
								getInt(csvRow[2]),
								getInt(csvRow[3]),
								new URL(csvRow[4]),
								new URL(csvRow[5]),
								getSex(csvRow[6]),
								csvRow[7]
								);
					} catch (NumberFormatException | MalformedURLException e) {
						e.printStackTrace();
					}
					ts.add((T) friendOrAlike);
				}
			});
		}
		csvReader.close();
		return ts;
		
		
		
		// jcsv
		/*Reader reader = new FileReader(file);

		ValueProcessorProvider provider = new ValueProcessorProvider();
		provider.registerValueProcessor(Sex.class, new SexProcessor() );
		provider.registerValueProcessor(URL.class, new UrlProcessor() );
		CSVEntryParser<T> entryParser = new AnnotationEntryParser<T>(clazz, provider);
		CSVReader<T> csvEntryReader = new CSVReaderBuilder<T>(reader).entryParser(entryParser).build();

		List<T> redAll = csvEntryReader.readAll();
		redAll.remove(0);
		return redAll;*/
		
	}
	
	private int getInt(String csvField) {
		String asString = csvField;
		int integerValue = 0;
		if (!asString.isEmpty())
			integerValue = Integer.parseInt( asString );
		return integerValue;
	}
	private Sex getSex(String csvField) {
		String sexAsString = csvField;
		Sex sex = Sex.unknown;
		if (!sexAsString.isEmpty())
			sex = Sex.valueOf(sexAsString);
		return sex;
	}
	
	static public class SexProcessor implements ValueProcessor<Sex> {
      @Override
      public Sex processValue(String value) {
      	System.out.println(value);
      	if (value.isEmpty()) {
      		return Sex.unknown;
      	}
        return Sex.valueOf(value);
      }
	}
	static public class UrlProcessor implements ValueProcessor<URL> {
      @Override
      public URL processValue(String value) {
      	System.out.println(value);
			try {
				return new URL(value);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}        
      }
	}
}
