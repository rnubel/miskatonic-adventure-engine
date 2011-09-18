package org.github.rnubel.miskatonic.aal;

public class AALInterpreter {
	private AALLexer mLexer;
	private AALParser mParser;

	public AALInterpreter() {
		mLexer = new AALLexer();		
	}
	
	public AALStatement interpret(String script) {		
		mParser = new AALParser(mLexer.lex(script));
		return mParser.parseProgram();
	}
}
