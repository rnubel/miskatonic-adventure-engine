package org.github.rnubel.miskatonic.tests;

import java.util.Vector;

import junit.framework.*;
import org.xml.sax.InputSource;
import org.github.rnubel.miskatonic.*;
import org.github.rnubel.miskatonic.astraal.*;
import org.junit.Before;
import org.junit.Test;

import android.R;


public class AstraalParserTest extends TestCase {
	private ASTRAALParser parser;
	private ASTRAALRoot rt;
	
	public void setUp() {
		parser = new ASTRAALParser();
		rt = parser.parse(new InputSource("assets/astraal_basic_test.xml"));	
	}
	
	@Test
	public void testShouldParseWithoutError() {
		setUp();
		assertTrue(true);
	}
	
	@Test
	public void testShouldParseVersionAndGameName() {		
		setUp();
		assertTrue(rt.getGameName().equals("Test Game"));
		assertTrue(rt.getVersion().equals("0.1"));
	}
	
	@Test
	public void testShouldLoadResourceBlocks() {
		setUp();
		Vector<ASTRAALResourceBlock> b = rt.getResourceBlocks();
		assertTrue(b.size() == 2);
		assertTrue(b.get(0).getID().equals("main"));
		assertTrue(b.get(1).getID().equals("set_two"));
	}
	
	
	@Test
	public void testShouldLoadNavigationCues() {
		setUp();
		Vector<ASTRAALNavigationCue> cues = rt.getFirstScene().getNavigationCues();
		assertTrue(cues.size() == 1);
		assertTrue(cues.get(0).getPosition().equals("right"));
		assertTrue(cues.get(0).getNewSceneID().equals("lab2"));
	}
	
	@Test
	public void testShouldLoadRightNumberOfAnimations() {
		setUp();
		Vector<ASTRAALResourceBlock> b = rt.getResourceBlocks();
		Vector<ASTRAALResource> sprites0 = b.get(0).getResources();
		Vector<ASTRAALResource> sprites1 = b.get(1).getResources();
		assertTrue(sprites0.size() == 3);
		assertTrue(sprites1.size() == 0);
		
	}
	
	@Test
	public void testShouldLoadAnimationsByID() {
		setUp();
		ASTRAALResourceBlock block = rt.getResourceBlockByID("main");
		assertTrue(block.getResourceByID("spr1") != null);
		assertTrue(block.getResourceByID("spr2") != null);
		assertTrue(block.getResourceByID("bg1") != null);	
	
	}
	
	@Test
	public void testShouldLoadScenesWithAttributesCorrect() {
		setUp();
		Vector<ASTRAALScene> scenes = rt.getScenes();
		assertTrue(scenes.size() == 2);
		ASTRAALScene one = scenes.get(0), two = scenes.get(1);
		assertTrue(one.getID().equals("lab"));
		assertTrue(two.getID().equals("lab2"));
		assertTrue(one.getResourceBlockID().equals("main"));
		assertTrue(two.getResourceBlockID().equals("set_two"));
	}
	
	@Test
	public void testShouldLoadObjectsFromSceneWithCorrectMetadata() {
		setUp();
		Vector<ASTRAALScene> scenes = rt.getScenes();
		ASTRAALScene one = scenes.get(0);
		Vector<ASTRAALObject> objs = one.getObjects();
		assertTrue(objs.size() == 2);
		ASTRAALObject obj1 = objs.get(0), obj2 = objs.get(1);
		
		assertTrue(obj1.getID().equals("jar"));
		assertTrue(obj1.getSpriteID().equals("spr1"));
		assertTrue(obj1.getX() == 4);
		assertTrue(obj1.getY() == 4);
		assertTrue(obj1.getWidth() == 40);
		assertTrue(obj1.getHeight() == 40);
		
		assertTrue(obj2.getID().equals("jar2"));
		assertTrue(obj2.getSpriteID().equals("spr2"));
		assertTrue(obj2.getX() == 42);
		assertTrue(obj2.getY() == 100);
		assertTrue(obj2.getWidth() == 40);
		assertTrue(obj2.getHeight() == 40);		
	}
	
	@Test
	public void testShouldLoadAnimationCorrectly() {
		setUp();
		Vector<ASTRAALResourceBlock> b = rt.getResourceBlocks();
		Vector<ASTRAALResource> sprites0 = b.get(0).getResources();
		ASTRAALResAnimation anim = (ASTRAALResAnimation) sprites0.get(0);
		assertTrue(anim.getID().equals("spr1"));
		assertTrue(anim.getFPS() == 4);
		assertTrue(anim.getWidth() == 40);
		assertTrue(anim.getHeight() == 40);
	}
	
	@Test
	public void testShouldLoadItems() {
		setUp();
		Vector<ASTRAALInventoryItem> items = rt.getItems();
		assertTrue(items.size() == 2);
		assertTrue(items.get(0).getID().equals("item_star"));
		assertTrue(items.get(0).getName().equals("Star"));
		assertTrue(items.get(0).getSpriteID().equals("spr_star"));
		assertTrue(items.get(0).getDescription().equals("A star, though not of the stone variety."));
		
		assertTrue(items.get(1).getID().equals("item_rune"));
	}

	
	@Test
	public void testShouldLoadJournals() {
		setUp();
		Vector<ASTRAALJournal> journals = rt.getJournals();
		assertTrue(journals.size() == 1);
		assertTrue(journals.get(0).getID().equals("personal"));
		assertTrue(journals.get(0).getTitle().equals("Notes"));
		
		Vector<ASTRAALJournalPage> pages = journals.get(0).getPages();
		assertTrue(pages.get(0).getID().equals("one"));
		assertTrue(pages.get(0).getTitle().equals("Awoken"));
		assertTrue(pages.get(0).getText().trim().equals("I've woken up in a strange lab... but I can't remember who I am, or how I got here."));
		
	}

	
}
