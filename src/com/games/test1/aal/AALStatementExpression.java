package com.games.test1.aal;

/**
 * A statement that just evaluates an expression.
 */
public class AALStatementExpression extends AALStatement{
	private AALExpression mExpression;
	
	public AALStatementExpression(AALExpression expr) {
		mExpression = expr;
	}
	
	/** Just evaluate the expression. */
	public void execute(AALExecutionState state) {
		mExpression.evaluate(state);
	}
	
	
	public boolean equals(Object _other) {
		AALStatementExpression other = (AALStatementExpression) _other; 
		return mExpression.equals(other.mExpression);
	}
}
