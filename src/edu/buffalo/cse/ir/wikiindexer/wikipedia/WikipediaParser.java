/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo
 * This class implements Wikipedia markup processing.
 * Wikipedia markup details are presented here: http://en.wikipedia.org/wiki/Help:Wiki_markup
 * It is expected that all methods marked "todo" will be implemented by students.
 * All methods are static as the class is not expected to maintain any state.
 */
public class WikipediaParser {
	
	private String title;
	private int id;
	private String timestamp;
	private String ipOrUserName;
	private String text;
	
	private Matcher matcher;
	private Pattern pattern;
	
	private WikipediaDocument wikipediaDocument;
	
	public WikipediaParser(String title, int id, String timestamp,
			String ipOrUsername, String text) {
		this.title = title;
		this.id = id;
		this.timestamp = timestamp;
		this.ipOrUserName = ipOrUsername;
		this.text = text;
	}

	/* TODO */
	/**
	 * Method to parse section titles or headings.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Sections
	 * @param titleStr: The string to be parsed
	 * @return The parsed string with the markup removed
	 */
	public static String parseSectionTitle(String titleStr) {
		if(null!=titleStr){
			return titleStr.replaceAll("( {0,1}={1,6} {0,1})","");
		}
		return null;
	}
	
	/* TODO */
	/**
	 * Method to parse list items (ordered, unordered and definition lists).
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Lists
	 * @param itemText: The string to be parsed
	 * @return The parsed string with markup removed
	 */
	public static String parseListItem(String itemText) {
		if(null!=itemText){
			return itemText.replaceAll("[*#;:]+ ","");
		}	
		return null;
	}
	
	/* TODO */
	/**
	 * Method to parse text formatting: bold and italics.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Text_formatting first point
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTextFormatting(String text) {
		if(null!=text){
			return text.replaceAll("['{2}'{3}'{5}]","");
		}
		return null;
	}
	
	/* TODO */
	/**
	 * Method to parse *any* HTML style tags like: <xyz ...> </xyz>
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed.
	 */
	public static String parseTagFormatting(String text) {
		if(null!=text){
//			return text.replaceAll("[^(<[^>]*> )( </{0,1}[^>]*/{0,1}> )( </{0,1}[^>]*>)$]","");
//			return text.replaceAll("[(^<[^>]*> )( <[^>]*>)( <[^>]>)]","");
		}
		return null;
	}
	
	/* TODO */
	/**
	 * Method to parse wikipedia templates. These are *any* {{xyz}} tags
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTemplates(String text) {
		if(null!=text){
			return text.replaceAll("^[{]{2}[^}]*[}]{2}$","");
		}
		return null;
	}
	
	
	/* TODO */
	/**
	 * Method to parse links and URLs.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * @param text: The text to be parsed
	 * @return An array containing two elements as follows - 
	 *  The 0th element is the parsed text as visible to the user on the page
	 *  The 1st element is the link url
	 */
	public static String[] parseLinks(String text) {
		return null;
	}

	public WikipediaDocument getWikipediaDocument() {
		String section = null;
		try {
			wikipediaDocument = new WikipediaDocument(id, timestamp, ipOrUserName, title);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Fetching section
		pattern = Pattern.compile("=+[A-Za-z ]+=+");
		matcher = pattern.matcher(text);
		while (matcher.find()) {
			section =parseSectionTitle(matcher.group(0));
			if(null!=section){
				//TODO: fetch content of section
				wikipediaDocument.addSection(section, "");
			}
		}
		if (null==section){
			wikipediaDocument.addSection("Default", "");
		}
		
		
		//TODO: Fetching other fields
		
		
		
		return wikipediaDocument;
	}
	
	
	
}
