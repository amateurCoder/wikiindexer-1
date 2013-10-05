/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author nikhillo
 * This class represents a subclass of a Dictionary class that is
 * shared by multiple threads. All methods in this class are
 * synchronized for the same reason.
 */
public class SharedDictionary extends Dictionary {
	
	/**
	 * Public default constructor
	 * @param props: The properties file
	 * @param field: The field being indexed by this dictionary
	 */
	private static int docId=0;
	
	public SharedDictionary(Properties props, INDEXFIELD field) {
		super(props, field);
		// TODO Add more code here if needed
		
		
		//Dictionary.docString+="nil";
		//compressedLinkDictionary.put(docString.indexOf("nil"),docId++);
	}
	
	/**
	 * Method to lookup and possibly add a m	 * @param value: The value to be looked up
apping for the given value
	 * in the dictionary. The class should first try and find the given
	 * value within its dictionary. If found, it should return its
	 * id (Or hash value). If not found, it should create an entry and
	 * return the newly created id.
	 * @return The id as explained above.
	 */
	public synchronized int lookup(String value) {
		//TODO Implement this method
		
		
		if(docString.contains(value))
			
		{
			return compressedLinkDictionary.get(docString.indexOf(value));
		}
		else
		{
			docString+=value; 
			
			compressedLinkDictionary.put(docString.indexOf(value),docId);
			
			return docId++;
		}		

		}
	
	public synchronized String reverseLookup(int value) {
		//TODO Implement this method
	
		Set<Integer> setIndexValues = compressedLinkDictionary.keySet();		
		 
		int firstIndex=0;
		int secondIndex=0;
		boolean gotFirstTerm=false;
		 
		Iterator<Integer> indexValuesIterator= setIndexValues.iterator();
		
		for (Map.Entry<Integer, Integer> entry : compressedLinkDictionary.entrySet()) {
			if(gotFirstTerm)
	         {
	        	 secondIndex=entry.getKey();
	        	 break;
	         }
	         if (value==entry.getValue()) {
	             
	        	 firstIndex=entry.getKey();
	        	 gotFirstTerm =true;
	         }
	         
	         
	     }
		
		
		return (String)docString.subSequence(firstIndex, secondIndex);

		}
	
	
}