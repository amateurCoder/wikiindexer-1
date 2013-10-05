/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo An abstract class that represents a dictionary object for a
 *         given index
 */
public abstract class Dictionary implements Writeable {
	public static Map<String, Integer> linkDictionary;

	FileOutputStream fileOutputStream;
	ObjectOutputStream objectOutputStream;

	public Dictionary(Properties props, INDEXFIELD field) {
		linkDictionary = new HashMap<String, Integer>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException {
		try {
			fileOutputStream = new FileOutputStream("files/dictMap.txt");
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(linkDictionary);
		} catch (FileNotFoundException e) {
			throw new IndexerException(e.getMessage());
		} catch (IOException e) {
			throw new IndexerException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
	 */
	public void cleanUp() throws IndexerException {
		try {
			objectOutputStream.close();
		} catch (IOException e) {
			throw new IndexerException(e.getMessage());
		}
	}

	/**
	 * Method to check if the given value exists in the dictionary or not Unlike
	 * the subclassed lookup methods, it only checks if the value exists and
	 * does not change the underlying data structure
	 * 
	 * @param value
	 *            : The value to be looked up
	 * @return true if found, false otherwise
	 */
	public boolean exists(String value) {
		if (linkDictionary.get(value) != null) {
			return true;
		}
		return false;
	}

	/**
	 * MEthod to lookup a given string from the dictionary. The query string can
	 * be an exact match or have wild cards (* and ?) Must be implemented ONLY
	 * AS A BONUS
	 * 
	 * @param queryStr
	 *            : The query string to be searched
	 * @return A collection of ordered strings enumerating all matches if found
	 *         null if no match is found
	 */
	public Collection<String> query(String queryStr) {
		ArrayList<String> keyMatches = new ArrayList<String>();

		Set<String> keySet = linkDictionary.keySet();
		boolean hasTerm = false;

		queryStr = queryStr.replace("*", ".*");
		queryStr = queryStr.replace("?", ".?");
		Pattern queryPattern = Pattern.compile(queryStr);

		Iterator<String> setIterator = keySet.iterator();

		while (setIterator.hasNext()) {
			String key = setIterator.next();
			Matcher m = queryPattern.matcher(key);

			if (m.matches()) {
				keyMatches.add(key);
				hasTerm = true;
			}
		}
		System.out.println("The keys found matcing the query are : \n");
		if (hasTerm == true) {
			return keyMatches;
		} else {
			return null;
		}
	}

	/**
	 * Method to get the total number of terms in the dictionary
	 * 
	 * @return The size of the dictionary
	 */
	public int getTotalTerms() {
		return linkDictionary.size();

	}
}
