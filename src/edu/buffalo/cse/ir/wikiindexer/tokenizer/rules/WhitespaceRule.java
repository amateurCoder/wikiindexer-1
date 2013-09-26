package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.WHITESPACE)
public class WhitespaceRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = null;
			stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				if (token != null) {
					String[] tempArr = token.trim().split("[ \n\r]+");
					stream.previous();
					for (int i = 0; i < tempArr.length; i++) {
						if (stream.hasNext()) {
							stream.set(tempArr[i]);
							stream.next();
						} else {
							stream.append(tempArr[i]);
							stream.seekEnd();
						}
					}
				}
			}
			stream.reset();
		}
	}
}
