package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DATES)
public class DatesRule implements TokenizerRule {

	String[] months = { "january", "february", "march", "april", "may", "june",
			"july", "august", "september", "october", "november", "december" };
	String year, month, day, hour, minute, second;
	int dayIndex, yearIndex, monthIndex, minIndex, maxIndex, timeIndex;

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "", finalToken = "";
			int nTokens = 0;
			stream.previous();
			// Accumulating all the tokens in the stream
			while (stream.hasNext()) {
				nTokens++;
				token = stream.next();
				if (token != null) {
					if (stream.hasNext()) {
						finalToken += token + " ";
					} else {
						finalToken += token;
					}
				}
			}
			System.out.println("Final token:" + finalToken);

			if (finalToken != null) {
				if(finalToken.matches(" \\d+ [A-Za-z]+ \\d+ ")){
					
				}
			}
		}
	}
}
