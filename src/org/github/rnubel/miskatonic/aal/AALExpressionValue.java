package org.github.rnubel.miskatonic.aal;

import org.github.rnubel.miskatonic.aal.*;

public class AALExpressionValue extends AALExpression {
	private AALValue mValue;

	public AALExpressionValue(AALValue v) {
		mValue = v;
	}

	public AALValue evaluate(AALExecutionState state) {
		return mValue;
	}
	
	
	
	public boolean equals(Object _other) {
		AALExpressionValue other = (AALExpressionValue) _other;
		return mValue.equals(other.mValue);
	}
}
