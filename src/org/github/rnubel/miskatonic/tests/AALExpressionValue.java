package org.github.rnubel.miskatonic.tests;

import org.github.rnubel.miskatonic.aal.*;

public class AALExpressionValue extends AALExpression {
	private AALValue mValue;

	public AALExpressionValue(AALValue v) {
		mValue = v;
	}

	public AALValue evaluate(AALExecutionState state) {
		return mValue;
	}
}
