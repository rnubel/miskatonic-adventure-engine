package com.games.test1.aal;

import java.text.ParseException;
import java.util.Vector;

/**
 * A parser takes a series of tokens as input and parses the tokens
 * into an AST. AALParser is a simplistic, top-down parser.
 *
 */
public class AALParser {
	private Vector<AALToken> toks;
	
	/** Constructor for easier testing. */
	public AALParser(AALToken tokArr[]) {
		toks = new Vector<AALToken>();
		for (int i = 0; i < tokArr.length; i++) {
			toks.add(tokArr[i]);
		}
	}

	public AALParser(Vector<AALToken> toks2) {
		toks = toks2;		
	}

	/** Parses all tokens until consumed or failed. 
	 * @return */
	public AALStatementBlock parseProgram() {
		return parseStatement();
	}
	
	public void throwParsingError() { throwParsingError(""); }
	public void throwParsingError(String tag) {
		System.err.print("ERROR " + tag + ": Invalid syntax near (probably before) tokens ");
		for (int i = 0; i < Math.min(5, toks.size()); i++) {
			System.err.print(toks.get(i).getToken() + "  ");
		}
		System.err.println();
	}

	/** Parse what should be a statement. */
	public AALStatementBlock parseStatement() {
		Vector<AALStatement> statements = new Vector<AALStatement>();
		
		int toknum = -1;
		while (hasMoreTokens()) {
			toknum = toks.size();
			
			AALStatement stmt;
			AALToken.Type nextType = peekType();
			if ( nextType == AALToken.Type.Else  
			   || nextType == AALToken.Type.End
			   || nextType == AALToken.Type.EOF) {
				// End of block; go back.
				break;
			} else if (nextType == AALToken.Type.If) { // If statement
				stmt = parseIfThen();
			} else if (nextType == AALToken.Type.Let) {
				stmt = parseAssignment();
			} else if (nextType == AALToken.Type.While) {
				stmt = parseWhile();			
			} else {	   
				// The fail case is probably an expression, so handle that.
				stmt = new AALStatementExpression(parseExpression());
			}
			
			// Add the newly-parsed statement.
			statements.add(stmt);
				
			if (toknum == toks.size()) {
				throwParsingError();
				return null;
			}
		}		
		
		AALStatement[] statementArr = new AALStatement[statements.size()];
		statements.toArray(statementArr);
		return new AALStatementBlock(statementArr);
	}
		
	
	/** Parse what should be an if-then statement. */
	public AALStatement parseIfThen() {
	   // If EXPR Then STATEMENT Else STATEMENT End
	   // If EXPR Then STATEMENT End
	   consume(AALToken.Type.If);
	   // Let Expression handle the left [, as it's equipped to do so.
	   // There at least needs to be a right ], or this won't work.
	   AALExpression conditional = parseExpression();
	   consume(AALToken.Type.Then);
	   AALStatement ifTrue = parseStatement();
	   
	   // Do we have an else block? 
	   if (peekType() == AALToken.Type.Else) {
		   consume(AALToken.Type.Else);
		   AALStatement ifFalse = parseStatement();
		   consume(AALToken.Type.End);
		   
		   return new AALStatementIfThen(conditional, ifTrue, ifFalse);
	   } else {
		   consume(AALToken.Type.End);
		   return new AALStatementIfThen(conditional, ifTrue);
	   }
	}
	
	/** Parse what should be a while statement. */
	public AALStatement parseWhile() {
	   // While EXPR Do STATEMENT End
	   consume(AALToken.Type.While);
	   // Parse the conditional expression.
	   AALExpression conditional = parseExpression();
	   consume(AALToken.Type.Do);
	   AALStatement loopStmt = parseStatement();
	   // Consume the End to signify the end of this while.
	   consume(AALToken.Type.End);
	   
	   return new AALStatementWhile(conditional, loopStmt);	   
	}
	
	/** Parse what should be an assignment statement. */
	public AALStatementAssignment parseAssignment() {
		consume(AALToken.Type.Let);
		String varName = (String) consume(AALToken.Type.Identifier).getValue().getValue();
		consume(AALToken.Type.Assign);
		AALExpression rhs = parseExpression();
		consume(AALToken.Type.Semicolon);
		
		return new AALStatementAssignment(varName, rhs);
	}

	/** Parse what should be an expression. */
	public AALExpression parseExpression() { return parseExpression(false); }
	
