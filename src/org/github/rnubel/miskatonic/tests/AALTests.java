package org.github.rnubel.miskatonic.tests;


import java.util.Vector;

import junit.framework.*;

import org.github.rnubel.miskatonic.*;
import org.github.rnubel.miskatonic.aal.*;
import org.junit.Before;
import org.junit.Test;


public class AALTests extends TestCase {	
	
	@Test
	public void testShouldCreateValuesRight() {
		AALValue a = new AALValue(4);
		AALValue b = new AALValue(3);
		
		assertTrue(a.getType().equals(AALValue.Type.Int));
	}
	
	@Test
	public void testShouldEvaluateAnExpressionAddingTwoIntegers() {
		AALExecutionState state = new AALExecutionState();
		AALValue a = new AALValue(4);
		AALValue b = new AALValue(3);				
		AALExpression e1 = new AALExpressionValue(a);
		AALExpression e2 = new AALExpressionValue(b);
		AALExpression e = new AALExpressionOperation(e1, e2, AALExpression.Operator.Plus);
		
		assertTrue((Integer)(e.evaluate(state).getValue()) == 7);
	}
	
	
	@Test
	public void testShouldEvaluateAnExpressionAddingTwoFloats() {
		AALExecutionState state = new AALExecutionState();
		AALValue a = new AALValue(4.0f);
		AALValue b = new AALValue(3.0f);				
		AALExpression e1 = new AALExpressionValue(a);
		AALExpression e2 = new AALExpressionValue(b);
		AALExpression e = new AALExpressionOperation(e1, e2, AALExpression.Operator.Plus);
		
		assertTrue((Float)(e.evaluate(state).getValue()) == 7.0f);
	}
	
	@Test
	public void testShouldEvaluateAnExpressionSubtractingTwoIntegers() {
		AALExecutionState state = new AALExecutionState();
		AALValue a = new AALValue(4);
		AALValue b = new AALValue(3);				
		AALExpression e1 = new AALExpressionValue(a);
		AALExpression e2 = new AALExpressionValue(b);
		AALExpression e = new AALExpressionOperation(e1, e2, AALExpression.Operator.Minus);
		
		assertTrue((Integer)(e.evaluate(state).getValue()) == 1);
		
		e = new AALExpressionOperation(e2, e1, AALExpression.Operator.Minus);
		
		assertTrue((Integer)(e.evaluate(state).getValue()) == -1);
	}
	
	@Test
	public void testShouldEvaluateAnExpressionAddingTwoStrings() {
		AALExecutionState state = new AALExecutionState();
		AALValue a = new AALValue("hello ");
		AALValue b = new AALValue("world");				
		AALExpression e1 = new AALExpressionValue(a);
		AALExpression e2 = new AALExpressionValue(b);
		AALExpression e = new AALExpressionOperation(e1, e2, AALExpression.Operator.Plus);
		
		assertTrue(((String)(e.evaluate(state).getValue())).equals("hello world"));		
	}	
	
	@Test
	public void testShouldEvaluateAVariableExpression() {
		AALExecutionState state = new AALExecutionState();
		state.setVariable("myvar", new AALValue(4));
		AALExpressionVariable e = new AALExpressionVariable("myvar");
		assertTrue(((Integer)e.evaluate(state).getValue()) == 4);
		
		state.setVariable("myvar", new AALValue("hello world"));
		assertTrue(((String)e.evaluate(state).getValue()).equals("hello world"));		
	}
	
	@Test
	public void testShouldEvaluateVariableExpressionInAdditionExpression() {
		AALExecutionState state = new AALExecutionState();
		state.setVariable("myvar", new AALValue(4));
		
		AALValue b = new AALValue(3);				
		AALExpression e1 = new AALExpressionVariable("myvar");
		AALExpression e2 = new AALExpressionValue(b);
		AALExpression e = new AALExpressionOperation(e1, e2, AALExpression.Operator.Plus);
		
		assertTrue((Integer)(e.evaluate(state).getValue()) == 7);		
	}
	
	@Test
	public void testShouldCallIncrementFunction() {
		AALExecutionState state = new AALExecutionState();
		state.setVariable("myvar", new AALValue(4));
		
		AALExpression ev = new AALExpressionVariable("myvar");
		AALExpression args[] = { new AALExpressionValue(new AALValue("myvar")) };
		AALExpression e = new AALExpressionFunctionCall("increment", args);
		e.evaluate(state);
		
		assertTrue((Integer)(ev.evaluate(state).getValue()) == 5);
	}
	
