package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.HYPHEN)
public class HyphenRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			System.out.println("Incoming Stream in hyphen:" + stream.getAllTokens());
			String token;
			// TODO: Remove stream.previous();
			stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				if (token != null) {
					if (token.matches(" *?-+ *")) {
						stream.previous();
						stream.remove();
					}else if (token.matches("[a-z]*-+[a-z]*")){
						if(token.matches("[a-z]*-[a-z]*")){
							token = token.replaceAll("-+", " ");
						}else if(token.matches("[a-z]*(-){2,3}[a-z]*"))
							token = token.replaceAll("(-){2,3}", "");
						stream.previous();
						stream.set(token);
					}
				}
			}
			stream.reset();
			System.out.println("Outgoing Stream in hyphen:" + stream.getAllTokens());
		}
	}
}
