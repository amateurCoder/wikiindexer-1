/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
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
	private static Map<String, LinkedList<PostingNode>> authMap;
	private static Map<String, LinkedList<PostingNode>> categoryMap;
	private static Map<String, LinkedList<PostingNode>> termMap;
	private static Map<Integer, LinkedList<PostingNode>> linkMap;
	private String type;
	
	private FileOutputStream fileOutputStream;
	private ObjectOutputStream objectOutputStream;

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
		indexProp = props;
		this.type = keyField.toString();
		if (INDEXFIELD.AUTHOR.equals(keyField)) {
			authMap = new HashMap<String, LinkedList<PostingNode>>();
		} else if (INDEXFIELD.CATEGORY.equals(keyField)) {
			categoryMap = new HashMap<String, LinkedList<PostingNode>>();
		} else if (INDEXFIELD.TERM.equals(keyField)) {
			termMap = new HashMap<String, LinkedList<PostingNode>>();
		} else if (INDEXFIELD.LINK.equals(keyField)) {
			linkMap = new HashMap<Integer, LinkedList<PostingNode>>();
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
		int nOccurances = 0;
		if (null != linkMap) {
			if (linkMap.containsKey(keyId)) {
				nOccurances = linkMap.get(keyId).get(0).getFrequency();
				nOccurances = nOccurances + numOccurances;
				linkMap.get(keyId).get(0).setFrequency(nOccurances);
				PostingNode postingNode = new PostingNode(valueId,
						numOccurances);
				linkMap.get(keyId).add(postingNode);
				
			} else {
				LinkedList<PostingNode> linkedList = new LinkedList<PostingNode>();
				linkedList.addFirst(new PostingNode(0, numOccurances));
				PostingNode node = new PostingNode(valueId, numOccurances);
				linkedList.add(node);
				linkMap.put(keyId, linkedList);
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
	public synchronized void addToIndex(String key, int valueId,
			int numOccurances) throws IndexerException {
		int nOccurances = 0;
		if (type.equalsIgnoreCase("AUTHOR")) {
			if (null != authMap) {
				if (authMap.containsKey(key)) {
					nOccurances = authMap.get(key).get(0).getFrequency();
					nOccurances = nOccurances + numOccurances;
					authMap.get(key).get(0).setFrequency(nOccurances);
					PostingNode postingNode = new PostingNode(valueId,
							numOccurances);
					authMap.get(key).add(postingNode);
				} else {
					LinkedList<PostingNode> linkedList = new LinkedList<PostingNode>();
					linkedList.addFirst(new PostingNode(0, numOccurances));
					PostingNode node = new PostingNode(valueId, numOccurances);
					linkedList.add(node);
					authMap.put(key, linkedList);
				}
			}
		} else if (type.equalsIgnoreCase("CATEGORY")) {
			if (null != categoryMap) {
				if (categoryMap.containsKey(key)) {
					nOccurances = categoryMap.get(key).get(0).getFrequency();
					nOccurances = nOccurances + numOccurances;
					categoryMap.get(key).get(0).setFrequency(nOccurances);
					PostingNode postingNode = new PostingNode(valueId,
							numOccurances);
					categoryMap.get(key).add(postingNode);
				} else {
					LinkedList<PostingNode> linkedList = new LinkedList<PostingNode>();
					linkedList.addFirst(new PostingNode(0, numOccurances));
					PostingNode node = new PostingNode(valueId, numOccurances);
					linkedList.add(node);
					categoryMap.put(key, linkedList);
				}
			}
		} else if (type.equalsIgnoreCase("TERM")) {
			if (null != termMap) {
				if (termMap.containsKey(key)) {
					nOccurances = termMap.get(key).get(0).getFrequency();
					nOccurances = nOccurances + numOccurances;
					termMap.get(key).get(0).setFrequency(nOccurances);
					PostingNode postingNode = new PostingNode(valueId,
							numOccurances);
					termMap.get(key).add(postingNode);
				} else {
					LinkedList<PostingNode> linkedList = new LinkedList<PostingNode>();
					linkedList.addFirst(new PostingNode(0, numOccurances));
					PostingNode node = new PostingNode(valueId, numOccurances);
					linkedList.add(node);
					termMap.put(key, linkedList);
				}
			}
		}
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
		try {
			if (type.equalsIgnoreCase("AUTHOR")) {
				fileOutputStream = new FileOutputStream("files/authMap.txt");
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(authMap);
			} else if (type.equalsIgnoreCase("CATEGORY")) {
				fileOutputStream = new FileOutputStream("files/categoryMap.txt");
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(categoryMap);
			} else if (type.equalsIgnoreCase("TERM")) {
				fileOutputStream = new FileOutputStream("files/termMap.txt");
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(termMap);
			} else if (type.equalsIgnoreCase("LINK")) {
				fileOutputStream = new FileOutputStream("files/linkMap.txt");
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(linkMap);
			}
		} catch (FileNotFoundException e) {
			throw new IndexerException(e.getMessage());
		} catch (IOException e) {
			throw new IndexerException(e.getMessage());
		}
		System.out.println("=====Disk write done====");
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

}
