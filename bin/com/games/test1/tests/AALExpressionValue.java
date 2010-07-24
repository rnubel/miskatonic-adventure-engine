package com.games.test1.tests;

import com.games.test1.aal.*;

public class AALExpressionValue extends AALExpression {
	private AALValue mValue;

	public AALExpressionValue(AALValue v) {
		mValue = v;
	}

	public AALValue evaluate(AALExecutionState state) {
		return mValue;
	}
}
