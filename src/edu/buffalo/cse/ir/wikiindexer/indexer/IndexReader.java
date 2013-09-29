/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author nikhillo This class is used to introspect a given index The
 *         expectation is the class should be able to read the index and all
 *         associated dictionaries.
 */
public class IndexReader {
	private static Properties indexProp;
	private static Map<String, List<Integer>> authMap;
	private static Map<String, List<Integer>> categoryMap;
	private static Map<String, List<Integer>> termMap;
	private static Map<Integer, List<Integer>> linkMap;
	private String types;

	/**
	 * Constructor to create an instance
	 * 
	 * @param props
	 *            : The properties file
	 * @param field
	 *            : The index field whose index is to be read
	 */
	public IndexReader(Properties props, INDEXFIELD field) {
		indexProp = props;
		this.types = field.toString();
	}

	/**
	 * Method to get the total number of terms in the key dictionary
	 * 
	 * @return The total number of terms as above
	 * @throws IndexerException
	 */
	public int getTotalKeyTerms() throws IndexerException {
		if (types.equalsIgnoreCase("AUTHOR")) {
			authMap = readInvertibleIndex(types);
			System.out.println("Number of indexes for author:"+authMap.size());
			return authMap.size();
		} else if (types.equalsIgnoreCase("CATEGORY")) {
			categoryMap = readInvertibleIndex(types);
			return categoryMap.size();
		} else if (types.equalsIgnoreCase("TERM")) {
			termMap = readInvertibleIndex(types);
			return termMap.size();
		} else if (types.equalsIgnoreCase("LINK")) {
			linkMap = readForwardIndex(types);
			return linkMap.size();
		}
		return -1;
	}

	/**
	 * Method to get the total number of terms in the value dictionary
	 * 
	 * @return The total number of terms as above
	 * @throws IndexerException 
	 */
	public int getTotalValueTerms() throws IndexerException {
		if (types.equalsIgnoreCase("AUTHOR")) {
			readInvertibleIndex(types);
			return getValueCount(types);
		} else if (types.equalsIgnoreCase("CATEGORY")) {
			readInvertibleIndex(types);
			return getValueCount(types);
		} else if (types.equalsIgnoreCase("TERM")) {
			readInvertibleIndex(types);
			return getValueCount(types);
		} else if (types.equalsIgnoreCase("LINK")) {
			readForwardIndex(types);
			return getValueCount(types);
		}
		return -1;
	}


	/**
	 * Method to retrieve the postings list for a given dictionary term
	 * 
	 * @param key
	 *            : The dictionary term to be queried
	 * @return The postings list with the value term as the key and the number
	 *         of occurrences as value. An ordering is not expected on the map
	 */
	public Map<String, Integer> getPostings(String key) {
		// TODO: Implement this method
		return null;
	}

	/**
	 * Method to get the top k key terms from the given index The top here
	 * refers to the largest size of postings.
	 * 
	 * @param k
	 *            : The number of postings list requested
	 * @return An ordered collection of dictionary terms that satisfy the
	 *         requirement If k is more than the total size of the index, return
	 *         the full index and don't pad the collection. Return null in case
	 *         of an error or invalid inputs
	 */
	public Collection<String> getTopK(int k) {
		// TODO: Implement this method
		return null;
	}

	/**
	 * Method to execute a boolean AND query on the index
	 * 
	 * @param terms
	 *            The terms to be queried on
	 * @return An ordered map containing the results of the query The key is the
	 *         value field of the dictionary and the value is the sum of
	 *         occurrences across the different postings. The value with the
	 *         highest cumulative count should be the first entry in the map.
	 */
	public Map<String, Integer> query(String... terms) {
		// TODO: Implement this method (FOR A BONUS)
		return null;
	}

	private Map<String, List<Integer>> readInvertibleIndex(String types)
			throws IndexerException {
		FileInputStream fileInputStream;
		ObjectInputStream objectInputStream;
		Map<String, List<Integer>> tempMap = new HashMap<String, List<Integer>>();
		try {
			if (types.equalsIgnoreCase("AUTHOR")) {
				fileInputStream = new FileInputStream("files/authMap.txt");
				objectInputStream = new ObjectInputStream(fileInputStream);
				tempMap = (Map<String, List<Integer>>) objectInputStream
						.readObject();
				objectInputStream.close();
				return tempMap;
			} else if (types.equalsIgnoreCase("CATEGORY")) {
				fileInputStream = new FileInputStream("files/categoryMap.txt");
				objectInputStream = new ObjectInputStream(fileInputStream);
				tempMap = (Map<String, List<Integer>>) objectInputStream
						.readObject();
				objectInputStream.close();
				return tempMap;
			} else if (types.equalsIgnoreCase("TERM")) {
				fileInputStream = new FileInputStream("files/termMap.txt");
				objectInputStream = new ObjectInputStream(fileInputStream);
				termMap = (Map<String, List<Integer>>) objectInputStream
						.readObject();
				objectInputStream.close();
				return tempMap;
			}
		} catch (FileNotFoundException e) {
			throw new IndexerException(e.getMessage());
		} catch (IOException e) {
			throw new IndexerException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new IndexerException(e.getMessage());
		}
		return null;
	}
	
	private Map<Integer, List<Integer>> readForwardIndex(String types) throws IndexerException {
		FileInputStream fileInputStream;
		ObjectInputStream objectInputStream;
		Map<Integer,List<Integer>> lMap = new HashMap<Integer,List<Integer>>();
		try {
			if (types.equalsIgnoreCase("LINK")) {
				fileInputStream = new FileInputStream("files/linkMap.txt");
				objectInputStream = new ObjectInputStream(fileInputStream);
				lMap = (Map<Integer, List<Integer>>) objectInputStream
						.readObject();
				objectInputStream.close();
				return lMap;
			}
		} catch (FileNotFoundException e) {
			throw new IndexerException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new IndexerException(e.getMessage());
		} catch (IOException e) {
			throw new IndexerException(e.getMessage());
		}
		return null; 
	}
	
	private int getValueCount(String types) {
		// TODO Auto-generated method stub
		return 0;
	}

}
