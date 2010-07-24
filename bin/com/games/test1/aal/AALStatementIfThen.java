package com.games.test1.aal;

/**
 * A basic if-then-else conditional, with optional else block.
 */
public class AALStatementIfThen extends AALStatement {
	private AALExpression mConditional;
	private AALStatement mStatementIfTrue;
	private AALStatement mStatementIfFalse;

	public AALStatementIfThen(AALExpression conditional, AALStatement ifTrue, AALStatement ifFalse) {
		mConditional = conditional;
		mStatementIfTrue = ifTrue;
		mStatementIfFalse = ifFalse;
	}
	
	public AALStatementIfThen(AALExpression conditional, AALStatement ifTrue) {
		this(conditional, ifTrue, new AALStatement());
	}
	
	/**
	 * Evaluate the conditional and execute the right statement.
	 */
	public void execute(AALExecutionState state) {
		if ((Boolean)mConditional.evaluate(state).getValue() == true) {
			mStatementIfTrue.execute(state);
		} else {
			mStatementIfFalse.execute(state);
		}
	}
	
	
	public boolean equals(Object _other) {
		AALStatementIfThen other = (AALStatementIfThen) _other;
		return mConditional.equals(other.mConditional) &&
				mStatementIfTrue.equals(other.mStatementIfTrue) &&
				mStatementIfFalse.equals(other.mStatementIfFalse);
	}
}
