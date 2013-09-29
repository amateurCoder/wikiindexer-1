/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author nikhillo This class is used to write an index to the disk
 * 
 */
public class IndexWriter implements Writeable {

	/**
	 * Constructor that assumes the underlying index is inverted Every index
	 * (inverted or forward), has a key field and the value field The key field
	 * is the field on which the postings are aggregated The value field is the
	 * field whose postings we are accumulating For term index for example: Key:
	 * Term (or term id) - referenced by TERM INDEXFIELD Value: Document (or
	 * document id) - referenced by LINK INDEXFIELD
	 * 
	 * @param props
	 *            : The Properties file
	 * @param keyField
	 *            : The index field that is the key for this index
	 * @param valueField
	 *            : The index field that is the value for this index
	 */
	private static Properties indexProp;
	private static Map<String, List<Integer>> authMap;
	private static Map<String, List<Integer>> categoryMap;
	private static Map<String, List<Integer>> termMap;
	private static Map<Integer, List<Integer>> linkMap;
	private String type;

	public IndexWriter(Properties props, INDEXFIELD keyField,
			INDEXFIELD valueField) {
		this(props, keyField, valueField, false);
	}

	/**
	 * Overloaded constructor that allows specifying the index type as inverted
	 * or forward Every index (inverted or forward), has a key field and the
	 * value field The key field is the field on which the postings are
	 * aggregated The value field is the field whose postings we are
	 * accumulating For term index for example: Key: Term (or term id) -
	 * referenced by TERM INDEXFIELD Value: Document (or document id) -
	 * referenced by LINK INDEXFIELD
	 * 
	 * @param props
	 *            : The Properties file
	 * @param keyField
	 *            : The index field that is the key for this index
	 * @param valueField
	 *            : The index field that is the value for this index
	 * @param isForward
	 *            : true if the index is a forward index, false if inverted
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField,
			INDEXFIELD valueField, boolean isForward) {
		this.indexProp = props;
		this.type = keyField.toString();
		if (INDEXFIELD.AUTHOR.equals(keyField)) {
			this.authMap = new HashMap<String, List<Integer>>();
		} else if (INDEXFIELD.CATEGORY.equals(keyField)) {
			this.categoryMap = new HashMap<String, List<Integer>>();
		} else if (INDEXFIELD.TERM.equals(keyField)) {
			this.termMap = new HashMap<String, List<Integer>>();
		} else if (INDEXFIELD.LINK.equals(keyField)) {
			this.linkMap = new HashMap<Integer, List<Integer>>();
		}

	}

	/**
	 * Method to make the writer self aware of the current partition it is
	 * handling Applicable only for distributed indexes.
	 * 
	 * @param pnum
	 *            : The partition number
	 */
	public void setPartitionNumber(int pnum) {
		// TODO: Optionally implement this method
	}

	/**
	 * Method to add a given key - value mapping to the index
	 * 
	 * @param keyId
	 *            : The id for the key field, pre-converted
	 * @param valueId
	 *            : The id for the value field, pre-converted
	 * @param numOccurances
	 *            : Number of times the value field is referenced by the key
	 *            field. Ignore if a forward index
	 * @throws IndexerException
	 *             : If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, int valueId, int numOccurances)
			throws IndexerException {
		Map<Integer, List<Integer>> tempMap = null;
		if (type.equalsIgnoreCase("LINK")) {
			tempMap = linkMap;
		}
		int nOccurances = 0;
		if (null != tempMap) {
			if (tempMap.containsKey(keyId)) {
				nOccurances = tempMap.get(keyId).get(0);
				nOccurances = nOccurances + numOccurances;
				tempMap.get(keyId).add(valueId);
				tempMap.get(keyId).add(0, nOccurances);
			} else {
				List<Integer> list = new ArrayList<Integer>();
				list.add(0, numOccurances);
				list.add(valueId);
				tempMap.put(keyId, list);
			}
		}
	}

	/**
	 * Method to add a given key - value mapping to the index
	 * 
	 * @param keyId
	 *            : The id for the key field, pre-converted
	 * @param value
	 *            : The value for the value field
	 * @param numOccurances
	 *            : Number of times the value field is referenced by the key
	 *            field. Ignore if a forward index
	 * @throws IndexerException
	 *             : If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, String value, int numOccurances)
			throws IndexerException {

	}

	/**
	 * Method to add a given key - value mapping to the index
	 * 
	 * @param key
	 *            : The key for the key field
	 * @param valueId
	 *            : The id for the value field, pre-converted
	 * @param numOccurances
	 *            : Number of times the value field is referenced by the key
	 *            field. Ignore if a forward index
	 * @throws IndexerException
	 *             : If any exception occurs while indexing
	 */
	public void addToIndex(String key, int valueId, int numOccurances)
			throws IndexerException {
		Map<String, List<Integer>> tempMap = null;
		if (type.equals("AUTHOR")) {
			tempMap = authMap;
		} else if (type.equals("CATEGORY")) {
			tempMap = categoryMap;
		} else if (type.equals("TERM")) {
			tempMap = termMap;
		}
		int nOccurances = 0;
		if (null != tempMap) {
			if (tempMap.containsKey(key)) {
				nOccurances = tempMap.get(key).get(0);
				nOccurances = nOccurances + numOccurances;
				tempMap.get(key).add(valueId);
				tempMap.get(key).add(0, nOccurances);
			} else {
				List<Integer> list = new ArrayList<Integer>();
				list.add(0, numOccurances);
				list.add(valueId);
				tempMap.put(key, list);
			}
		}
		System.out.println("Map:"+ tempMap);
		
	}

	/**
	 * Method to add a given key - value mapping to the index
	 * 
	 * @param key
	 *            : The key for the key field
	 * @param value
	 *            : The value for the value field
	 * @param numOccurances
	 *            : Number of times the value field is referenced by the key
	 *            field. Ignore if a forward index
	 * @throws IndexerException
	 *             : If any exception occurs while indexing
	 */
	public void addToIndex(String key, String value, int numOccurances)
			throws IndexerException {
		// TODO: Implement this method
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException {
		String filename = "";
		Map<String, List<Integer>> map = null;
		FileOutputStream fileOutputStream;
		ObjectOutputStream objectOutputStream;
		if (type.equalsIgnoreCase("AUTHOR")) {
			filename = "files/authMap.txt";
			map = authMap;
		} else if (type.equalsIgnoreCase("CATEGORY")) {
			filename = "files/categoryMap.txt";
			map = categoryMap;
		} else if (type.equalsIgnoreCase("TERM")) {
			filename = "files/termMap.txt";
			map = termMap;
		}
		try {
			if (type.equalsIgnoreCase("LINK")) {
				fileOutputStream = new FileOutputStream("files/linkMap.txt");
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(linkMap);
				objectOutputStream.close();
			} else {
				fileOutputStream = new FileOutputStream(filename);
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(map);
				objectOutputStream.close();
			}
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
	public void cleanUp() {
		// TODO Implement this method

	}

}
