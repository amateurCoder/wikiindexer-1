/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.util.Properties;

/**
 * @author nikhillo This class represents a subclass of a Dictionary class that
 *         is shared by multiple threads. All methods in this class are
 *         synchronized for the same reason.
 */
public class SharedDictionary extends Dictionary {

	/**
	 * Public default constructor
	 * 
	 * @param props
	 *            : The properties file
	 * @param field
	 *            : The field being indexed by this dictionary
	 */
	private static int docId = 1;

	public SharedDictionary(Properties props, INDEXFIELD field) {
		super(props, field);
		SharedDictionary.linkDictionary.put("nil", 0);

	}

	/**
	 * Method to lookup and possibly add a mapping for the given value in the
	 * dictionary. The class should first try and find the given value within
	 * its dictionary. If found, it should return its id (Or hash value). If not
	 * found, it should create an entry and return the newly created id.
	 * 
	 * @param value
	 *            : The value to be looked up
	 * @return The id as explained above.
	 */
	public synchronized int lookup(String value) {
		if (linkDictionary.get(value) != null) {
			return linkDictionary.get(value);
		} else {
			linkDictionary.put(value, docId++);
			return linkDictionary.get(value);
		}

	}

}
