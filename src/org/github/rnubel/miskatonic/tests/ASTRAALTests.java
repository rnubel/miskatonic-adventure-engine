package org.github.rnubel.miskatonic.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ASTRAALTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("ASTRAAL Tests");
		//$JUnit-BEGIN$		
		suite.addTestSuite(AstraalParserTest.class);
		suite.addTestSuite(AstraalResourceTest.class);		
		//$JUnit-END$
		return suite;
	}

}
