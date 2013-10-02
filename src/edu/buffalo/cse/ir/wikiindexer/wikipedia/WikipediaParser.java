/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			return itemText.replaceAll("(?m)^[*#;:]+ *", "");
		}
		return null;
	}

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
			return text.replaceAll("('{5}|'{3}|'{2})", "");
		}
		return null;
	}

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
			return text.replaceAll(
					"<[^>/]*> ?| ?<.*?>|<.*?/.*?>|&lt;([^.]*?)&gt;", "");

		}
		return null;
	}

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
		String tempStr[] = new String[2];
		tempStr[0] = "";
		tempStr[1] = "";

		if (null != text) {
			// case-1
			if (text.matches("\\[\\[[0-9A-Za-z- \\|0-9A-Za-z- ]+\\]\\]")) {
				String temp[] = text.split("\\|");
				if (temp.length > 1) {
					tempStr[0] = temp[1].replaceAll("\\[\\[|\\]\\]", "");
					tempStr[1] = temp[0].replaceAll("\\]\\]|\\[\\[", "");
				} else {
					tempStr[0] = temp[0].replaceAll("\\[\\[|\\]\\]", "");
					tempStr[1] = temp[0].replaceAll("\\]\\]|\\[\\[", "");
				}
				tempStr[1] = tempStr[1].replaceAll(" ", "_");
				tempStr[1] = capitalize(tempStr[1]);
				
				return tempStr;
			}

			// for link with stupid (brackets)
			// case1&case2
			if (text.matches("\\[+.* \\(*.*\\)*\\|.*?\\]+.*")) {
				if (text.matches("\\[+.* \\(*.*\\)*\\|\\]+")) {
					tempStr[1] = text.replaceAll("\\[|\\]|\\|", "");
					tempStr[1] = tempStr[1].replaceAll(" ", "_");
					tempStr[1] = Character.toUpperCase(tempStr[1].charAt(0))
							+ tempStr[1].substring(1);
					String temp[] = text.split("\\ ");
					tempStr[0] = temp[0].replaceAll("\\[|,", "");
				} else {
					String temp[] = text.split("\\|");
					tempStr[0] = temp[1].replaceAll("\\]", "");
					tempStr[1] = temp[0].replaceAll("\\[|\\]|\\|", "");
					tempStr[1] = tempStr[1].replaceAll(" ", "_");
					tempStr[1] = Character.toUpperCase(tempStr[1].charAt(0))
							+ tempStr[1].substring(1);
				}
			}
				 
			

			// case with :
			if (text.matches("\\[\\[.*:.*\\|*\\]\\]+")) {
				if (text.matches("\\[+Wikipedia:.* *\\|\\]+")) {
					// case4
					if (text.matches("\\[+Wikipedia:.* *\\(+.*\\)+\\|\\]+")) {
						String temp[] = text.split("\\:");
						String temp2[] = temp[1].split("\\(");
						tempStr[0] = temp2[0].trim();

						tempStr[1] = "";
						return tempStr;

					}
					// case14
					if (text.matches("\\[+Wikipedia:.*\\#.*\\|\\]+")) {
						tempStr[0] = text.replaceAll("\\[|\\||\\]", "");
						tempStr[1] = "";
						return tempStr;

					}
					// case3
					String temp[] = text.split("\\:");
					tempStr[0] = temp[1].replaceAll("\\[|\\||\\]", "");

					tempStr[1] = "";
					return tempStr;

				}

				// case 5,6
				if (text.matches("\\[+Wiktionary:.*\\|*\\]+")) {
					if (text.matches("\\[+Wiktionary:.*\\|+\\]+")) {
						String temp[] = text.split("\\|");

						tempStr[0] = temp[0].replaceAll(
								"\\[|Wiktionary:|\\]|\\|", "");
						tempStr[1] = "";
						return tempStr;
					}

					String temp[] = text.split("\\|");

					tempStr[0] = temp[0].replaceAll("\\[|\\]|\\|", "");

					tempStr[1] = "";
					return tempStr;

				}
				// case15
				if (text.matches("\\[+Category:.*?\\]+")) {
					String temp[] = text.split("\\:");
					tempStr[0] = temp[1].replaceAll("\\[|\\]|\\|", "");
					tempStr[1] = "";
					return tempStr;
				}
				// case16
				if (text.matches("\\[+:Category:.*?\\]+")) {

					tempStr[0] = text.replaceAll("\\[|\\]|\\|", "");
					tempStr[0] = tempStr[0].replaceFirst(":", "");
					tempStr[1] = "";
					return tempStr;
				}
				// case7,9 & 10
				if (text.matches("\\[+.*:.*\\|.*\\]+")) {
					String temp[] = text.split("\\|");

					if (temp.length == 0) {
						tempStr[0] = "";
					} else {
						tempStr[0] = temp[temp.length - 1]
								.replaceAll("\\]", "");
					}
					tempStr[1] = "";
					return tempStr;

				}

				// case08 & 17
				text = text.replaceAll("\\[|\\]", "");
				String temp[] = text.split("\\:");
				if (temp[0].equals("File")) {
					tempStr[0] = "";
					tempStr[1] = "";
				} else {
					tempStr[0] = text;
					tempStr[1] = "";
				}
				return tempStr;

			}

			// for stupid links with auto capitaliztion
			// case 0, 11, 12, 13
			if (text.matches(".* \\[+.*\\]+.*")) {
				if (text.matches(".* \\[+.*\\|.*\\]+.*")) {
					String tempStr2 = "";
					Pattern pattern = Pattern.compile("\\[\\[.*\\]\\]");
					Matcher matcher = pattern.matcher(text);
					while (matcher.find()) {
						String temp[] = matcher.group(0).split("\\|");
						tempStr[1] = temp[0];
						tempStr2 = temp[1].replaceAll("\\]", ""); // to hold
																	// public
																	// transportation
					}

					tempStr[1] = tempStr[1].replaceAll(" ", "_");
					tempStr[1] = tempStr[1].replaceAll("\\[|\\]", "");

					tempStr[1] = Character.toUpperCase(tempStr[1].charAt(0))
							+ tempStr[1].substring(1);

					String temp[] = text.split("\\[\\[");
					tempStr[0] = temp[0] + tempStr2;

					return tempStr;

				}
				// Pattern pattern =
				// Pattern.compile("\\[\\[[0-9A-Za-z ]+\\]\\]");
				Pattern pattern = Pattern.compile("\\[\\[.*\\]\\]");
				Matcher matcher = pattern.matcher(text);
				while (matcher.find()) {
					tempStr[1] = matcher.group(0).replaceAll(" ", "_");
				}
				tempStr[1] = tempStr[1].replaceAll("\\[|\\]", "");

				tempStr[1] = tempStr[1].replaceAll("<nowiki \\/>", "");
				tempStr[1] = Character.toUpperCase(tempStr[1].charAt(0))
						+ tempStr[1].substring(1);

				tempStr[0] = text.replaceAll("\\[|\\]", "");
				tempStr[0] = tempStr[0].replaceAll("<nowiki \\/>", "");
				tempStr[0] = tempStr[0].replaceAll("\\.", "");
				return tempStr;
			}

			if (text.matches("\\[http:\\/\\/www\\.wikipedia\\.org.*\\]")) {
				if (text.matches("\\[http:\\/\\/www\\.wikipedia\\.org .*\\]")) {
					String temp[] = text.split(" ");
					tempStr[0] = temp[1].replaceAll("\\]", "");
				} else
					tempStr[0] = "";

				tempStr[1] = "";

			}
		}

		return tempStr;
	}

	private static String capitalize(String temp) {
		temp = Character.toUpperCase(temp.charAt(0)) + temp.substring(1);
		return temp;
	}

	public WikipediaDocument getWikipediaDocument() {
		String tempText = null;
		String tempArr[] = null;
		try {
			wikipediaDocument = new WikipediaDocument(id, timestamp,
					ipOrUserName, title);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Lang links - what to populate in Wikipedia document
		// TODO Refactor regex expression to constants
		// TODO Accented chars
		// TODO Move RegEx in constants
		// TODO what to do with LINK URl

		tempText = parseTemplates(text);
		tempText = parseTextFormatting(tempText);
		tempText = parseTagFormatting(tempText);
		tempText = parseListItem(tempText);

		// Fetching links

		// Fetching Categories - case 15 & 16
		pattern = Pattern.compile("\\[\\[:?Category:.*?\\]\\]");
		matcher = pattern.matcher(tempText);
		while (matcher.find()) {
			tempArr = parseLinks(matcher.group(0));
			if (!tempArr[0].trim().equals(null)) {
				wikipediaDocument.addCategory(tempArr[0].trim());
			}
			tempText = tempText.replace(matcher.group(0), "");
		}

		pattern = Pattern.compile("\\[\\[[a-z]*?:.*?\\]\\]");
		matcher = pattern.matcher(tempText);
		while (matcher.find()) {
			tempArr = parseLinks(matcher.group(0).trim());
			if (!tempArr[0].trim().equals(null)) {
				String[] code = tempArr[0].split(":");
				wikipediaDocument.addLangLink(code[0], tempArr[1]);
			}
			tempText = tempText.replace(matcher.group(0), tempArr[0]);
		}

		pattern = Pattern
				.compile("\\[\\[[0-9A-Za-z\\W \\|0-9A-Za-z\\W ]*?\\]\\]");
		matcher = pattern.matcher(tempText);
		while (matcher.find()) {
			tempArr = parseLinks(matcher.group(0).trim());
			if (!tempArr[0].trim().equals(null)&&!tempArr[0].trim().equals("")) {
				wikipediaDocument.addLink(tempArr[0].trim());
			}
			tempText = tempText.replace(matcher.group(0), tempArr[0]);
		}

		// Fetching section
		String[] sectionArr = tempText.split("[=]{2,6}");
		if (sectionArr.length > 1) {
			for (int i = 0; i < sectionArr.length; i++) {
				if (i == 0) {
					// System.out.println("Header " + "Default");
					// System.out.println("Body " + sectionArr[i].trim());
					wikipediaDocument.addSection("Default",
							sectionArr[i].trim());
				} else {
					// System.out.println("Header " + sectionArr[i].trim());
					// System.out.println("Body " + sectionArr[i + 1].trim());
					wikipediaDocument.addSection(sectionArr[i].trim(),
							sectionArr[i + 1].trim());
					i = i + 1;
				}
			}
		}
		// In case no section is present, setting it to value "Default";
		else if (sectionArr.length == 1) {
			for (int i = 0; i < sectionArr.length; i++) {
				wikipediaDocument.addSection("Default", sectionArr[i].trim());
				// System.out.println("Header " + "Default");
				// System.out.println("Body " + sectionArr[i].trim());
			}

		}
		return wikipediaDocument;
	}

}
