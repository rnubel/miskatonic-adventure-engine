package org.github.rnubel.miskatonic.aal;

/**
 * An expression which returns a value, and may cause side-effects
 * in the process.
 */
public class AALExpression {
	public enum Type {
		FunctionCall, /* Call function([Expr],[Expr],[Expr]) */
		Operation, /* [Expr] + [Expr] */		
		Variable, /* [Var] */
	}
	
	public enum Operator {
		Plus,
		Minus,
		Times,
		Divide,
		LessThan,
		EqualTo,
		GreaterThan,
		Or,
		And,
		Not
	}
	
	/** Evaluate this expression. */
	public AALValue evaluate(AALExecutionState state) {		
		return new AALValue();
	}
	
	
}
