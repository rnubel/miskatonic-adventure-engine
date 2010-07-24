package com.games.test1.aal;

import java.util.Vector;
import java.util.regex.*;

/**
 * A lexer breaks up a string into a list of tokens, which are 
 * meaningful units of program syntax. AALLexer does exactly that.
 */
public class AALLexer {
	public Vector<AALToken> lex(String str) {
		Vector<AALToken> tokens = new Vector<AALToken>();
		
		Pattern tokPattern = Pattern.compile("(;|\\,|\\(|\\)|\\[|\\]|\\\".+?\\\"|[^ \\t\\n\\r;\\(\\)\\[\\]\\,]+)");		
		Matcher matcher = tokPattern.matcher(str);
		while (matcher.find()) {
			// System.out.println(matcher.group());
			tokens.add(new AALToken(matcher.group()));
		}
		
		return tokens;
	}
}
