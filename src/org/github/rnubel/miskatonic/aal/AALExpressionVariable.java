package org.github.rnubel.miskatonic.aal;

public class AALExpressionVariable extends AALExpression {
	private String mVarName;

	public AALExpressionVariable(String varname) {
		mVarName = varname;
	}

	/** Use the state's symbol table. No scope for you! */
	public AALValue evaluate(AALExecutionState state) {
		return state.getVariable(mVarName);
	}
	
	
	public boolean equals(Object _other) {
		AALExpressionVariable other = (AALExpressionVariable) _other;
		return other.mVarName.equals(mVarName);
	}
}