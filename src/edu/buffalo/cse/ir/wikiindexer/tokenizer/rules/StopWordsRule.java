package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.STOPWORDS)
public class StopWordsRule implements TokenizerRule {
	private List<String> stopWords;

	public StopWordsRule() {
		stopWords = new ArrayList<String>();
		Collections.addAll(stopWords, "a", "about", "above", "after", "again",
				"against", "all", "am", "an", "and", "any", "are", "aren't",
				"as", "at", "be", "because", "been", "before", "being",
				"below", "between", "both", "but", "by", "can't", "cannot",
				"could", "couldn't", "did", "didn't", "do", "does", "doesn't",
				"doing", "don't", "down", "during", "each", "few", "for",
				"from", "further", "had", "hadn't", "has", "hasn't", "have",
				"haven't", "having", "he", "he'd", "he'll", "he's", "her",
				"here", "here's", "hers", "herself", "him", "himself", "his",
				"how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in",
				"into", "is", "isn't", "it", "it's", "its", "itself", "let's",
				"me", "more", "most", "mustn't", "my", "myself", "no", "nor",
				"not", "of", "off", "on", "once", "only", "or", "other",
				"ought", "our", "ours", "ourselves", "out", "over", "own",
				"same", "shan't", "she", "she'd", "she'll", "she's", "should",
				"shouldn't", "so", "some", "such", "than", "that", "that's",
				"the", "their", "theirs", "them", "themselves", "then",
				"there", "there's", "these", "they", "they'd", "they'll",
				"they're", "they've", "this", "those", "through", "to", "too",
				"under", "until", "up", "very", "was", "wasn't", "we", "we'd",
				"we'll", "we're", "we've", "were", "weren't", "what", "what's",
				"when", "when's", "where", "where's", "which", "while", "who",
				"who's", "whom", "why", "why's", "with", "won't", "would",
				"wouldn't", "you", "you'd", "you'll", "you're", "you've",
				"your", "yours", "yourself", "yourselves");

	}

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
//			System.out.println("Incoming Stream in stopwords:" + stream.getAllTokens());
			String token;
			// TODO: Remove stream.previous();
			stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				if (!token.equals(null)) {
					
					String[] tempArr = token.split(" ");
					ArrayList<String> finalArr = new ArrayList<String>();
					for (int i = 0; i < tempArr.length; i++) {
						if (!stopWords.contains(tempArr[i].toLowerCase().trim())) {
							finalArr.add(tempArr[i]);
						}
					}
					String finalToken = "";
					ListIterator<String> it = finalArr.listIterator();
					while (it.hasNext()) {
						finalToken += it.next();
						finalToken += " ";
					}
					// To remove extra space at the end
					finalToken = finalToken.trim();
					if (finalToken == "") {
						stream.previous();
						stream.remove();
					} else {
						stream.previous();
						stream.set(finalToken);
						stream.next();
					}
				}
			}
			stream.reset();
//			System.out.println("Outgoing Stream in stopwords:" + stream.getAllTokens());
		}

	}
}
