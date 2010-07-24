package com.games.test1.aal;

import com.games.test1.*;

/**
 * A Statement is the fundamental execution unit of AAL. Blocks of 
 * other Statements can be comprised within a Statement of type Block.
 * To execute, it requires a State object.
 */
public class AALStatement {
	/** The various types of Statements. */
	public enum Type {		
		Assignment, /* Let var = [Expression]; */
		Expression, /* [Expression]; */
		Block, /* { [Statement]; [Statement]; [Statement]; } */
		IfThen, /* If <[Expression]> Then [Block] End */
		While, /* While <[Expression]> Do [Block] End */		
	}
	
	public AALStatement() {
		
	}
	
	public void execute(AALExecutionState state) { }
}
