/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer;

/**
 * 
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

/**
 * This class represents a stream of tokens as the name suggests. It wraps the
 * token stream and provides utility methods to manipulate it
 * 
 * @author nikhillo
 * 
 */
public class TokenStream implements Iterator<String> {

	/**
	 * Default constructor
	 * 
	 * @param bldr
	 *            : THe stringbuilder to seed the stream
	 */

	private ArrayList<String> collStream;
	private ListIterator<String> iterator;
	private String stream;
	private Map<String, Integer> countMap;

	public TokenStream(StringBuilder bldr) {
		// TODO: Implement this method

		collStream.add(bldr.toString());
	}

	/**
	 * Overloaded constructor
	 * 
	 * @param bldr
	 *            : THe stringbuilder to seed the stream
	 */
	public TokenStream(String string) {
		// TODO: Implement this method

		if (null != string && string.length() > 0) {
			collStream = new ArrayList<String>();
			iterator = collStream.listIterator();
			countMap = new HashMap<String, Integer>();
			countMap.put(string, 1);
			iterator.add(string);

		} else
			collStream = null;

	}

	/**
	 * Method to append tokens to the stream
	 * 
	 * @param tokens
	 *            : The tokens to be appended
	 */
	public void append(String... tokens) {
		// TODO: Implement this method
		if (null != tokens) {
			for (String value : tokens) {
				if (value != null && value.length() > 0 && iterator!=null) {
					iterator.add(value);
					if (countMap.get(value) == null) {
						countMap.put(value, 1);
						// iterator.add(value);
					} else {
						int count = countMap.get(value).intValue();
						count++;
						countMap.put(value, count);
						// iterator.add(value);
					}

				}

			}
			
			if(null!=collStream){
				iterator = collStream.listIterator();// reset
			}
			
		}

	}

	/**
	 * Method to retrieve a map of token to count mapping This map should
	 * contain the unique set of tokens as keys The values should be the number
	 * of occurrences of the token in the given stream
	 * 
	 * @return The map as described above, no restrictions on ordering
	 *         applicable
	 */
	public Map<String, Integer> getTokenMap() {
		// TODO: Implement this method
		if (countMap != null)
			return countMap;
		else

			return null;
	}

	/**
	 * Method to get the underlying token stream as a collection of tokens
	 * 
	 * @return A collection containing the ordered tokens as wrapped by this
	 *         stream Each token must be a separate element within the
	 *         collection. Operations on the returned collection should NOT
	 *         affect the token stream
	 */
	public Collection<String> getAllTokens() {
		// TODO: Implement this method

		return collStream;

	}

	/**
	 * Method to query for the given token within the stream
	 * 
	 * @param token
	 *            : The token to be queried
	 * @return: THe number of times it occurs within the stream, 0 if not found
	 */
	public int query(String token) {
		// TODO: Implement this method

		if (countMap != null) {
			if (countMap.get(token) != null)
				return countMap.get(token).intValue();

		}
		return 0;
	}

	/**
	 * Iterator method: Method to check if the stream has any more tokens
	 * 
	 * @return true if a token exists to iterate over, false otherwise
	 */
	public boolean hasNext() {
		// TODO: Implement this method
		if (null != iterator) {
			if (iterator.hasNext())
				return true;
			else
				return false;

		}
		return false;
	}

	/**
	 * Iterator method: Method to check if the stream has any more tokens
	 * 
	 * @return true if a token exists to iterate over, false otherwise
	 */
	public boolean hasPrevious() {
		// TODO: Implement this method
		if (null != iterator) {
			if (iterator.hasPrevious())
				return true;
			else
				return false;

		}
		return false;
	}

	/**
	 * Iterator method: Method to get the next token from the stream Callers
	 * must call the set method to modify the token, changing the value of the
	 * token returned by this method must not alter the stream
	 * 
	 * @return The next token from the stream, null if at the end
	 */
	public String next() {
		// TODO: Implement this method
		if (null != collStream) {

			if (iterator.hasNext())
				return iterator.next();
			else
				return null;
		} else
			return null;
	}

	/**
	 * Iterator method: Method to get the previous token from the stream Callers
	 * must call the set method to modify the token, changing the value of the
	 * token returned by this method must not alter the stream
	 * 
	 * @return The next token from the stream, null if at the end
	 */
	public String previous() {
		// TODO: Implement this method

		if (null != collStream) {
			if (iterator.hasPrevious())

				return iterator.previous();
			else
				return null;
		} else
			return null;
	}

	/**
	 * Iterator method: Method to remove the current token from the stream
	 */
	public void remove() {
		int count=0;
		if (null != iterator)
		{
			if (iterator.hasNext())
			{
				String temp = iterator.next();
				if(null!=countMap.get(temp))
				{
				 count = countMap.get(temp).intValue() - 1;
				if (count == 0)
					countMap.remove(temp);
				else
					countMap.put(temp, count);

				iterator.previous();
				iterator.remove();
				}
			}

		}

	}

