package com.games.test1.aal;

/**
 * A basic while loop.
 */
public class AALStatementWhile extends AALStatement {
	private static final int MAX_ITERATIONS = 10000;
	private AALExpression mConditional;
	private AALStatement mStatement;

	public AALStatementWhile(AALExpression conditional, AALStatement statement) {
		mConditional = conditional;
		mStatement = statement;
	}
	
	/**
	 * Evaluate the conditional and execute the right statement.
	 */
	public void execute(AALExecutionState state) {
		/* Add a hard-coded limit, just in case. */
		int iterations = 0;		
		while ((Boolean)mConditional.evaluate(state).getValue() == true 
				&& iterations++ < MAX_ITERATIONS) {			
			mStatement.execute(state);			
		}
	}
	
	
	public boolean equals(AALStatementWhile other) {
		return mConditional.equals(other.mConditional) && mStatement.equals(other.mStatement);
	}
}
