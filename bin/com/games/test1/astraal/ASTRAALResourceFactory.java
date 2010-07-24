package com.games.test1.astraal;

import java.util.HashMap;

import com.games.test1.Animation;

/** Factory class to create resources on demand. */
public class ASTRAALResourceFactory {
	private static HashMap<String, ASTRAALResource> resourceMap = 
		new HashMap<String, ASTRAALResource>();
	
	/** Add the given animation to our map for creation. */
	public static void addByID(String id, ASTRAALResource res) {
		resourceMap.put(id, res);
	}
	
	/** 
	 * Return an instance of the Animation that the given resource defines. 
	 * Note that this requires that the resource has been loaded, which means
	 * that it will die horribly (need to handle exception) if you e.g. try
	 * to give a player an item in a zone that doesn't have that item's sprite
	 * in its resource block. So be careful.
	 * @param id ID of sprite to load. 
	 */
	public static Animation createAnimation(String id) {
		ASTRAALResAnimation resAnim = (ASTRAALResAnimation) resourceMap.get(id);		
		return (Animation) resAnim.getResource();
	}
}
