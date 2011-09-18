package org.github.rnubel.miskatonic.astraal;

import java.util.Vector;

import org.github.rnubel.miskatonic.Animation;
import org.w3c.dom.Node;


/**
 * A scene in the game, which might not be a single room.
 */
public class ASTRAALScene {

	private String mID;
	private String mResourceBlockID;
	private String mBackgroundID;
	private int mWidth;
	private int mHeight;
	private ASTRAALResAnimation mBackground;
	private ASTRAALResourceBlock mResourceBlock;

	private Vector<ASTRAALObject> mObjects;
	private Vector<ASTRAALNavigationCue> mNavigationCues;
	
	
	


	/** Create a new scene with basic data. 
	 * @param height 
	 * @param width */
	public ASTRAALScene(String id, String resourceblock, String background, int width, int height) {
		this.mID = id;
		this.mResourceBlockID = resourceblock;
		this.mBackgroundID = background;
		this.mWidth = width;
		this.mHeight = height;
		
		this.mObjects = new Vector<ASTRAALObject>();
		this.mNavigationCues = new Vector<ASTRAALNavigationCue>();
	}

	public void addObject(ASTRAALObject object) {
		this.mObjects.add(object);
	}
	
		
	/**
	 * @return the mID
	 */
	public String getID() {
		return mID;
	}

	/**
	 * @param mID the mID to set
	 */
	public void setID(String mID) {
		this.mID = mID;
	}

	/**
	 * @return the mResourceBlock
	 */
	public String getResourceBlockID() {
		return mResourceBlockID;
	}

	/**
	 * @param mResourceBlock the mResourceBlock to set
	 */
	public void setResourceBlockID(String mResourceBlock) {
		this.mResourceBlockID = mResourceBlock;
	}

	/**
	 * @return the mObjects
	 */
	public Vector<ASTRAALObject> getObjects() {
		return mObjects;
	}

	/**
	 * @param mObjects the mObjects to set
	 */
	public void setObjects(Vector<ASTRAALObject> mObjects) {
		this.mObjects = mObjects;
	}

	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}

	public String getBackgroundID() {
		return mBackgroundID;
	}

	/** Set the actual resource that our background uses. */
	public void setBackground(ASTRAALResource resource) {
		mBackground = (ASTRAALResAnimation)resource;		
	}

	public ASTRAALResAnimation getBackground() {
		return mBackground;
	}

	public void setResourceBlock(ASTRAALResourceBlock block) {
		mResourceBlock = block;
	}
	
	public ASTRAALResourceBlock getResourceBlock() {
		return mResourceBlock;
	}

	public void addNavigationCue(ASTRAALNavigationCue cue) {
		mNavigationCues.add(cue);		
	}

	public Vector<ASTRAALNavigationCue> getNavigationCues() {
		return mNavigationCues;
	}
 
}
