package com.games.test1.aal;

import com.games.test1.aal.AALExpression.Operator;

/** [Expr] (op) [Expr] */
public class AALExpressionOperation extends AALExpression {
	private AALExpression mLeft;
	private AALExpression mRight;
	private Operator mOperator;

	/** Create a new expression. */
	public AALExpressionOperation(AALExpression left, AALExpression right, Operator op) {
		mLeft = left;
		mRight = right;
		mOperator = op;
	}
	
	/** Combine our two operands. */
	public AALValue evaluate(AALExecutionState state) {		
		switch (mOperator) {
		case Plus:
			return mLeft.evaluate(state).plus(mRight.evaluate(state));
		case Minus:
			return mLeft.evaluate(state).minus(mRight.evaluate(state));
		case Divide:
			return mLeft.evaluate(state).divide(mRight.evaluate(state));
		case Times:
			return mLeft.evaluate(state).times(mRight.evaluate(state));
		case LessThan:
			return mLeft.evaluate(state).lessThan(mRight.evaluate(state));
		case GreaterThan:
			return mLeft.evaluate(state).greaterThan(mRight.evaluate(state));
		case EqualTo:
			return mLeft.evaluate(state).equalTo(mRight.evaluate(state));
			
		case And:
			return mLeft.evaluate(state).and(mRight.evaluate(state));
		case Or:
			return mLeft.evaluate(state).or(mRight.evaluate(state));
		case Not:
			return mLeft.evaluate(state).not(null);
			
		default:
			return mLeft.evaluate(state); // Failsafe.
		}		
	}
	
	
	public boolean equals(Object _other) {
		AALExpressionOperation other = (AALExpressionOperation) _other;
		return 	this.mLeft.equals(other.mLeft) && 
				this.mRight.equals(other.mRight) && 
				this.mOperator.equals(other.mOperator);
	}
}
