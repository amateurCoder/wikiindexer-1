/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument.Section;

/**
 * A Callable document transformer that converts the given WikipediaDocument
 * object into an IndexableDocument object using the given Tokenizer
 * 
 * @author nikhillo
 * 
 */
public class DocumentTransformer implements Callable<IndexableDocument> {
	/**
	 * Default constructor, DO NOT change
	 * 
	 * @param tknizerMap
	 *            : A map mapping a fully initialized tokenizer to a given field
	 *            type
	 * @param doc
	 *            : The WikipediaDocument to be processed
	 */

	Map<INDEXFIELD, Tokenizer> tempMap;
	WikipediaDocument tempDoc;
	IndexableDocument indexableDoc;

	public DocumentTransformer(Map<INDEXFIELD, Tokenizer> tknizerMap,
			WikipediaDocument doc) {
		tempMap = tknizerMap;
		tempDoc = doc;
		
		indexableDoc = new IndexableDocument(doc.getTitle());
	}

	/**
	 * Method to trigger the transformation
	 * 
	 * @throws TokenizerException
	 *             Inc ase any tokenization error occurs
	 */
	public IndexableDocument call() throws TokenizerException {
		// TODO Implement this method

		for (Map.Entry<INDEXFIELD, Tokenizer> entry : tempMap.entrySet()) {

			if (entry.getKey().toString().equalsIgnoreCase("AUTHOR")) {

				TokenStream tempStream = new TokenStream(tempDoc.getAuthor());
				
				entry.getValue().tokenize(tempStream);
				
				indexableDoc.addField(entry.getKey(), tempStream);
			}
			 if (entry.getKey().toString().equalsIgnoreCase("LINK")) {
				
				
				Set<String> tempLinkSet = tempDoc.getLinks();
				Iterator<String> setIterator = tempLinkSet.iterator();
				if (setIterator.hasNext()) {
					TokenStream tempStream = new TokenStream(setIterator.next());
					while (setIterator.hasNext()) {

						tempStream.append(setIterator.next());
					}
					
					entry.getValue().tokenize(tempStream);
					
					indexableDoc.addField(entry.getKey(), tempStream);
				}
			} 
			 else if (entry.getKey().toString().equalsIgnoreCase("CATEGORY")) {

				List<String> tempCategoryList = tempDoc.getCategories();

				Iterator<String> categorylistIterator = tempCategoryList
						.listIterator();

				if (categorylistIterator.hasNext()) {
					TokenStream tempStream = new TokenStream(
							categorylistIterator.next());
					while (categorylistIterator.hasNext()) {
						tempStream.append(categorylistIterator.next());
					}
					
					entry.getValue().tokenize(tempStream);
					
					indexableDoc.addField(entry.getKey(), tempStream);
				}
			}
			if (entry.getKey().toString().equalsIgnoreCase("TERM")) {

				List<Section> tempListOfSections = tempDoc.getSections();
				ListIterator<Section> tempIt = tempListOfSections
						.listIterator();
				if (tempIt.hasNext()) {
					Section tempSection = tempIt.next();
					TokenStream tempStream = new TokenStream(
							tempSection.getTitle());
					tempStream.append(tempSection.getText());
					while (tempIt.hasNext()) {
						tempStream.append(tempIt.next().getTitle());
						tempIt.previous();
						tempStream.append(tempIt.next().getText());

					}
					
//					System.out.println("Before tokenization==="+tempStream.getAllTokens());
					
					entry.getValue().tokenize(tempStream);
					
//					System.out.println("After tokenization==="+tempSection.getText());
					
					indexableDoc.addField(entry.getKey(), tempStream);
				}
			}
		}
		
		return indexableDoc;
	}

}