	/**
	 * Parse what should be an expression.
	 * @param isSubExpression -- whether or not this expression needs to consume a closing ].
	 */
	public AALExpression parseExpression(boolean isSubExpression) {
		AALExpression expr = null;
		if (peekType() == AALToken.Type.Identifier) {
			// VARIABLE
			expr = parseVariable();
		} else if (peek().isLiteral()) {
			// LITERAL
			expr = parseLiteral();  
		} else if (peekType() == AALToken.Type.Call) {
			// FUNCTION CALL
			expr = parseFunctionCall();
		} else if (peekType() == AALToken.Type.LeftSquare) {
			// SUB-EXPRESSION
			consume(AALToken.Type.LeftSquare);
			expr = parseExpression(true);
		}

		// If the next token is an operator, parse an
		// operation expression.
		while (peek().isOperator()) {
			expr = parseOperation(expr);
		}

	   // Consume a right ] or a semicolon, if available.
	   if (isSubExpression) {
		   consume(AALToken.Type.RightSquare);
	   }
	   consume(AALToken.Type.Semicolon);
	   
	   return expr;
	}

	public AALExpressionFunctionCall parseFunctionCall() {
		// First, get the function name.
		consume(AALToken.Type.Call);
		String funcName = (String) consume().getValue().getValue();
		
		// Now, get a list of arguments.
		consume(AALToken.Type.LeftParen);
		Vector<AALExpression> argVec = new Vector<AALExpression>(); 
		// We have a while loop, so use the typical error apparatus.
		int toknum = -1;
		while (hasMoreTokens()) {
			toknum = toks.size();
		
			if (peekType() == AALToken.Type.RightParen) {
				consume(AALToken.Type.RightParen);
				break; // Done adding arguments.				
			}
			
			argVec.add(parseExpression());			
			
			if (peekType() != AALToken.Type.Comma && peekType() != AALToken.Type.RightParen) {
				throwParsingError("[argument list malformed for " + funcName + "]");
				break;
			} else {
				// Optional consume:
				consume(AALToken.Type.Comma); // And on to the next argument. 
			}
			
			// Progress check.
			if (toknum == toks.size()) {
				throwParsingError("[argument list seriously malformed for " + funcName + "]");
				break;
			}
		}
				
		AALExpression args[] = new AALExpression[argVec.size()];
		argVec.toArray(args);		
		
		return new AALExpressionFunctionCall(funcName, args);
	}
	
	/** Parse a value literal into a value. */
	public AALExpressionValue parseLiteral() {
		return new AALExpressionValue(consume().getValue());
	}
	
	
	/** Parse a variable expression. */
	public AALExpressionVariable parseVariable() {
		// The token has the variable name, so just get its value.
		// (Note: Martin Fowler be all hatin' on this call-chain)
		String varname = (String) consume(AALToken.Type.Identifier).getValue().getValue();
		
		return new AALExpressionVariable(varname);
	}
	
	
	/**
	 * Parse what should be an operation expression.
	 * Note: this is right-associative... for some reason.
	 */
	public AALExpression parseOperation(AALExpression lhs) {
	   AALExpression.Operator op = getOperatorFromToken(consume());
	   AALExpression rhs = parseExpression();
	   
	   return new AALExpressionOperation(lhs, rhs, op);
	}
	
	
	/** Return true if toks is not empty. */
	private boolean hasMoreTokens() {
		return toks.size() > 0;
	}
	
	/** Peek at the next token. */
	private AALToken peek() {
		if (!hasMoreTokens()) {
			return new AALToken(AALToken.Type.EOF);
		} else {
			return toks.firstElement();
		}
	}
	
	private AALToken.Type peekType() {
		return peek().getType();
	}
	
	/** Peek ahead at the token list, by the given amount. 0 is the same as peek(). */
	private AALToken peek(int ahead) {
		return toks.get(ahead);
	}
	
	private AALToken.Type peekType(int ahead) {
		return peek(ahead).getType();
	}
	
	/** Consume and return the next token. */
	private AALToken consume() {
		AALToken temp = peek();
		toks.removeElementAt(0);
		return temp;
	}
	
	/** Consume only if the next token is of the given type. */
	private AALToken consume(AALToken.Type type) {
		AALToken temp = peek();
		if (temp.getType() == type) {
			toks.removeElementAt(0);
			return temp;
		} else return null;
	}

	
	/** Convert a token operator into an AALExpression.Operator. */
	private AALExpression.Operator getOperatorFromToken(AALToken tok) {
		switch (tok.getType()) {
		case Plus:
			return AALExpression.Operator.Plus;
		case Minus:
			return AALExpression.Operator.Minus;
		case Times:
			return AALExpression.Operator.Times;
		case Divide:
			return AALExpression.Operator.Divide;
		case And:
			return AALExpression.Operator.And;
		case Or:
			return AALExpression.Operator.Or;
		case LessThan:
			return AALExpression.Operator.LessThan;
		case Equals:
			return AALExpression.Operator.EqualTo;
		case GreaterThan:
			return AALExpression.Operator.GreaterThan;			
		}
		
		// If we failed, return null.
		return null;		
	}
}