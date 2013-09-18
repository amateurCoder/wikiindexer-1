package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.text.ParseException;
import java.util.Collection;
import java.util.Properties;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;

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
	private String text;

	Collection<WikipediaDocument> wikiDocs;
	private WikipediaDocument wikipediaDocument;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (boolTitle) {
			title = new String(ch, start, length);
			System.out.println("Title :" + title);
		} else if (boolTimestamp) {
			timestamp = new String(ch, start, length);
			System.out.println("Time :" + timestamp);
		} else if ((boolContributor) && ((boolIP) || (boolUsername))) {
			ipOrUsername = new String(ch, start, length);
			System.out.println("IP/Username :" + ipOrUsername);
		} else if ((!boolRevision) && (boolId)) {
			String str = new String(ch, start, length);
			id = Integer.parseInt(str);
			System.out.println("Id :" + id);
		}	else if (boolText){
			text = new String(ch, start, length);
			System.out.println("Text :" + text);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(String uri, String localname, String tagName)
			throws SAXException {
		if (("title").equals(tagName)) {
			boolTitle = false;
		} else if (("id").equals(tagName)) {
			boolId = false;
		} else if (("revision").equals(tagName)) {
			boolRevision = false;
		} else if (("timestamp").equals(tagName)) {
			boolTimestamp = false;
		} else if (("contributor").equals(tagName)) {
			boolContributor = false;
		} else if (("ip").equals(tagName)) {
			boolIP = false;
		} else if (("username").equals(tagName)) {
			boolUsername = false;
		} else if (("text").equals(tagName)) {
			boolText = false;
		} else if (("page").equals(tagName)) {
			try {
				wikipediaDocument = new WikipediaDocument(id, timestamp,
						ipOrUsername, title);
				add(wikipediaDocument, wikiDocs);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	}

	@Override
	public void startElement(String uri, String localname, String tagName,
			Attributes attributes) throws SAXException {
		if (("title").equals(tagName)) {
			boolTitle = true;
		} else if (("id").equals(tagName)) {
			boolId = true;
		} else if (("revision").equals(tagName)) {
			boolRevision = true;
		} else if (("timestamp").equals(tagName)) {
			boolTimestamp = true;
		} else if (("contributor").equals(tagName)) {
			boolContributor = true;
		} else if (("ip").equals(tagName)) {
			boolIP = true;
		} else if (("username").equals(tagName)) {
			boolUsername = true;
		} else if (("text").equals(tagName)) {
			boolText = true;
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}
}
