package org.github.rnubel.miskatonic.aal;

/**
 * A statement that assigns some variable to the value of an expression.
 */
public class AALStatementAssignment extends AALStatement {
	private String mVarName;
	private AALExpression mExpression;

	public AALStatementAssignment(String varname, AALExpression expr) {
		mVarName = varname;
		mExpression = expr;
	}
	
	/**
	 * Evaluate the expression and store its result into varname.
	 */
	public void execute(AALExecutionState state) {
		state.setVariable(mVarName, mExpression.evaluate(state));
	}
	
	
	public boolean equals(Object _other) {
		AALStatementAssignment other = (AALStatementAssignment) _other;
		return mVarName.equals(other.mVarName) && mExpression.equals(other.mExpression);		
	}
	
	
}
