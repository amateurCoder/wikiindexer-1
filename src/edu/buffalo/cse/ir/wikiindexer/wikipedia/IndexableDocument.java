/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.HashMap;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;

/**
 * A simple map based token view of the transformed document
 * 
 * @author nikhillo
 * 
 */
public class IndexableDocument {
	/**
	 * Default constructor
	 */

	private String docId;
	private static int doc;

	private HashMap<INDEXFIELD, TokenStream> indexMap;

	public IndexableDocument(String docTitle) {
		// TODO: Init state as needed
		docId = docTitle;

		indexMap = new HashMap<INDEXFIELD, TokenStream>(
				INDEXFIELD.values().length);
	}

	/**
	 * MEthod to add a field and stream to the map If the field already exists
	 * in the map, the streams should be merged
	 * 
	 * @param field
	 *            : The field to be added
	 * @param stream
	 *            : The stream to be added.
	 */
	public void addField(INDEXFIELD field, TokenStream stream) {
		if (indexMap.get(field) != null) {
			indexMap.get(field).merge(stream);
		} else
			indexMap.put(field, stream);
	}

	/**
	 * Method to return the stream for a given field
	 * 
	 * @param key
	 *            : The field for which the stream is requested
	 * @return The underlying stream if the key exists, null otherwise
	 */
	public TokenStream getStream(INDEXFIELD key) {
		if (indexMap.get(key) != null) {
			return indexMap.get(key);
		}
		return null;
	}

	/**
	 * Method to return a unique identifier for the given document. It is left
	 * to the student to identify what this must be But also look at how it is
	 * referenced in the indexing process
	 * 
	 * @return A unique identifier for the given document
	 */
	public String getDocumentIdentifier() {
		return docId + "doc" + doc++;
	}

}
