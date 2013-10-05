package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.PUNCTUATION)
public class PunctuationRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			System.out.println("Incoming Stream in punctuation:" + stream.getAllTokens());
			String token;
			stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				if (token != null) {
					token=token.replaceAll(" +", " ");
					if (token
							.matches(".*?[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.?[a-zA-Z0-9]*\\.?[a-zA-Z0-9]*(\\?|!)")) {
						token = token.replaceAll("(\\?|!)", "");
						stream.previous();
						stream.set(token);
					} else if (token.matches(".*?(\\.|\\?|!)")) {
						token = token.replaceAll("(\\.|\\?|!)", "");
						stream.previous();
						stream.set(token);
					}
				}

			}
			stream.reset();
			System.out.println("Outgoing Stream in punctuation:" + stream.getAllTokens());
		}

	}

}
