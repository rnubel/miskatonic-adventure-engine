package org.github.rnubel.miskatonic.tests;

import static org.junit.Assert.*;

import android.graphics.BitmapFactory;


import org.github.rnubel.miskatonic.*;
import org.github.rnubel.miskatonic.astraal.*;
import org.junit.Test;
import junit.framework.*;
import org.xml.sax.InputSource;
import org.junit.Before;
import org.junit.Test;




public class AstraalResourceTest {
	@Test
	public void testShouldDynamicallyFindResourceID() {
		int id = ASTRAALResource.getDrawableID("star");
		System.out.println("0x" + Integer.toHexString(id));
		assertTrue(id == 0x7f020005);
	}
	
	@Test
	public void testShouldFailOnBogusResource() {
		int id = ASTRAALResource.getDrawableID("cthulhuawakesfromhisprison");
		assertTrue(id == 0);
	}
	
}
