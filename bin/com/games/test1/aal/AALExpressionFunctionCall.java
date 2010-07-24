package com.games.test1.aal;

/**
 * Call to a predefined function.
 */
public class AALExpressionFunctionCall extends AALExpression {
	private String mFunctionName;
	private AALExpression mArgs[];
	
	public AALExpressionFunctionCall(String fname, AALExpression args[]) {
		mFunctionName = fname;		
		mArgs = args;
	}
	
	public AALValue evaluate(AALExecutionState state) {
		/** Evaluate arguments. */
		AALValue argVals[] = new AALValue[mArgs.length];
		for (int i = 0; i < mArgs.length; i++) {
			argVals[i] = mArgs[i].evaluate(state);
		}
		return state.callFunction(mFunctionName, argVals);
	}
	
	
	public boolean equals(Object _other) {
		AALExpressionFunctionCall other = (AALExpressionFunctionCall) _other;
		boolean arrEq = true;
		for (int i = 0; i < Math.min(other.mArgs.length, mArgs.length); i++) {
			arrEq |= mArgs[i].equals(other.mArgs[i]);
		}
		return mFunctionName.equals(other.mFunctionName);
	}
}
