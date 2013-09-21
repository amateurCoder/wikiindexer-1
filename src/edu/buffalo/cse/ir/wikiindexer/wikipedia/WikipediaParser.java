/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.text.ParseException;

/**
 * @author nikhillo This class implements Wikipedia markup processing. Wikipedia
 *         markup details are presented here:
 *         http://en.wikipedia.org/wiki/Help:Wiki_markup It is expected that all
 *         methods marked "todo" will be implemented by students. All methods
 *         are static as the class is not expected to maintain any state.
 */
public class WikipediaParser {

	private String title;
	private int id;
	private String timestamp;
	private String ipOrUserName;
	private String text;

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
	 * Method to parse section titles or headings. Refer:
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#Sections
	 * 
	 * @param titleStr
	 *            : The string to be parsed
	 * @return The parsed string with the markup removed
	 */
	public static String parseSectionTitle(String titleStr) {
		if (null != titleStr) {
			return titleStr.replaceAll("( {0,1}={1,6} {0,1})", "");
		}
		return null;
	}

	/* TODO */
	/**
	 * Method to parse list items (ordered, unordered and definition lists).
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Lists
	 * 
	 * @param itemText
	 *            : The string to be parsed
	 * @return The parsed string with markup removed
	 */
	public static String parseListItem(String itemText) {
		if (null != itemText) {
			return itemText.replaceAll("[*#;:]+ ", "");
		}
		return null;
	}

	/* TODO */
	/**
	 * Method to parse text formatting: bold and italics. Refer:
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#Text_formatting first point
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTextFormatting(String text) {
		if (null != text) {
			return text.replaceAll("['{2}'{3}'{5}]", "");
		}
		return null;
	}

	/* TODO */
	/**
	 * Method to parse *any* HTML style tags like: <xyz ...> </xyz> For most
	 * cases, simply removing the tags should work.
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return The parsed text with the markup removed.
	 */
	public static String parseTagFormatting(String text) {
		if (null != text) {
			// return
			// text.replaceAll("[^(<[^>]*> )( </{0,1}[^>]*/{0,1}> )( </{0,1}[^>]*>)$]","");
			// return text.replaceAll("[(^<[^>]*> )( <[^>]*>)( <[^>]>)]","");
		}
		return null;
	}

	/* TODO */
	/**
	 * Method to parse wikipedia templates. These are *any* {{xyz}} tags For
	 * most cases, simply removing the tags should work.
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTemplates(String text) {
		if (null != text) {
			while (text.indexOf("{{") != -1) {
				text = text.replaceAll("\\{\\{[^{}]*\\}\\}", "");
			}
			return text;
		}
		return null;
	}

	/* TODO */
	/**
	 * Method to parse links and URLs. Refer:
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return An array containing two elements as follows - The 0th element is
	 *         the parsed text as visible to the user on the page The 1st
	 *         element is the link url
	 */
	public static String[] parseLinks(String text) {
		return null;
	}

	public WikipediaDocument getWikipediaDocument() {
		String tempText = null;
		try {
			wikipediaDocument = new WikipediaDocument(id, timestamp,
					ipOrUserName, title);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tempText = parseTemplates(text);
		tempText = parseTextFormatting(tempText);

		// Fetching section
		String[] sectionArr = tempText.split("[=]{2,6}");
		if (sectionArr.length > 1) {
			for (int i = 1; i < sectionArr.length; i = i + 2) {
				 System.out.println("Array " + i + "=" +
				 sectionArr[i].trim());
				 System.out.println("Body " + (i + 1) + "="
				 + sectionArr[i + 1].trim());
				wikipediaDocument.addSection(sectionArr[i], sectionArr[i + 1]);
			}
		} 
		//In case no section is present, setting it to value "Default";
		else if (sectionArr.length == 1){
			for (int i = 0; i < sectionArr.length; i++) {
			}
			wikipediaDocument.addSection("Default", "");
		}

		/*
		 * pattern = Pattern.compile("=+[A-Za-z ]+=+"); matcher =
		 * pattern.matcher(text); while (matcher.find()) { section =
		 * parseSectionTitle(matcher.group(0)); if (null != section) { // TODO:
		 * fetch content of section wikipediaDocument.addSection(section, ""); }
		 * } if (null == section) { // TODO: Fetch content of default
		 * wikipediaDocument.addSection("Default", ""); }
		 * 
		 * // TODO: Fetching other fields // Fetching Lists pattern =
		 * Pattern.compile("(?m)^[*#:;]+ *.*"); matcher = pattern.matcher(text);
		 * while (matcher.find()) { // System.out.println(matcher.group(0)); }
		 * 
		 * // Fetching Categories pattern =
		 * Pattern.compile("\\[\\[Category:[0-9A-Za-z ]+\\]\\]"); matcher =
		 * pattern.matcher(text); while (matcher.find()) { //
		 * System.out.println(matcher.group(0)); }
		 */

		return wikipediaDocument;
	}

}
