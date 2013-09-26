package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DATES)
public class DatesRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token;
			stream.previous();
			while (stream.hasNext()) { 
				token = stream.next();
				System.out.println("Incoming Token:" + token);
				if(token!=null){
					if (token.matches(".*?(\\.|\\?|!)")) {
						token = token.replaceAll("(\\.|\\?|!)", "");
						System.out.println("Outgoing Token:" + token);
						stream.previous();
						stream.set(token);
					}
				}
				
			}
			stream.reset();
		}
	}

}
