/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerFactory;

/**
 * @author nikhillo This class is used to introspect a given index The
 *         expectation is the class should be able to read the index and all
 *         associated dictionaries.
 */
public class IndexReader {
	private static Properties indexProp;
	private static Map<String, LinkedList<PostingNode>> authMap;
	private static Map<String, LinkedList<PostingNode>> categoryMap;
	private static Map<String, LinkedList<PostingNode>> termMap;
	private static Map<Integer, LinkedList<PostingNode>> linkMap;
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

	public Map<String, LinkedList<PostingNode>> getMap()
			throws IndexerException {
		if (types.equalsIgnoreCase("AUTHOR")) {
			authMap = readInvertibleIndex(types);
			return authMap;
		} else if (types.equalsIgnoreCase("CATEGORY")) {
			categoryMap = readInvertibleIndex(types);
			return categoryMap;
		} else if (types.equalsIgnoreCase("TERM")) {
			termMap = readInvertibleIndex(types);
			return termMap;
		}
		return null;
	}

	public Map<Integer, LinkedList<PostingNode>> getLinkMap()
			throws IndexerException {
		if (types.equalsIgnoreCase("LINK")) {
			linkMap = readForwardIndex(types);
			return linkMap;
		}
		return null;
	}

	/**
	 * Method to get the total number of terms in the value dictionary
	 * 
	 * @return The total number of terms as above
	 * @throws IndexerException
	 */
	public int getTotalValueTerms() throws IndexerException {
		Map<String, Integer> dictMap = readDictionaryFromDisk();
		if (null != dictMap) {
			return dictMap.size();
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
	 * @throws IndexerException
	 * @throws TokenizerException
	 */
	public Map<String, Integer> getPostings(String key)
			throws IndexerException, TokenizerException {
		if (types.equalsIgnoreCase("AUTHOR")) {
			TokenizerFactory tf = TokenizerFactory.getInstance(indexProp);
			Tokenizer tk = tf.getTokenizer(INDEXFIELD.AUTHOR);
			TokenStream stream = new TokenStream(key);
			tk.tokenize(stream);
			Collection<String> coll = stream.getAllTokens();
			Iterator<String> it = coll.iterator();
			String str = "";
			while (it.hasNext()) {
				str += it.next();
			}
			authMap = readInvertibleIndex(types);
			return populatePostingMap(str, authMap);
		} else if (types.equalsIgnoreCase("CATEGORY")) {
			TokenizerFactory tf = TokenizerFactory.getInstance(indexProp);
			Tokenizer tk = tf.getTokenizer(INDEXFIELD.CATEGORY);
			TokenStream stream = new TokenStream(key);
			tk.tokenize(stream);
			Collection<String> coll = stream.getAllTokens();
			Iterator<String> it = coll.iterator();
			String str = "";
			while (it.hasNext()) {
				str += it.next();
			}
			categoryMap = readInvertibleIndex(types);
			return populatePostingMap(str, categoryMap);
		} else if (types.equalsIgnoreCase("TERM")) {
			TokenizerFactory tf = TokenizerFactory.getInstance(indexProp);
			Tokenizer tk = tf.getTokenizer(INDEXFIELD.TERM);
			TokenStream stream = new TokenStream(key);
			tk.tokenize(stream);
			Collection<String> coll = stream.getAllTokens();
			Iterator<String> it = coll.iterator();
			String str = "";
			while (it.hasNext()) {
				str += it.next();
			}
			termMap = readInvertibleIndex(types);
			return populatePostingMap(str, termMap);
		} else if (types.equalsIgnoreCase("LINK")) {
			TokenizerFactory tf = TokenizerFactory.getInstance(indexProp);
			Tokenizer tk = tf.getTokenizer(INDEXFIELD.LINK);
			TokenStream stream = new TokenStream(key);
			tk.tokenize(stream);
			Collection<String> coll = stream.getAllTokens();
			Iterator<String> it = coll.iterator();
			String str = "";
			while (it.hasNext()) {
				str += it.next();
			}
			linkMap = readForwardIndex(types);
			return populateLinkPostingMap(str, linkMap);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Integer> populatePostingMap(String key,
			Map<String, LinkedList<PostingNode>> tempMap)
			throws IndexerException {
		Map<String, Integer> finalPostingMap = new HashMap<String, Integer>();

		// Read dictionary map from disk
		Map<String, Integer> dictMap = readDictionaryFromDisk();

		Iterator<Entry<String, LinkedList<PostingNode>>> iterator = tempMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry indexMapEntry = (Map.Entry) iterator.next();
			if (indexMapEntry.getKey().equals(key)) {
				LinkedList<PostingNode> list = (LinkedList<PostingNode>) indexMapEntry
						.getValue();
				for (int i = 1; i < list.size(); i++) {
					PostingNode pn = list.get(i);
					// Iterating over shared dictionary
					for (Map.Entry<String, Integer> dictEntry : dictMap
							.entrySet()) {
						if (pn.getValue() == dictEntry.getValue()) {
							finalPostingMap.put(dictEntry.getKey(),
									pn.getFrequency());
						}
					}
				}
			}
		}
		return finalPostingMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Integer> populateLinkPostingMap(String key,
			Map<Integer, LinkedList<PostingNode>> tempMap)
			throws IndexerException {
		Map<String, Integer> finalPostingMap = new HashMap<String, Integer>();

		// Reading dictionary from disk
		Map<String, Integer> dictMap = readDictionaryFromDisk();

		Iterator<Entry<Integer, LinkedList<PostingNode>>> iterator = tempMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry indexMapEntry = (Map.Entry) iterator.next();
			if (indexMapEntry.getKey() == key) {
				LinkedList<PostingNode> list = (LinkedList<PostingNode>) indexMapEntry
						.getValue();
				for (int i = 1; i < list.size(); i++) {
					PostingNode pn = list.get(i);
					// Iterating over shared dictionary
					for (Map.Entry<String, Integer> dictEntry : dictMap
							.entrySet()) {
						if (pn.getValue() == dictEntry.getValue()) {
							finalPostingMap.put(dictEntry.getKey(),
									pn.getFrequency());
						}
					}
				}
			}
		}
		return finalPostingMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Integer> readDictionaryFromDisk()
			throws IndexerException {
		Map<String, Integer> dictMap;
		try {
			FileInputStream fileInputStream = new FileInputStream(
					"files/dictMap.txt");
			ObjectInputStream objectInputStream = new ObjectInputStream(
					fileInputStream);
			dictMap = (Map<String, Integer>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (FileNotFoundException e) {
			throw new IndexerException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new IndexerException(e.getMessage());
		} catch (IOException e) {
			throw new IndexerException(e.getMessage());
		}
		return dictMap;
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
	 * @throws IndexerException
	 */
	public Collection<String> getTopK(int k) throws IndexerException {
		Collection<String> collKey = new ArrayList<String>();
		if (types.equalsIgnoreCase("AUTHOR")) {
			authMap = readInvertibleIndex(types);
			Map<String, Integer> authCountMap = new HashMap<String, Integer>();
			for (Entry<String, LinkedList<PostingNode>> entry : authMap
					.entrySet()) {
				authCountMap.put(entry.getKey(), entry.getValue().size());
			}
			Map<String, Integer> authSortedCountMap = sortInvertibleIndex(authCountMap);
			// Putting top k into the collection
			int count = 0;
			for (Entry<String, Integer> entry : authSortedCountMap.entrySet()) {
				if (count < k) {
					collKey.add(entry.getKey());
					count++;
				} else {
					break;
				}
			}
			return collKey;
		} else if (types.equalsIgnoreCase("CATEGORY")) {
			categoryMap = readInvertibleIndex(types);
			Map<String, Integer> categoryCountMap = new HashMap<String, Integer>();
			for (Entry<String, LinkedList<PostingNode>> entry : categoryMap
					.entrySet()) {
				categoryCountMap.put(entry.getKey(), entry.getValue().size());
			}
			Map<String, Integer> categorySortedCountMap = sortInvertibleIndex(categoryCountMap);
			// Putting top k into the collection
			int count = 0;
			for (Entry<String, Integer> entry : categorySortedCountMap
					.entrySet()) {
				if (count < k) {
					collKey.add(entry.getKey());
					count++;
				} else {
					break;
				}

			}
			return collKey;
		} else if (types.equalsIgnoreCase("TERM")) {
			termMap = readInvertibleIndex(types);
			Map<String, Integer> termCountMap = new HashMap<String, Integer>();
			for (Entry<String, LinkedList<PostingNode>> entry : termMap
					.entrySet()) {
				termCountMap.put(entry.getKey(), entry.getValue().size());
			}
			Map<String, Integer> termSortedCountMap = sortInvertibleIndex(termCountMap);
			// Putting top k into the collection
			int count = 0;
			for (Entry<String, Integer> entry : termSortedCountMap.entrySet()) {
				if (count < k) {
					collKey.add(entry.getKey());
					count++;
				} else {
					break;
				}

			}
			return collKey;
		} else if (types.equalsIgnoreCase("LINK")) {
			linkMap = readForwardIndex(types);
			Map<Integer, Integer> linkCountMap = new HashMap<Integer, Integer>();
			for (Entry<Integer, LinkedList<PostingNode>> entry : linkMap
					.entrySet()) {
				linkCountMap.put(entry.getKey(), entry.getValue().size());
			}
			Map<Integer, Integer> linkSortedCountMap = sortForwardIndex(linkCountMap);
			// Putting top k into the collection
			int count = 0;
			for (Entry<Integer, Integer> entry : linkSortedCountMap.entrySet()) {
				if (count < k) {
					collKey.add(entry.getKey().toString());
					count++;
				} else {
					break;
				}

			}
			return collKey;
		}
		return null;
	}

	private Map<Integer, Integer> sortForwardIndex(
			Map<Integer, Integer> unsortedMap) {
		List<Entry<Integer, Integer>> entries = new LinkedList<Map.Entry<Integer, Integer>>(
				unsortedMap.entrySet());

		Collections.sort(entries, new Comparator<Entry<Integer, Integer>>() {

			@Override
			public int compare(Entry<Integer, Integer> o1,
					Entry<Integer, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		HashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();

		for (Map.Entry<Integer, Integer> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private Map<String, Integer> sortInvertibleIndex(
			Map<String, Integer> unsortedMap) {
		List<Entry<String, Integer>> entries = new LinkedList<Map.Entry<String, Integer>>(
				unsortedMap.entrySet());

		Collections.sort(entries, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		for (Map.Entry<String, Integer> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
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
	 * @throws IndexerException
	 * @throws TokenizerException
	 */
	public Map<String, Integer> query(String... terms) throws IndexerException,
			TokenizerException {
		Map<String, Integer> queryResult = new HashMap<String, Integer>();
		ArrayList<Map<String, Integer>> linkedListArray = new ArrayList<Map<String, Integer>>();
		for (int i = 0; i < terms.length; i++) {
			linkedListArray.add(getPostings(terms[i]));
		}
		if (linkedListArray.size() == 1) {
			queryResult = linkedListArray.get(0);
		} else if (linkedListArray.size() > 1) {
			Map<String, Integer> tempMap = mergePostings(
					linkedListArray.get(0), linkedListArray.get(1));
			for (int i = 2; i < linkedListArray.size(); i++) {
				tempMap = mergePostings(tempMap, linkedListArray.get(i));
			}
			queryResult = sortInvertibleIndex(tempMap);
		}
		return queryResult;
	}

	private Map<String, Integer> mergePostings(Map<String, Integer> map,
			Map<String, Integer> map2) {
		Map<String, Integer> mergedMap = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			for (Map.Entry<String, Integer> entry1 : map2.entrySet()) {
				if (entry.getKey().equals(entry1.getKey())) {
					mergedMap.put(entry.getKey(),
							entry.getValue() + entry1.getValue());
				}
			}
		}
		return mergedMap;
	}

	@SuppressWarnings({ "unchecked", "resource" })
	private Map<String, LinkedList<PostingNode>> readInvertibleIndex(
			String types) throws IndexerException {
		FileInputStream fileInputStream;
		ObjectInputStream objectInputStream;
		Map<String, LinkedList<PostingNode>> tempMap = new HashMap<String, LinkedList<PostingNode>>();
		try {
			if (types.equalsIgnoreCase("AUTHOR")) {
				fileInputStream = new FileInputStream("files/authMap.txt");
				objectInputStream = new ObjectInputStream(fileInputStream);
				tempMap = (Map<String, LinkedList<PostingNode>>) objectInputStream
						.readObject();
				objectInputStream.close();
				return tempMap;
			} else if (types.equalsIgnoreCase("CATEGORY")) {
				fileInputStream = new FileInputStream("files/categoryMap.txt");
				objectInputStream = new ObjectInputStream(fileInputStream);
				tempMap = (Map<String, LinkedList<PostingNode>>) objectInputStream
						.readObject();
				objectInputStream.close();
				return tempMap;
			} else if (types.equalsIgnoreCase("TERM")) {
				fileInputStream = new FileInputStream("files/termMap.txt");
				objectInputStream = new ObjectInputStream(fileInputStream);
				tempMap = (Map<String, LinkedList<PostingNode>>) objectInputStream
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

	@SuppressWarnings({ "unchecked", "resource" })
	private Map<Integer, LinkedList<PostingNode>> readForwardIndex(String types)
			throws IndexerException {
		FileInputStream fileInputStream;
		ObjectInputStream objectInputStream;
		Map<Integer, LinkedList<PostingNode>> lMap = new HashMap<Integer, LinkedList<PostingNode>>();
		try {
			if (types.equalsIgnoreCase("LINK")) {
				fileInputStream = new FileInputStream("files/linkMap.txt");
				objectInputStream = new ObjectInputStream(fileInputStream);
				lMap = (Map<Integer, LinkedList<PostingNode>>) objectInputStream
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
}
