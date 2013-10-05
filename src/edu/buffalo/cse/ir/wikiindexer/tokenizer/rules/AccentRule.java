package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.ACCENTS)
public class AccentRule implements TokenizerRule {

	/*Map<String, String> accentMap;
	String[][] accentList = { { "\u00C0", "A" }, { "\u00C1", "A" },
			{ "\u00C2", "A" }, { "\u00C3", "A" }, { "\u00C4", "A" },
			{ "\u00C5", "A" }, { "\u00C6", "AE" }, { "\u00C7", "C" },
			{ "\u00C8", "E" }, { "\u00C9", "E" }, { "\u00CA", "E" },
			{ "\u00CB", "E" }, { "\u00CC", "I" }, { "\u00CD", "I" },
			{ "\u00CE", "I" }, { "\u00CF", "I" }, { "\u0132", "IJ" },
			{ "\u00D0", "D" }, { "\u00D1", "N" }, { "\u00D2", "O" },
			{ "\u00D3", "O" }, { "\u00D4", "O" }, { "\u00D5", "O" },
			{ "\u00D6", "O" }, { "\u00D8", "O" }, { "\u0152", "OE" },
			{ "\u00DE", "TH" }, { "\u00D9", "U" }, { "\u00DA", "U" },
			{ "\u00DB", "U" }, { "\u00DC", "U" }, { "\u00DD", "Y" },
			{ "\u0178", "Y" }, { "\u00E0", "a" }, { "\u00E1", "a" },
			{ "\u00E2", "a" }, { "\u00E3", "a" }, { "\u00E4", "a" },
			{ "\u00E5", "a" }, { "\u00E6", "ae" }, { "\u00E7", "c" },
			{ "\u00E8", "e" }, { "\u00E9", "e" }, { "\u00EA", "e" },
			{ "\u00EB", "e" }, { "\u00EC", "i" }, { "\u00ED", "i" },
			{ "\u00EE", "i" }, { "\u00EF", "i" }, { "\u0133", "ij" },
			{ "\u00F0", "d" }, { "\u00F1", "n" }, { "\u00F2", "o" },
			{ "\u00F3", "o" }, { "\u00F4", "o" }, { "\u00F5", "o" },
			{ "\u00F6", "o" }, { "\u00F8", "o" }, { "\u0153", "oe" },
			{ "\u00DF", "ss" }, { "\u00FE", "th" }, { "\u00F9", "u" },
			{ "\u00FA", "u" }, { "\u00FB", "u" }, { "\u00FC", "u" },
			{ "\u00FD", "y" }, { "\u00FF", "y" }, { "\uFB00", "ff" },
			{ "\uFB01", "fi" }, { "\uFB02", "fl" }, { "\uFB03", "ffi" },
			{ "\uFB04", "ffl" }, { "\uFB05", "ft" }, { "\uFB06", "st" } };*/

	/*public AccentRule() {
		accentMap = new HashMap<String, String>();
		// Copying from Array to Map
		for (int i = 0; i < accentList.length; i++) {
			accentMap.put(accentList[i][0], accentList[i][1]);
		}
	}*/

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO : execute failing test case
		if (stream != null) {
			System.out.println("Incoming Stream in accent:"
					+ stream.getAllTokens());
			String token = "", result = "";
			stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				result = Normalizer.normalize(token, Normalizer.Form.NFD);
				Pattern pattern = Pattern
						.compile("\\p{InCombiningDiacriticalMarks}+");
				token = pattern.matcher(result).replaceAll("");
				stream.previous();
				stream.set(token);
				stream.next();
			}
			stream.reset();
			System.out.println("Outgoing Stream in accent:"
					+ stream.getAllTokens());
		}
	}
}
