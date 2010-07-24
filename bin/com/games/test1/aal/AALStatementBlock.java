package com.games.test1.aal;

/**
 * A list of statements.
 */
public class AALStatementBlock extends AALStatement {
	private AALStatement[] mStatements;

	public AALStatementBlock(AALStatement[] statements) {
		mStatements = statements;
	}
	
	public void execute(AALExecutionState state) {
		for (int i = 0; i < mStatements.length; i++) {
			mStatements[i].execute(state);
		}
	}
	
	
	public boolean equals(Object _other) {
		// Add a check such that a wrapping statement block is ignored around a single statement.
		if ( (_other.getClass() == AALStatementExpression.class 
				|| _other.getClass() == AALStatementIfThen.class
				|| _other.getClass() == AALStatementWhile.class
				|| _other.getClass() == AALStatementAssignment.class
				)
				&& mStatements.length == 1) {
			AALStatement other = (AALStatement) _other;
			return mStatements[0].equals(other);
		} else {
			AALStatementBlock other = (AALStatementBlock) _other;
			return mStatements.equals(other.mStatements);
		}
	}

	public AALStatement[] getStatements() {
		return mStatements;
	}
}