	/**
	 * Method to merge the current token with the previous token, assumes
	 * whitespace separator between tokens when merged. The token iterator
	 * should now point to the newly merged token (i.e. the previous one)
	 * 
	 * @return true if the merge succeeded, false otherwise
	 */
	public boolean mergeWithPrevious() {
		// TODO: Implement this method
		if (null != iterator) {
			if (iterator.hasPrevious() && collStream.size() > 1) {
				String temp_1 = iterator.previous();

				iterator.remove();
				String temp_2 = iterator.next();
				iterator.set(temp_1 + " " + temp_2);
				iterator.previous();
				// to reduce the count for temp1 and temp2 also to add
				// tamp1_temp2 to map
				int count_1 = countMap.get(temp_1).intValue() - 1;
				int count_2 = countMap.get(temp_2).intValue() - 1;
				if (count_1 == 0)
					countMap.remove(temp_1);
				else
					countMap.put(temp_1, count_1);
				if (count_2 == 0)
					countMap.remove(temp_2);
				else
					countMap.put(temp_2, count_1);
				if (countMap.get(temp_1 + " " + temp_2) == null) {

					countMap.put(temp_1 + " " + temp_2, 1);

				} else {
					int count = countMap.get(temp_1 + " " + temp_2).intValue();
					count++;
					countMap.put(temp_1 + " " + temp_2, count);

				}

				return true;
			}
			return false;
		}

		return false;
	}

	/**
	 * Method to merge the current token with the next token, assumes whitespace
	 * separator between tokens when merged. The token iterator should now point
	 * to the newly merged token (i.e. the current one)
	 * 
	 * @return true if the merge succeeded, false otherwise
	 */
	public boolean mergeWithNext() {
		// TODO: Implement this method
		if (null != iterator) {
			if (iterator.hasNext() && collStream.size() > 1) {
				String temp_1 = iterator.next();
				iterator.remove();
				String temp_2 = iterator.next();
				iterator.set(temp_1 + " " + temp_2);
				iterator.previous();

				int count_1 = countMap.get(temp_1).intValue() - 1;
				int count_2 = countMap.get(temp_2).intValue() - 1;
				if (count_1 == 0)
					countMap.remove(temp_1);
				else
					countMap.put(temp_1, count_1);
				if (count_2 == 0)
					countMap.remove(temp_2);
				else
					countMap.put(temp_2, count_1);
				if (countMap.get(temp_1 + " " + temp_2) == null) {

					countMap.put(temp_1 + " " + temp_2, 1);

				} else {
					int count = countMap.get(temp_1 + " " + temp_2).intValue();
					count++;
					countMap.put(temp_1 + " " + temp_2, count);

				}
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Method to replace the current token with the given tokens The stream
	 * should be manipulated accordingly based upon the number of tokens set It
	 * is expected that remove will be called to delete a token instead of
	 * passing null or an empty string here. The iterator should point to the
	 * last set token, i.e, last token in the passed array.
	 * 
	 * @param newValue
	 *            : The array of new values with every new token as a separate
	 *            element within the array
	 */
	public void set(String... newValue) {
		// TODO: Implement this method
		if (null != iterator) {
			if (null != newValue[0] && newValue[0] != "" && iterator.hasNext()) {
				iterator.next();
				iterator.previous();
				iterator.set(newValue[0]);
			}
			for (int i = 1; i < newValue.length; i++) {

				if (null != newValue[i] && newValue[i] != ""
						&& iterator.hasNext()) {

					// iterator.previous();
					iterator.next();
					iterator.add(newValue[i]);
					iterator.previous();
				}

			}
		}
	}

	/**
	 * Iterator method: Method to reset the iterator to the start of the stream
	 * next must be called to get a token
	 */
	public void reset() {
		// TODO: Implement this method
		if (null != iterator)
			iterator = collStream.listIterator();
	}

	/**
	 * Iterator method: Method to set the iterator to beyond the last token in
	 * the stream previous must be called to get a token
	 */
	public void seekEnd() {
		if (null != iterator) {
			while (iterator.hasNext())
				iterator.next();
		}

	}

	/**
	 * Method to merge this stream with another stream
	 * 
	 * @param other
	 *            : The stream to be merged
	 */
	public void merge(TokenStream other) {
		// TODO: Implement this method

		if (this.hasNext()) {
			if (other != null) {

				Collection<String> tempColl = other.getAllTokens();
				if (null != tempColl) {
					Iterator<String> it2 = tempColl.iterator();
					while (it2.hasNext()) {
						this.seekEnd();
						this.append(it2.next());
					}
				}
			}

		} else
			this.collStream = other.collStream;

	}

	public ListIterator<String> getIterator() {
		// TODO Auto-generated method stub
		return iterator;
	}
}