	@Test
	public void testShouldExecuteExpressionInStatement() {
		AALExecutionState state = new AALExecutionState();
		state.setVariable("myvar", new AALValue(4));
		
		// Build the statement.
		AALExpression args[] = { new AALExpressionValue(new AALValue("myvar")) };
		AALExpression e = new AALExpressionFunctionCall("increment", args);
		AALStatementExpression s = new AALStatementExpression(e);
		s.execute(state);
		
		// Test that it worked.
		AALExpression ev = new AALExpressionVariable("myvar");
		assertTrue((Integer)(ev.evaluate(state).getValue()) == 5);
	}
	
	@Test
	public void testShouldAssignVariableTwice() {
		AALExecutionState state = new AALExecutionState();
		AALStatementAssignment s1 = new AALStatementAssignment("var1", 
			new AALExpressionValue(new AALValue(4)));
		AALStatementAssignment s2 = new AALStatementAssignment("var2", 
				new AALExpressionValue(new AALValue(true)));
		
		s1.execute(state);
		s2.execute(state);
		
		assertTrue((Integer) state.getVariable("var1").getValue() == 4);
		assertTrue((Boolean) state.getVariable("var2").getValue() == true);
	}
	
	@Test
	public void testShouldExecuteBlock() {
		AALExecutionState state = new AALExecutionState();
		AALStatementAssignment s1 = new AALStatementAssignment("var1", 
			new AALExpressionValue(new AALValue(4)));
		AALStatementAssignment s2 = new AALStatementAssignment("var2", 
				new AALExpressionValue(new AALValue(true)));
		
		AALStatement statements[] = {s1, s2};
		AALStatementBlock block = new AALStatementBlock(statements);
		block.execute(state);
		
		assertTrue((Integer) state.getVariable("var1").getValue() == 4);
		assertTrue((Boolean) state.getVariable("var2").getValue() == true);
	}
	
	@Test
	public void testShouldHandleIfThenElseForTrue() {
		AALExecutionState state = new AALExecutionState();
		AALStatementAssignment s1 = new AALStatementAssignment("var1", 
				new AALExpressionValue(new AALValue(4)));
		AALStatementAssignment s2 = new AALStatementAssignment("var1", 
				new AALExpressionValue(new AALValue(2)));

		// Trivial conditional.
		AALExpressionValue conditional = new AALExpressionValue(new AALValue(true));
		
		AALStatementIfThen ift = new AALStatementIfThen(conditional, s1, s2);
		ift.execute(state);
		
		assertTrue((Integer)state.getVariable("var1").getValue() == 4);
	}
	
	@Test
	public void testShouldHandleIfThenElseForFalse() {
		AALExecutionState state = new AALExecutionState();
		AALStatementAssignment s1 = new AALStatementAssignment("var1", 
				new AALExpressionValue(new AALValue(4)));
		AALStatementAssignment s2 = new AALStatementAssignment("var1", 
				new AALExpressionValue(new AALValue(2)));

		// Trivial conditional.
		AALExpressionValue conditional = new AALExpressionValue(new AALValue(false));
		
		AALStatementIfThen ift = new AALStatementIfThen(conditional, s1, s2);
		ift.execute(state);
		
		assertTrue((Integer)state.getVariable("var1").getValue() == 2);
	}
	
	@Test
	public void testShouldHandleWhile() {
		AALExecutionState state = new AALExecutionState();
		
		AALExpressionVariable i = new AALExpressionVariable("i");
		AALExpressionValue max = new AALExpressionValue(new AALValue(5));
		
		state.setVariable("i", new AALValue(0)); //i = 0
		
		AALExpressionOperation check = new AALExpressionOperation(i, max, AALExpression.Operator.LessThan); // i < 4
		AALStatementAssignment incr = new AALStatementAssignment("i",
						new AALExpressionOperation(i, new AALExpressionValue(new AALValue(1)), AALExpression.Operator.Plus)
					); // i = i + 1
		
		
		AALStatementWhile loop = new AALStatementWhile(check, incr);
		
		loop.execute(state);
		
		assertTrue((Integer)state.getVariable("i").getValue() == 5);
	}
	
	@Test
	public void testShouldLexBasic() {
		AALLexer lex = new AALLexer();
		Vector<AALToken> toks = lex.lex("test; string;");
		assertTrue(toks.size() == 4);
		
		assertTrue(toks.get(0).getType() == AALToken.Type.Identifier);
		assertTrue(toks.get(1).getType() == AALToken.Type.Semicolon);
		assertTrue(toks.get(2).getType() == AALToken.Type.Identifier);
		assertTrue(toks.get(3).getType() == AALToken.Type.Semicolon);
	}
	

