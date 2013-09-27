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
			String[] tempArr;
			int i;
			boolean boolFlag = false;
			int nTokens = 0;
			stream.previous();
			// Accumulating all the tokens in the stream
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
			stream.reset();
			if (finalToken != null) {
				// Checking if First word in the token is of the form Abcdef, if
				// yes converting to lower case
				if (finalToken.matches(".*?[A-Za-z ]+.*")) {
					tempArr = finalToken.trim().split(" +");
					if (Character.isUpperCase(tempArr[0].charAt(0))) {
						str = tempArr[0].substring(1);
						for (char ch : str.toCharArray()) {
							if (Character.isLetter(ch)
									&& Character.isUpperCase(ch)) {
								boolFlag = true;
							}
						}
						if (!boolFlag) {
							//Converting to lower case
							tempArr[0] = tempArr[0].toLowerCase();
						}
					}
					//Populating the tokens in the stream
					//If number of token is 1, then setting the stream with single token
					if (nTokens == 1 && stream.hasNext()) {
						finalToken = "";
						for (i = 0; i < (tempArr.length - 1); i++) {
							finalToken += tempArr[i] + " ";
						}
						finalToken += tempArr[i];
						stream.set(finalToken);
						// If number of tokens are more than 1, then merging the
						// adjacent tokens if they are in camel case
					} else if (nTokens > 1) {
						for (i = 0; i < tempArr.length; i++) {
							if (i == tempArr.length - 1) {
								if (stream.hasNext()) {
									stream.set(tempArr[i]);
									stream.next();
								}
							} else {
								if (isCamelCase(tempArr[i])
										&& isCamelCase(tempArr[i + 1])) {
									//Checking if adjacent elements are camel cased
									if (stream.hasNext()) {
										stream.set(tempArr[i] + " "
												+ tempArr[i + 1]);
										stream.next();
										i++;
									} else {
										stream.append(tempArr[i] + " "
												+ tempArr[i + 1]);
									}
								} else {
									if (stream.hasNext()) {
										stream.set(tempArr[i]);
										stream.next();
									} else {
										stream.append(tempArr[i]);
									}
								}
							}
						}
						//Removing extra token place holders after the merge
						stream.previous();
						while(stream.hasNext()){
							stream.next();
							stream.remove();
						}
					}
				}
			}
			stream.reset();
		}
	}

	private boolean isCamelCase(String string) {
		if (string.matches("[a-zA-Z][a-z]*([A-Z0-9]+[a-z]*)+")) {
			return true;
		}
		return false;
	}

}
