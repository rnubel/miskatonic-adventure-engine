package com.games.test1;

import java.util.HashMap;
import java.util.Vector;

import com.games.test1.aal.AALExecutionState;
import com.games.test1.aal.AALValue;
import com.games.test1.astraal.ASTRAALResourceFactory;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
 

/**
 * Contains, draws and manages objects currently in the scene. 
 */
public class Scene {
	
	private static final String SELECTED_ITEM_ID = "SELECTED_ITEM_ID";
	private static final String KEY_SCENE_OBJECTS = "sceneObjects";
	/** All objects in the scene. */
	private Vector<DrawnObject> mObjects;
	/** Transient objects, which are not saved and can disappear. */
	private Vector<Effect> mEffects;
	
	private HashMap<String, DrawnObject> mObjectMap;
	private int mSceneWidth;
	private int mSceneHeight;
	private AALExecutionState mExecutionState;
	private Vector<Effect> mEffectRemovalQueue = new Vector<Effect>();
	private Vector<Effect> mEffectAdditionQueue = new Vector<Effect>();
	private Vector<DrawnObject> mObjectRemovalQueue = new Vector<DrawnObject>();
	private Vector<DrawnObject> mObjectAdditionQueue = new Vector<DrawnObject>();
	
		
	public Scene()	{
		mObjects = new Vector<DrawnObject>();
		mEffects = new Vector<Effect>();
		mObjectMap = new HashMap<String, DrawnObject>();
	}	
	
	public Scene(int sceneWidth, int sceneHeight) {
		this();
		
		mSceneWidth = sceneWidth;
		mSceneHeight = sceneHeight;		
	}
	
	public void moveObject(String objID, int x, int y) {
		mObjectMap.get(objID).setX(x);
		mObjectMap.get(objID).setY(y);
	}	
	
	/** Update every object, effect, etc. in this scene. */
	public void updateScene()	{
		for (DrawnObject obj : mObjects) {
			obj.update();			
		}
		
		for (Effect e : mEffects) {
			if (e.update()) {
				mEffectRemovalQueue.add(e);
			}
		}
	}

	/** 
	 * Called by Camera for efficiency reasons. If effects aren't
	 * getting removed or added, then Camera was changed. 
	 */
	public synchronized void doQueues() {		
		while (!mEffectRemovalQueue.isEmpty()) {
			mEffects.remove(mEffectRemovalQueue.get(0));
			mEffectRemovalQueue.remove(0);
		}
			
		while (!mEffectAdditionQueue.isEmpty()) {
			mEffects.add((Effect) mEffectAdditionQueue.get(0));
			mEffectAdditionQueue.remove(0);
		}
		
		while (!mObjectRemovalQueue.isEmpty()) {
			mObjects.remove(mObjectRemovalQueue.get(0));
			mObjectRemovalQueue.remove(0);
		}
			
		while (!mObjectAdditionQueue.isEmpty()) {
			mObjects.add((DrawnObject) mObjectAdditionQueue.get(0));
			mObjectAdditionQueue.remove(0);
		}
	}
	
	/** Pass down click events. */
	public void doOnClick(Camera c, int x, int y) {
		int clickX = c.getAbsoluteX(x);
		int clickY = c.getAbsoluteY(y);
		
		for (DrawnObject lObj : mObjects) {						
			Rect lZone = lObj.getClickRect();
			if (lZone.contains(clickX,clickY))
			{
				lObj.doOnClick(mExecutionState);
				
				// TODO: Better way to do this?
				if (lObj.getClass() == ForegroundObject.class) {
					addEffect(new EffectHighlight(lObj));
				}
			}
		}
	}
	
	/** Pass down a combine event (with an inventory item selected). */
	public void doOnCombine(Camera c, int x, int y, InventoryItem item) {
		int clickX = c.getAbsoluteX(x);
		int clickY = c.getAbsoluteY(y);
	
		for (DrawnObject lObj : mObjects) {						
			Rect lZone = lObj.getClickRect();
			if (lZone.contains(clickX,clickY))
			{
				mExecutionState.setVariable(SELECTED_ITEM_ID, new AALValue(item.getID()));
				lObj.doOnCombine(mExecutionState);
			}
		}
	}
	
	/** Return a vector of all objects. */
	public Vector<DrawnObject> getAllObjects()	{
		return mObjects;
	}
	
	/** Return a vector of all effects. */
	public Vector<Effect> getAllEffects() {
		return mEffects;
	}
			
	/** Add an object to this scene. */
	public void addObject(DrawnObject obj)	{
		mObjectAdditionQueue.add(obj);
		mObjectMap.put(obj.getID(), obj);
	}
	
	/** Add an effect to this scene. */
	public void addEffect(Effect e) {
		mEffectAdditionQueue.add(e);
	}
	
	/** Remove an object from this scene. */
	public void removeObject(DrawnObject obj)	{
		mObjectRemovalQueue.remove(obj);
		mObjectMap.remove(obj.getID());
	}
	
	public void clearScene()	{
		mObjects.clear();
		mEffects.clear();
	}
	
	public int getWidth()	{
		return mSceneWidth;
	}
	
	public int getHeight()	{
		return mSceneHeight;
	}
	
	public void setSceneWidth(int w)	{
		mSceneWidth = w;
	}
	
	public void setSceneHeight(int h)	{
		mSceneHeight = h;
	}
	
	/** Set an execution state to use when executing events. */
	public void setExecutionState(AALExecutionState executionState) {
		mExecutionState = executionState;		
		// Establish back-reference.
		mExecutionState.attachToScene(this);
	}

	/** Find a DrawnObject from its ID. */
	public DrawnObject getObjectFromID(String objID) {
		return mObjectMap.get(objID);
	}

	
	
	
	/** 
	 * Write everything this scene needs to know to memory. We do
	 * not need to save the ExecutionState, as GameExecutor will
	 * hold on to that when saving and loading the game. 
	 */
	public void saveToBundle(Bundle b) {
		// All we really need to save in any given situation is
		// the ExecutionState, which GameExecutor is responsible 
		// for, and the positions, sprites, etc. of the objects. 
		// To that end, parceling just ObjectData does the trick.
					

		// Bundle up an array of objects.
		ObjectData[] objArray = new ObjectData[mObjects.size()];		
		int i = 0;
		for (DrawnObject d : mObjects) {
			// Write this object's basic data to the bundle.
			objArray[i++] = new ObjectData(d);			
		}	

		b.putParcelableArray(KEY_SCENE_OBJECTS, objArray);
	}


	/**
	 * Update our objects based on the data in the bundle.
	 */
	public void loadFromBundle(Bundle b) {		
		
		ObjectData[] objArray = (ObjectData[]) b.getParcelableArray(KEY_SCENE_OBJECTS);
		DrawnObject d;
		for (ObjectData o : objArray) {
			// Look up the object by its ID and update it accordingly.
			d = getObjectFromID(o.getObjectID());
			d.setX(o.getX());
			d.setY(o.getY());
			d.setW(o.getWidth());
			d.setH(o.getHeight());
			if (d.getSpriteID() != o.getSpriteID() && !o.getSpriteID().equals("") 
					&& !d.getSpriteID().equals(DrawnObject.DO_NOT_DRAW_SPRITE_ID)) {
				d.setSprite(o.getSpriteID());				
			}
		}
	}

	
}
