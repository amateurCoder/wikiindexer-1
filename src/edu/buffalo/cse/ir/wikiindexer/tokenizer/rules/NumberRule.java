package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.NUMBERS)
public class NumberRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "";
			String [] tempArr;
			int nTokens = 0, i = 0, count = 0;
			nTokens = stream.getAllTokens().size();
			stream.reset();
			while (count < nTokens) {
				token = stream.next();
				stream.previous();
				stream.remove();
				// stream.next();
				System.out.println("Token:" + token);
				if (token != null) {
					if (token.matches(".*? ?[0-9]+[.,]*[0-9]* ?.*")) {
						if (token.matches(".*? ?[0-9]+[\\.,]*[0-9]* ?.*")) {
							System.out.println("Block 1:"+token);
							token = token.replaceAll(" ?[0-9]+[\\.,]*[0-9]* ?",
									" ");
						}
						if (token
								.matches(".*?([0-9]+[\\.,]*[0-9]*[^\\s].*|[^\\s][0-9]+[\\.,]*[0-9]*).*")) {
							System.out.println("Block 2:"+token);
							token = token.replaceAll("[0-9]+[\\.,]*[0-9]*", "");
						}
						
					}
					System.out.println("Final Token:" + token);

//					tempArr = token.trim().split(" +");
////					token = "";
//					for (i = 0; i < tempArr.length; i++) {
					if(token.trim().length()>0){
						stream.seekEnd();
						stream.append(token.trim());
					}	
						// stream.next();
//						System.out.println("Temp" + token);
//					}
					count++;
				}
			}
			stream.reset();
		}
	}
}
