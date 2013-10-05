/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo An abstract class that represents a dictionary object for a
 *         given index
 */
public abstract class Dictionary implements Writeable {

	public static Map<String,Integer> linkDictionary;
	public  Map<Integer,Integer> compressedLinkDictionary;
	public  String docString;
	FileOutputStream fileOutputStream;
	ObjectOutputStream objectOutputStream;
	public Dictionary (Properties props, INDEXFIELD field) {
		//TODO Implement this method
		docString= new String("");
		
		linkDictionary = new HashMap<String,Integer>();
		compressedLinkDictionary = new LinkedHashMap<Integer,Integer>();

	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException {
		try {
			fileOutputStream = new FileOutputStream("files/dictMap.txt");
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(linkDictionary);
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
	public void cleanUp() throws IndexerException {
		try {
			objectOutputStream.close();
		} catch (IOException e) {
			throw new IndexerException(e.getMessage());
		}
	}

	/**
	 * Method to check if the given value exists in the dictionary or not Unlike
	 * the subclassed lookup methods, it only checks if the value exists and
	 * does not change the underlying data structure
	 * 
	 * @param value
	 *            : The value to be looked up
	 * @return true if found, false otherwise
	 */
	public boolean exists(String value) {
		//TODO Implement this method
		
		if(docString.contains(value))
		{
			
			return true;
		}
		
		return false;
	}
	

	/**
	 * MEthod to lookup a given string from the dictionary. The query string can
	 * be an exact match or have wild cards (* and ?) Must be implemented ONLY
	 * AS A BONUS
	 * 
	 * @param queryStr
	 *            : The query string to be searched
	 * @return A collection of ordered strings enumerating all matches if found
	 *         null if no match is found
	 */
	public Collection<String> query(String queryStr) {
		ArrayList<String> keyMatches = new ArrayList<String>();
		ListIterator<String> resultListIterator = keyMatches.listIterator();
		String match;
		int secondIndex=0;
		boolean hastext=false;
	
			queryStr=queryStr.replace("*", ".*");
			queryStr=queryStr.replace("?", ".?");
			Pattern queryPattern = Pattern.compile(queryStr);
		
			
			Set<Integer> index = compressedLinkDictionary.keySet();
			
			Iterator<Integer> indexIterator = index.iterator();
			indexIterator.next();
			int firstIndex = indexIterator.next();
			if(indexIterator.hasNext())
			{
			while(indexIterator.hasNext())
			{
				secondIndex= indexIterator.next();
				match=(String) docString.subSequence(firstIndex , secondIndex);
				firstIndex=secondIndex;
				Matcher m=queryPattern.matcher(match);
				if(m.matches())
				{
					resultListIterator.add(match);
					hastext = true;
				}
				
			}
			match=(String) docString.subSequence(secondIndex,docString.length());
			firstIndex=secondIndex;
			Matcher m=queryPattern.matcher(match);
			if(m.matches())
			{
				resultListIterator.add(match);
				hastext = true;
			}
			}
	
			if(hastext)
			{
				return keyMatches;
			}
			else
			{
				return null;
			}
			
	}
		
		
	

	/**
	 * Method to get the total number of terms in the dictionary
	 * 
	 * @return The size of the dictionary
	 */
	public int getTotalTerms() {
		//TODO: Implement this method
		//return linkDictionary.size();
		if(compressedLinkDictionary.size()>0)
		return (compressedLinkDictionary.size()-1);
		else
			return 0;
		

	}
}
