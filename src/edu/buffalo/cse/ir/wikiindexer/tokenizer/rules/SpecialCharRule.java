package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.Collection;
import java.util.Iterator;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.EnglishStemmer.Stemmer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.SPECIALCHARS)
public class SpecialCharRule implements TokenizerRule {

	// TODO Execute commented out test case
	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "",finalToken="";
			String[] tempArr;
			int nTokens = 0, i = 0, count = 0;
			nTokens = stream.getAllTokens().size();
			// stream.previous();
			stream.reset();
			while (count < nTokens) {
				token = stream.next();
				if (token != null) {
					if (nTokens == 1) {
						if (token
								.matches(".*?[~@#$%\\*=\\^&\\+:;<>\\|_/\\\\\\(\\)]+.*?")) {
							token = token
									.replaceAll(
											"[~@#\\$%\\*=\\^\\&\\+:;<>\\|_/\\\\\\(\\)]+",
											" ");
							tempArr = token.trim().split(" +");
							finalToken="";
							for (i = 0; i < tempArr.length; i++) {
								finalToken+=tempArr[i]+" ";
							}
							finalToken= finalToken.trim();
							stream.previous();
							stream.set(finalToken);
						}
					}else if(nTokens>1){
						if (token
								.matches(".*?[~@#$%\\*=\\^&\\+:;<>\\|_/\\\\\\(\\) ]+.*?")) {
							tempArr = token.trim().split("[~@#\\$%\\*=\\^\\&\\+:;<>\\|_/\\\\\\(\\)]+");
							if(tempArr.length==1){
								stream.previous();
								stream.set(tempArr[0].trim());
								stream.next();
							}
							else{
								stream.previous();
								stream.remove();
								for (i = 0; i < tempArr.length; i++) {
									if(tempArr[i].trim().length()>0){
										stream.seekEnd();
										stream.append(tempArr[i]);
									}
									
								}
							}
							
						}
					}
						else{
							stream.previous();
							stream.set(token);
							stream.next();
						}
				}
				count++;
			}
			stream.reset();
		}
	}
}
