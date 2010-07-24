package com.games.test1.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.games.test1.*;

import com.games.test1.Camera;

public class UtilityTests {
	@Test
	public void testShouldParseVersionAndGameName() {	
	//	System.out.println(Utility.moveTowards(0,0,1,1,(float) 1.414));
	//	System.out.println(Utility.moveTowards(0,0,-1,-1,(float) 1.414));
	}
	
	
	@Test
	public void testShouldConvertAbsoluteToRelativeWithOneMag() {
		Camera c = new Camera();
		c.setMag(1);
		c.setX(0);
		c.setY(0);		
		assertTrue(c.getAbsoluteX(0) == 0);
		assertTrue(c.getAbsoluteY(0) == 0);		
		assertTrue(c.getAbsoluteX(10) == 10);
		assertTrue(c.getAbsoluteY(10) == 10);
		
		c.setX(50);
		c.setY(50);		
		assertTrue(c.getAbsoluteX(0) == 50);
		assertTrue(c.getAbsoluteY(0) == 50);		
		assertTrue(c.getAbsoluteX(50) == 100);
		assertTrue(c.getAbsoluteY(80) == 130);
	}
	
	@Test
	public void testShouldConvertRelativeToAbsoluteWithOneMag() {
		Camera c = new Camera();
		c.setMag(1);
		c.setX(0);
		c.setY(0);		
		assertTrue(c.getRelativeX(0) == 0);
		assertTrue(c.getRelativeY(0) == 0);		
		assertTrue(c.getRelativeX(10) == 10);
		assertTrue(c.getRelativeY(10) == 10);
		
		c.setX(50);
		c.setY(50);		
		assertTrue(c.getRelativeX(50) == 0);
		assertTrue(c.getRelativeY(50) == 0);		
		assertTrue(c.getRelativeX(100) == 50);
		assertTrue(c.getRelativeY(130) == 80);
	}
	
	@Test
	public void testShouldConvertAbsoluteToRelativeWithTwoMag() {
		Camera c = new Camera();
		c.setMag(2);
		c.setX(0);
		c.setY(0);		
		assertTrue(c.getAbsoluteX(0) == 0);
		assertTrue(c.getAbsoluteY(0) == 0);		
		assertTrue(c.getAbsoluteX(10) == 5);
		assertTrue(c.getAbsoluteY(10) == 5);
		
		c.setX(50);
		c.setY(50);		
		assertTrue(c.getAbsoluteX(0) == 50);
		assertTrue(c.getAbsoluteY(0) == 50);		
		assertTrue(c.getAbsoluteX(50) == 75);
		assertTrue(c.getAbsoluteY(80) == 90);
	}
	
	@Test
	public void testShouldConvertRelativeToAbsoluteWithTwoMag() {
		Camera c = new Camera();
		c.setMag(2);
		c.setX(0);
		c.setY(0);		
		assertTrue(c.getRelativeX(0) == 0f);
		assertTrue(c.getRelativeY(0) == 0f);		
		assertTrue(c.getRelativeX(5) == 10f);
		assertTrue(c.getRelativeY(5) == 10f);
		
		c.setX(50);
		c.setY(50);		
		assertTrue(c.getRelativeX(50) == 0f);
		assertTrue(c.getRelativeY(50) == 0f);		
		assertTrue(c.getRelativeX(75) == 50f);
		assertTrue(c.getRelativeY(90) == 80f);
	}
}

