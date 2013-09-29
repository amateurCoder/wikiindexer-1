package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.HashMap;
import java.util.Map;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.APOSTROPHE)
public class ApostropheRule implements TokenizerRule {

	Map<String, String> apostropheWithOmission;
	String[][] aposMapping = { { "aren't", "are not" }, { "can't", "cannot" },
			{ "couldn't", "could not" }, { "didn't", "did not" },
			{ "doesn't", "does not" }, { "don't", "do not" },
			{ "hadn't", "had not" }, { "hasn't", "has not" },
			{ "haven't", "have not" }, { "he'd", "he had" },
			{ "he'll", "he will" }, { "he's", "he is" }, { "I'd", "I had" },
			{ "I'll", "I will" }, { "I'm", "I am" }, { "I've", "I have" },
			{ "isn't", "is not" }, { "it's", "it is" }, { "let's", "let us" },
			{ "mightn't", "might not" }, { "mustn't", "must not" },
			{ "'em", "them" }, { "shan't", "shall not" },
			{ "Put 'em", "Put them" }, { "she'd", "she had" },
			{ "she'll", "she will" }, { "she's", "she is" },
			{ "shouldn't", "should not" }, { "should've", "should have" },
			{ "that's", "that is" }, { "there's", "there is" },
			{ "they'd", "they would" }, { "they'll", "they will" },
			{ "they're", "they are" }, { "they've", "they have" },
			{ "we'd", "we had" }, { "we're", "we are" },
			{ "we've", "we have" }, { "weren't", "were not" },
			{ "what'll", "what will" }, { "what're", "what are" },
			{ "what's", "what is" }, { "what've", "what have" },
			{ "where's", "where is" }, { "who'd", "who had" },
			{ "who'll", "who will" }, { "who're", "who are" },
			{ "who's", "who is" }, { "who've", "who have" },
			{ "won't", "will not" }, { "wouldn't", "would not" },
			{ "you'd", "you had" }, { "you'll", "you will" },
			{ "you're", "you are" }, { "you've", "you have" }

	};

	public ApostropheRule() {
		apostropheWithOmission = new HashMap<String, String>();
		// Copying from Array to Map
		for (int i = 0; i < aposMapping.length; i++) {
			apostropheWithOmission.put(aposMapping[i][0], aposMapping[i][1]);
		}
	}

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "", result = "", tempResult = "";
			stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				if (token != null) {
					if (token.matches(".*?'.*")) {
						String tempToken = token;
						result = apostropheWithOmission.get(token);
						if (result == null) {
							tempResult = apostropheWithOmission.get(tempToken
									.toLowerCase());
							if (tempResult != null) {
								tempResult = tempResult.substring(0, 1)
										.toUpperCase()
										+ tempResult.substring(1);
								result = tempResult;
							}
						}
						if (result == null || tempResult == null) {
							// Matched simple Apostrophe
							if (token.matches(".*?('s).*")) {
								token = token.replaceAll("('s)", "");
								stream.previous();
								stream.set(token);
								stream.next();
							} else if (token.matches(".*?(s'|')(.*?){0,}")) {
								token = token.replaceAll("'", "");
								stream.previous();
								stream.set(token);
								stream.next();
							}
						} else {
							// Matched contraction
							String[] tempArr = result.split(" ");
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
				}
			}
			stream.reset();
		}

	}
}
