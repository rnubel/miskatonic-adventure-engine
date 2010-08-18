package com.games.test1.astraal;

import java.util.HashMap;

import com.games.test1.Animation;
import com.games.test1.SoundClip;

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
	public static Animation getAnimation(String id) {
		ASTRAALResAnimation resAnim = (ASTRAALResAnimation) resourceMap.get(id);		
		return (Animation) resAnim.getResource();
	}
	
	/**
	 * Return an instance of the SoundClip that corresponds to the given ID.
	 * Note that this clip must have been loaded in the correct resource block,
	 * or else this function will die horribly.
	 */
	public static SoundClip getSoundClip(String id) {
		ASTRAALResSound resSound = (ASTRAALResSound) resourceMap.get(id);		
		return (SoundClip) resSound.getResource();
	}
	
	/** Free memory. */
	public static void cleanUp() {
		resourceMap.clear();
	}
}