	@Test
	public void testShouldLexNumbers() {
		AALLexer lex = new AALLexer();
		Vector<AALToken> toks = lex.lex("10 10.5 20 0.5");
		assertTrue(toks.size() == 4);
		
		assertTrue(toks.get(0).getType() == AALToken.Type.IntLiteral);
		assertTrue(((Integer)toks.get(0).getValue().getValue()).equals(10));
		assertTrue(toks.get(1).getType() == AALToken.Type.FloatLiteral);
		assertTrue(((Float)toks.get(1).getValue().getValue()).equals(10.5f));
		assertTrue(toks.get(2).getType() == AALToken.Type.IntLiteral);
		assertTrue(((Integer)toks.get(2).getValue().getValue()).equals(20));
		assertTrue(toks.get(3).getType() == AALToken.Type.FloatLiteral);
		assertTrue(((Float)toks.get(3).getValue().getValue()).equals(0.5f));
	}
	
	@Test
	public void testShouldLexStringsAndBools() {
		AALLexer lex = new AALLexer();
		Vector<AALToken> toks = lex.lex("\"word\" True \"word2\" False");
		assertTrue(toks.size() == 4);
		
		assertTrue(toks.get(0).getType() == AALToken.Type.StringLiteral);		
		assertTrue(((String)toks.get(0).getValue().getValue()).equals("word"));
		assertTrue(toks.get(1).getType() == AALToken.Type.BoolLiteral);
		assertTrue(((Boolean)toks.get(1).getValue().getValue()).equals(true));
		assertTrue(toks.get(2).getType() == AALToken.Type.StringLiteral);
		assertTrue(((String)toks.get(2).getValue().getValue()).equals("word2"));
		assertTrue(toks.get(3).getType() == AALToken.Type.BoolLiteral);
		assertTrue(((Boolean)toks.get(3).getValue().getValue()).equals(false));
	}
	
	@Test
	public void testShouldLexComplexString() {
		AALLexer lex = new AALLexer();
		Vector<AALToken> toks = lex.lex("\"asdf<asdf\"");
		assertTrue(toks.size() == 1);
		
		assertTrue(toks.get(0).getType() == AALToken.Type.StringLiteral);		
		assertTrue(((String)toks.get(0).getValue().getValue()).equals("asdf<asdf"));
	}
	
	@Test
	public void testShouldLexKeywords() {
		AALLexer lex = new AALLexer();
		Vector<AALToken> toks = lex.lex("Let If Then Else End While Do End Call ;()[]");
				
		AALToken.Type types[] = { 
				AALToken.Type.Let,
				AALToken.Type.If,
				AALToken.Type.Then,
				AALToken.Type.Else,
				AALToken.Type.End,
				AALToken.Type.While,
				AALToken.Type.Do,
				AALToken.Type.End,
				AALToken.Type.Call,
				AALToken.Type.Semicolon,
				AALToken.Type.LeftParen,
				AALToken.Type.RightParen,
				AALToken.Type.LeftSquare,
				AALToken.Type.RightSquare,
		};
		
		assertTrue(toks.size() == types.length);
		
		for (int i = 0; i < types.length; i++) {
			assertTrue(toks.get(i).getType() == types[i]);			
		}
	}
	
	@Test
	public void testShouldLexAcrossLines() {
		AALLexer lex = new AALLexer();
		Vector<AALToken> toks = lex.lex("" +
				"If [x > 4] Then\n" +
				"Let x = x - 1;\n" +
				"End");
				
		AALToken.Type types[] = { 
				AALToken.Type.If,
				AALToken.Type.LeftSquare,
				AALToken.Type.Identifier,
				AALToken.Type.GreaterThan,
				AALToken.Type.IntLiteral,
				AALToken.Type.RightSquare,
				AALToken.Type.Then,
				AALToken.Type.Let,
				AALToken.Type.Identifier,
				AALToken.Type.Assign,
				AALToken.Type.Identifier,
				AALToken.Type.Minus,
				AALToken.Type.IntLiteral,
				AALToken.Type.Semicolon,
				AALToken.Type.End,
		};
		
		assertTrue(toks.size() == types.length);
		
		for (int i = 0; i < types.length; i++) {
			assertTrue(toks.get(i).getType() == types[i]);			
		}
	}
	
	
	/// PARSER TESTS ///
	
