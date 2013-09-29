package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.CAPITALIZATION)
public class CapitalizationRule implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "", finalToken = "", str = "";
			String[] tempArr = null;
			int i, tokenCounter = 0;
			boolean boolFlag = false, firstTokenFlag = false;
			stream.previous();
			while (stream.hasNext()) {
				tokenCounter++;
				if (tokenCounter == 1) {
					firstTokenFlag = true;
				} else {
					firstTokenFlag = false;
				}
				token = stream.next();
				if (token != null) {
					// nTokens++;
					// Checking if First word in the token is of the form
					// Abcdef, if
					// yes converting to lower case
					if (token.matches("[A-Za-z0-9 ]+.*")) {
						System.out.println("Matched:" + token);
						tempArr = token.trim().split(" +");
						if (firstTokenFlag) {
							if (Character.isUpperCase(tempArr[0].charAt(0))) {
								str = tempArr[0].substring(1);
								for (char ch : str.toCharArray()) {
									if (Character.isLetter(ch)
											&& Character.isUpperCase(ch)) {
										boolFlag = true;
									}
								}
								if (!boolFlag) {
									// Converting to lower case
									tempArr[0] = tempArr[0].toLowerCase();
								}
							}
						}

					}
					finalToken = "";
					for (i = 0; i < tempArr.length; i++) {
						if (i == (tempArr.length - 1)) {
							finalToken += tempArr[i];
						} else {
							finalToken += tempArr[i] + " ";
						}
					}
					// finalToken = finalToken.trim();
					// if (stream.hasNext()) {
					stream.previous();
					stream.set(finalToken);
					stream.next();
					// } else {
					// stream.append(finalToken);
					// stream.seekEnd();
					// }
				}
			}
			stream.reset();
		}
	}
}
