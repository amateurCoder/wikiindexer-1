/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.Properties;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.*;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.test.StopWordsRuleTest;

/**
 * Factory class to instantiate a Tokenizer instance
 * The expectation is that you need to decide which rules to apply for which field
 * Thus, given a field type, initialize the applicable rules and create the tokenizer
 * @author nikhillo
 *
 */
public class TokenizerFactory {
	//private instance, we just want one factory
	private static TokenizerFactory factory;
	
	//properties file, if you want to read soemthing for the tokenizers
	private static Properties props;
	
	/**
	 * Private constructor, singleton
	 */
	private TokenizerFactory() {
		//TODO: Implement this method
	}
	
	/**
	 * MEthod to get an instance of the factory class
	 * @return The factory instance
	 */
	public static TokenizerFactory getInstance(Properties idxProps) {
		if (factory == null) {
			factory = new TokenizerFactory();
			props = idxProps;
		}
		
		return factory;
	}
	
	/**
	 * Method to get a fully initialized tokenizer for a given field type
	 * @param field: The field for which to instantiate tokenizer
	 * @return The fully initialized tokenizer
	 * @throws TokenizerException 
	 */
	public Tokenizer getTokenizer(INDEXFIELD field) {
		//TODO: Implement this method
		/*
		 * For example, for field F1 I want to apply rules R1, R3 and R5
		 * For F2, the rules are R1, R2, R3, R4 and R5 both in order
		 * So the pseudo-code will be like:
		 * if (field == F1)
		 * 		return new Tokenizer(new R1(), new R3(), new R5())
		 * else if (field == F2)
		 * 		return new TOkenizer(new R1(), new R2(), new R3(), new R4(), new R5())
		 * ... etc
		 */
		
		try {
			if(field.toString().equalsIgnoreCase("AUTHOR"))
			{
				return new Tokenizer();
			
			}
			if(field.toString().equalsIgnoreCase("LINK"))
			{
				return new Tokenizer(new CapitalizationRule());
			
			}
			if(field.toString().equalsIgnoreCase("CATEGORY"))
			{
				return new Tokenizer();
			
			}
			if(field.toString().equalsIgnoreCase("TERM"))
			{
				return new Tokenizer(/*new StopWordsRule(),new WhitespaceRule(),new AccentRule(),new SpecialCharRule(), /*Date?*//*new NumberRule(),new PunctuationRule(),new ApostropheRule(),new HyphenRule(),new StopWordsRule()*/);
			
			}
		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