	@Test
	public void testShouldParseEmptyTokenList() {
		AALToken toks[] = { };
		AALParser parser = new AALParser(toks);
		parser.parseProgram();
		assertTrue(true);
	}
	
	@Test
	public void testShouldParseIdentifierExpression() {
		Vector<AALToken> toks = (new AALLexer()).lex("myvar");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementExpression(new AALExpressionVariable("myvar"))
		));
	}
	
	@Test
	public void testShouldParseIfThenWithIDExps() {
		Vector<AALToken> toks = (new AALLexer()).lex("If [myvar] Then myvar2 Else myvar3 End");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementIfThen(
				new AALExpressionVariable("myvar"),
				new AALStatementExpression(new AALExpressionVariable("myvar2")),
				new AALStatementExpression(new AALExpressionVariable("myvar3"))
			)		
		));
		
	}
	
	@Test
	public void testShouldParseAssignmentExpression() {
		Vector<AALToken> toks = (new AALLexer()).lex("Let myvar = othervar;");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementAssignment("myvar", new AALExpressionVariable("othervar"))			
		));
	}
	
	@Test
	public void testShouldParseAndExecuteAssignmentIfThen() {		
		Vector<AALToken> toks = (new AALLexer()).lex("If [myvar] Then Let x = a; Else Let x = b; End");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementIfThen(
				new AALExpressionVariable("myvar"),
				new AALStatementAssignment("x",new AALExpressionVariable("a")),
				new AALStatementAssignment("x",new AALExpressionVariable("b"))
			)		
		));		
		
		// Now test it...
		AALExecutionState state = new AALExecutionState();
		state.setVariable("myvar", new AALValue(false));
		state.setVariable("a", new AALValue(1));
		state.setVariable("b", new AALValue(2));
		b.execute(state);
		
		assertTrue((Integer) state.getVariable("x").getValue() == 2);
	}
	
	@Test
	public void testShouldParseSimpleOperation() {
		Vector<AALToken> toks = (new AALLexer()).lex("x + y");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementExpression(new AALExpressionOperation(
					new AALExpressionVariable("x"), 
					new AALExpressionVariable("y"),
					AALExpression.Operator.Plus)
			))			
		);
	}
	

	@Test
	public void testShouldParseNestedOperation() {
		Vector<AALToken> toks = (new AALLexer()).lex("x + [y + z]");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementExpression(new AALExpressionOperation(
					new AALExpressionVariable("x"), 
					new AALExpressionOperation(
						new AALExpressionVariable("y"),
						new AALExpressionVariable("z"),
						AALExpression.Operator.Plus
					),
					AALExpression.Operator.Plus)
			))			
		);
	}
	
	
	@Test
	public void testShouldParseLeftNestedOperation() {
		Vector<AALToken> toks = (new AALLexer()).lex("[x + y] + z");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementExpression(new AALExpressionOperation(
					new AALExpressionOperation(
						new AALExpressionVariable("x"),
						new AALExpressionVariable("y"),
						AALExpression.Operator.Plus
					),
					new AALExpressionVariable("z"),
					AALExpression.Operator.Plus)
			))			
		);
	}
	
	@Test
	public void testShouldParseIntLiteral() {
		Vector<AALToken> toks = (new AALLexer()).lex("4");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementExpression(new AALExpressionValue(new AALValue(4)))			
		));
	}
	
	@Test
	public void testShouldParseFloatLiteral() {
		Vector<AALToken> toks = (new AALLexer()).lex("6.66");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementExpression(new AALExpressionValue(new AALValue(6.66f)))			
		));
	}
	
	@Test
	public void testShouldParseBoolLiteral() {
		Vector<AALToken> toks = (new AALLexer()).lex("False");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementExpression(new AALExpressionValue(new AALValue(false)))			
		));
	}
	
	@Test
	public void testShouldParseStringLiteral() {
		Vector<AALToken> toks = (new AALLexer()).lex("\"asdf<asdf\"");		
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementExpression(new AALExpressionValue(new AALValue("asdf<asdf")))			
		));
	}
	
	@Test
	public void testShouldParseAssignIntLiteral() {
		Vector<AALToken> toks = (new AALLexer()).lex("Let myvar = 4;");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementAssignment("myvar", new AALExpressionValue(new AALValue(4)))			
		));		
	}
		
	@Test
	public void testShouldParseAssignStringLiteral() {
		Vector<AALToken> toks = (new AALLexer()).lex("Let myvar = \"hello world\";");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementAssignment("myvar", new AALExpressionValue(new AALValue("hello world")))			
		));		
	}
	
	@Test
	public void testShouldParseAndExecuteAssignOperation() {
		Vector<AALToken> toks = (new AALLexer()).lex("Let myvar = 4 + 2;");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram().getStatements()[0];
		assertTrue(b.equals(
			new AALStatementAssignment("myvar", 
				new AALExpressionOperation(
					new AALExpressionValue(new AALValue(4)),
					new AALExpressionValue(new AALValue(2)),	
					AALExpression.Operator.Plus
				)
			)			
		));		
		
		AALExecutionState state = new AALExecutionState();
		b.execute(state);
		
		assertTrue((Integer) state.getVariable("myvar").getValue() == 6);
		
		// Test minus, too.
		toks = (new AALLexer()).lex("Let myvar = 4 - 2;");
		parser = new AALParser(toks);
		parser.parseProgram().execute(state);
		assertTrue((Integer) state.getVariable("myvar").getValue() == 2);
	}
	
	@Test
	public void testShouldParseAndExecuteDivideOperation() {
		Vector<AALToken> toks = (new AALLexer()).lex("Let r = 4 / 3;");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		AALExecutionState state = new AALExecutionState();
		b.execute(state);
		
		assertTrue((Integer) state.getVariable("r").getValue() == 1);		
	}
	
	@Test
	public void testShouldParseAndExecuteFloatDivideOperation() {
		Vector<AALToken> toks = (new AALLexer()).lex("Let r = 4.0 / 3.0;");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		AALExecutionState state = new AALExecutionState();
		b.execute(state);
		
		assertTrue((Float) state.getVariable("r").getValue() == 4.0f/3.0f);		
	}
	
	@Test
	public void testShouldParseAndExecuteFloatMultiplyOperation() {
		Vector<AALToken> toks = (new AALLexer()).lex("Let r = 4.0 * 3.0;");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		AALExecutionState state = new AALExecutionState();
		b.execute(state);
		
		assertTrue((Float) state.getVariable("r").getValue() == 12.0f);		
	}
	
	@Test
	public void testShouldParseComplexIfThen() {
		Vector<AALToken> toks = (new AALLexer()).lex("" +
				"Let a = 4;" + "\n" +
				"Let test = a > 5; \n" +
				"If [a > 5] Then" + "\n" + 
				"	Let x = 2;" + "\n" + 
				"Else" + "\n" +
				"	Let x = 0;" + "\n" +
				"End");
		AALParser parser = new AALParser(toks);
		AALStatementBlock b = new AALStatementBlock(parser.parseProgram().getStatements());
		
		AALExecutionState state = new AALExecutionState();
		b.execute(state);
		
		assertTrue((Boolean)state.getVariable("test").getValue() == false);
		assertTrue((Integer)state.getVariable("x").getValue() == 0);
		
	}
	
	@Test
	public void testShouldParseAndExecuteWhile() {
		Vector<AALToken> toks = (new AALLexer()).lex("" +
				"Let i = 0;" + "\n" +				
				"While [i < 5] Do" + "\n" + 
				"	Let i = i + 1;" + "\n" + 
				"End");
		AALParser parser = new AALParser(toks);
		AALStatementBlock b = new AALStatementBlock(parser.parseProgram().getStatements());
		
		AALExecutionState state = new AALExecutionState();		
		b.execute(state);
		
		assertTrue((Integer)state.getVariable("i").getValue() == 5);
	}
	
	@Test
	public void testShouldThrowParsingError() {
		Vector<AALToken> toks = (new AALLexer()).lex("" +
				"Let i = 0;" + "\n" +				
				"Wherg [i < 5] Do" + "\n" + 
				"	Let i = i + 1;" + "\n" + 
				"End");
		AALParser parser = new AALParser(toks);
		AALStatementBlock b = parser.parseProgram();
		assertTrue(b == null);
	}
	
	@Test
	public void testShouldHandleBlocks() {
		Vector<AALToken> toks = (new AALLexer()).lex("" +
				"Let i = 0;" + "\n" +
				"Let j = 0;" + "\n" +
				"While [i < 5] Do" + "\n" + 
				"	Let i = i + 1;" + "\n" +
				"	Let j = j + 1;" + "\n" + 
				"End");
		AALParser parser = new AALParser(toks);
		AALStatementBlock b = parser.parseProgram();
		
		AALExecutionState state = new AALExecutionState();		
		b.execute(state);
		
		assertTrue((Integer)state.getVariable("i").getValue() == 5);
		assertTrue((Integer)state.getVariable("j").getValue() == 5);
	}
	
	@Test
	public void testShouldHandleNestedBlocks() {
		Vector<AALToken> toks = (new AALLexer()).lex("" +
				"Let i = 0;" + "\n" +
				"Let j = 0;" + "\n" +
				"While [i < 5] Do" + "\n" + 
				"	Let i = i + 1;" + "\n" +
				"	If [i == 4] Then" + "\n" +
				"		Let j = j + 1;" + "\n" +
				"	End" + "\n" +
				"End");
		AALParser parser = new AALParser(toks);
		AALStatementBlock b = parser.parseProgram();
		
		AALExecutionState state = new AALExecutionState();		
		b.execute(state);
		
		assertTrue((Integer)state.getVariable("i").getValue() == 5);
		assertTrue((Integer)state.getVariable("j").getValue() == 1);
	}
	
	@Test
	public void testShouldParseFunctionCallWithNoArgs() {
		Vector<AALToken> toks = (new AALLexer()).lex("Call myfun()");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		
		AALExpression args[] = {};
		
		assertTrue(b.equals(
			new AALStatementExpression(
				new AALExpressionFunctionCall("myfun", args)
			)	
		));			
	}
	
	@Test
	public void testShouldParseFunctionCallWithOneArg() {
		Vector<AALToken> toks = (new AALLexer()).lex("Call myfun(\"woo\")");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		
		AALExpression args[] = { new AALExpressionValue(new AALValue("woo"))};
		
		assertTrue(b.equals(
			new AALStatementExpression(
				new AALExpressionFunctionCall("myfun", args)
			)			
		));			
	}
	
	@Test
	public void testShouldParseFunctionCallWithOneArgAndIgnoreWhitespace() {
		Vector<AALToken> toks = (new AALLexer()).lex("\n\n\n\n\t\tCall myfun(\"woo\")");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		
		AALExpression args[] = { new AALExpressionValue(new AALValue("woo"))};
		
		assertTrue(b.equals(
			new AALStatementExpression(
				new AALExpressionFunctionCall("myfun", args)
			)			
		));			
	}
	
	@Test
	public void testShouldParseFunctionCallWithTwoArgs() {
		Vector<AALToken> toks = (new AALLexer()).lex("Call myfun(\"woo\", 4)");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		
		AALExpression args[] = { new AALExpressionValue(new AALValue("woo")), 
				new AALExpressionValue(new AALValue(4))};
		
		assertTrue(b.equals(
			new AALStatementExpression(
				new AALExpressionFunctionCall("myfun", args)
			)			
		));			
	}
	

	
	@Test
	public void testShouldParseFunctionCallWithAComplexArg() {
		Vector<AALToken> toks = (new AALLexer()).lex("Call myfun(\"woo\", [4 + 2] - 3)");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		
		AALExpression args[] = { new AALExpressionValue(
				new AALValue("woo")), 
				new AALExpressionOperation(
					new AALExpressionOperation(
						new AALExpressionValue(new AALValue(4)),
						new AALExpressionValue(new AALValue(2)),
						AALExpression.Operator.Plus
					),
					new AALExpressionValue(new AALValue(3)),
					AALExpression.Operator.Minus				
				)};
		
		assertTrue(b.equals(
			new AALStatementExpression(
				new AALExpressionFunctionCall("myfun", args)
			)			
		));			
	}
	
	@Test
	public void testShouldParseFunctionCallWithThreeArgs() {
		Vector<AALToken> toks = (new AALLexer()).lex("Call myfun(\"woo\", 4, 2)");
		AALParser parser = new AALParser(toks);
		AALStatement b = parser.parseProgram();
		
		AALExpression args[] = { new AALExpressionValue(new AALValue("woo")), 
				new AALExpressionValue(new AALValue(4)),
				new AALExpressionValue(new AALValue(2))};
		
		assertTrue(b.equals(
			new AALStatementExpression(
				new AALExpressionFunctionCall("myfun", args)
			)			
		));			
	}
	
	@Test
	public void testIntegrationOne() {
		String script = "Let curx = Call getObjectX(\"jar\");" +
				"Let cury = Call getObjectY(\"jar\");" +	
				"Call moveObject(\"jar\", curx + 5, cury + 5);";
		
		AALInterpreter i = new AALInterpreter();
		AALStatement s = i.interpret(script);
		assertTrue(((AALStatementBlock)s).getStatements().length == 3);
	}
}

