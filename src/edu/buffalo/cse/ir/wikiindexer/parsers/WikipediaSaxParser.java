package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.util.Collection;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;

public class WikipediaSaxParser extends Parser implements ContentHandler {

	public WikipediaSaxParser(Properties idxProps,
			Collection<WikipediaDocument> docs) {
		super(idxProps);
		this.wikiDocs = docs;
		// TODO Auto-generated constructor stub
	}

	private boolean boolTitle;
	private boolean boolId;
	private boolean boolRevision;
	private boolean boolTimestamp;
	private boolean boolContributor;
	private boolean boolIP;
	private boolean boolUsername;
	private boolean boolText;

	private String title;
	private int id;
	private String timestamp;
	private String ipOrUsername;

	private Collection<WikipediaDocument> wikiDocs;
	private WikipediaDocument wikipediaDocument;
	private StringBuilder textBuffer;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (boolTitle) {
			title = new String(ch, start, length);
		} else if (boolTimestamp) {
			timestamp = new String(ch, start, length);
		} else if ((boolContributor) && ((boolIP) || (boolUsername))) {
			ipOrUsername = new String(ch, start, length);
		} else if ((!boolRevision) && (boolId)) {
			String str = new String(ch, start, length);
			id = Integer.parseInt(str);
		} else if (boolText) {
			textBuffer.append(ch, start, length);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(String uri, String localname, String tagName)
			throws SAXException {
		if (("title").equalsIgnoreCase(tagName)) {
			
			boolTitle = false;
		} else if (("id").equalsIgnoreCase(tagName)) {
			boolId = false;
		} else if (("revision").equalsIgnoreCase(tagName)) {
			boolRevision = false;
		} else if (("timestamp").equalsIgnoreCase(tagName)) {
			boolTimestamp = false;
		} else if (("contributor").equalsIgnoreCase(tagName)) {
			boolContributor = false;
		} else if (("ip").equalsIgnoreCase(tagName)) {
			boolIP = false;
		} else if (("username").equalsIgnoreCase(tagName)) {
			boolUsername = false;
		} else if (("text").equalsIgnoreCase(tagName)) {
			boolText = false;
		} else if (("page").equalsIgnoreCase(tagName)) {
			WikipediaParser wikipediaParser = new WikipediaParser(title, id,
					timestamp, ipOrUsername, textBuffer.toString());
			wikipediaDocument = wikipediaParser.getWikipediaDocument();
			
			add(wikipediaDocument, wikiDocs);
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("in start document");

	}

	@Override
	public void startElement(String uri, String localname, String tagName,
			Attributes attributes) throws SAXException {
		if (("title").equalsIgnoreCase(tagName)) {
			boolTitle = true;
		} else if (("id").equalsIgnoreCase(tagName)) {
			boolId = true;
		} else if (("revision").equalsIgnoreCase(tagName)) {
			boolRevision = true;
		} else if (("timestamp").equalsIgnoreCase(tagName)) {
			boolTimestamp = true;
		} else if (("contributor").equalsIgnoreCase(tagName)) {
			boolContributor = true;
		} else if (("ip").equalsIgnoreCase(tagName)) {
			boolIP = true;
		} else if (("username").equalsIgnoreCase(tagName)) {
			boolUsername = true;
		} else if (("text").equalsIgnoreCase(tagName)) {
			textBuffer = new StringBuilder();
			boolText = true;
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}
}
