package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.NUMBERS)
public class NumberRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "", finalToken = "";
			String[] tempArr;
			int nTokens = 0;
			stream.previous();
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
			for (int i = 0; i < 2; i++) {
				if (finalToken != null) {
					if (finalToken.matches(".*? [0-9]+[.,]*[0-9]*.*")) {
						if (finalToken
								.matches(".*?\\s+[0-9]+[\\.,]*[0-9]*\\s+.*")) {
							finalToken = finalToken.replaceAll(
									" [0-9]+[\\.,]*[0-9]* ", " ");
						} else if (finalToken
								.matches("(.*? [0-9]+[\\.,]*[0-9]*[^\\s].*|.*?[^\\s][0-9]+[\\.,]*[0-9]* .*)")) {
							finalToken = finalToken.replaceAll(
									"[0-9]+[\\.,]*[0-9]*", "");
						}
					}
				}
			}
			tempArr = finalToken.trim().split(" +");
			finalToken = "";
			//In case only single token is present in the stream
			if (nTokens == 1) {
				for (int i = 0; i < (tempArr.length - 1); i++) {
					if (i == (tempArr.length - 2)) {
						finalToken += tempArr[i].trim() + " "
								+ tempArr[i + 1].trim();
					} else {
						finalToken += tempArr[i].trim() + " ";
					}
				}
				stream.previous();
				stream.set(finalToken);
				stream.seekEnd();
			//Multiple tokens in the stream
			} else if (nTokens > 1) {
				stream.reset();
				for (int i = 0; i < tempArr.length; i++) {
					if (stream.hasNext()) {
						stream.set(tempArr[i]);
						stream.next();
					} else {
						stream.append(tempArr[i]);
					}
				}
				stream.reset();
				for (int i = 0; i < tempArr.length; i++) {
					stream.next();
				}
				while (stream.hasNext()) {
					stream.remove();
				}
				stream.reset();
			}
		}
	}
}
