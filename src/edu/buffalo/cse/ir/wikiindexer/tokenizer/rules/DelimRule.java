package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.Collection;
import java.util.Iterator;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DELIM)
public class DelimRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "", finalToken="";
			stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				finalToken+=token+" ";
			}	
			finalToken =  finalToken.trim();
				
				if (finalToken != null) {
					String[] tempArr = finalToken.trim().split("[\\/@:]");
					stream.reset();
					for (int i = 0; i < tempArr.length; i++) {
						if (stream.hasNext()) {
							stream.set(tempArr[i].trim());
							stream.next();
						} else {
							stream.append(tempArr[i].trim());
							stream.seekEnd();
						}
					}
				}
			}
			stream.reset();
		}
		
	}
