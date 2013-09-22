/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.buffalo.cse.ir.wikiindexer.FileUtil;
import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;

/**
 * @author nikhillo
 * 
 */
public class Parser {
	/* */
	private final Properties props;

	/**
	 * 
	 * @param idxConfig
	 * @param parser
	 */
	public Parser(Properties idxProps) {
		props = idxProps;

	}

	/* TODO: Implement this method */
	/**
	 * 
	 * @param filename
	 * @param docs
	 * @throws Exception
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public void parse(String filename, Collection<WikipediaDocument> docs) {

		System.out.println("in parse");
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = saxParserFactory.newSAXParser();
			XMLReader xmlReader;

			xmlReader = saxParser.getXMLReader();

			xmlReader.setContentHandler(new WikipediaSaxParser(props, docs));
			if (filename != null && filename != "") {
				if ((FileUtil.getRootFilesFolder(props) + props
						.getProperty(IndexerConstants.DUMP_FILENAME))
						.equals(filename)) {
					xmlReader.parse(filename);
				}
			}
		} catch (IOException | SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		}
	}

	/**
	 * Method to add the given document to the collection. PLEASE USE THIS
	 * METHOD TO POPULATE THE COLLECTION AS YOU PARSE DOCUMENTS For better
	 * performance, add the document to the collection only after you have
	 * completely populated it, i.e., parsing is complete for that document.
	 * 
	 * @param doc
	 *            : The WikipediaDocument to be added
	 * @param documents
	 *            : The collection of WikipediaDocuments to be added to
	 */
	protected synchronized void add(WikipediaDocument doc,
			Collection<WikipediaDocument> documents) {
		
		documents.add(doc);
		

	}
}
