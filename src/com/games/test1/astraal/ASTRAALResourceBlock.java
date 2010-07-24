package com.games.test1.astraal;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.content.res.Resources;

import com.games.test1.*;
import org.w3c.dom.Node;

public class ASTRAALResourceBlock {

	private boolean mPreload;
	private String mID;

	private Vector<ASTRAALResource> mResources; 
	private Map<String, ASTRAALResource> mResourceMap;
	
	/**
	 * Create an new resource block, which is a block of resources to be loaded
	 * at one time.
	 * @param id - ID of this resource block
	 * @param preload - whether this block should be loaded when the game loads.
	 */
	public ASTRAALResourceBlock(String id, String preload) {
		mID = id;
		if (preload != null && preload.equalsIgnoreCase("true")) {
			mPreload = true;
		} else {
			mPreload = false;
		}
		
		mResources = new Vector<ASTRAALResource>();
		mResourceMap = new HashMap<String, ASTRAALResource>();
	}
	
	/**
	 * Add a resource to this block; we'll manage it from now on.
	 * @param res
	 */
	public void addResource(ASTRAALResource res) {
		mResources.add(res);
		
		// Add to our map for quick reference.
		mResourceMap.put(res.getID(), res);
	}
	
	/**
	 * Load all resources in this block, preparing them for use
	 * by ASTRAALObjects.
	 * @param res - Context for resources. Comes from GameThread.
	 */
	public void loadAllResources(Resources res) {
		for (int i = 0; i < mResources.size(); i++) {
			ASTRAALResource resource = mResources.get(i);
			resource.loadResource(res);
		}		
	}
	
	
	public String getID() {
		return mID;
	}
	
	/**
	 * Whether or not to preload this block of resources.
	 * @return
	 */
	public boolean getPreload() {
		return mPreload;
	}
	
	public Vector<ASTRAALResource> getResources() {
		return mResources;
	}

	public ASTRAALResource getResourceByID(String id) {
		return mResourceMap.get(id);
	}



}
