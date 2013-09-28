package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.WHITESPACE)
public class WhitespaceRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "";
			int nTokens = 0, i = 0, count = 0;
			nTokens = stream.getAllTokens().size();
			stream.reset();
			while (count < nTokens) {
				token = stream.next();
				stream.previous();
				stream.remove();
				System.out.println("Token:" + token);
				if (token != null) {
					String[] tempArr = token.trim().split("[ \n\r]+");
					for (i = 0; i < tempArr.length; i++) {
						stream.seekEnd();
						stream.append(tempArr[i]);
						System.out.println(tempArr[i]);
					}
					count++;
				}
			}
			stream.reset();
		}
	}
}
