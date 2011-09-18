package org.github.rnubel.miskatonic;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

import org.github.rnubel.miskatonic.*;
import org.github.rnubel.miskatonic.GameView.*;
import org.github.rnubel.miskatonic.aal.AALExecutionState;
import org.github.rnubel.miskatonic.aal.AALInterpreter;
import org.github.rnubel.miskatonic.aal.AALStatement;
import org.github.rnubel.miskatonic.aal.AALStatementBlock;
import org.github.rnubel.miskatonic.astraal.*;

import android.os.Bundle;
import android.util.Log;


/**
 * AKA "Azathoth", the GameExecutor is responsible for interpreting the ASTRAAL
 * definition of the game (a parsed representation of the XML file) into an actual
 * game.
 */
public class GameExecutor {
	private static final String KEY_SCENE_STATES = "sceneStates";

	private static final String KEY_CURRENT_SCENE_ID = "currentSceneID";
	
	private GameThread mGame;
	private ASTRAALRoot mAstraal;
	
	/** Template for current scene. */
	private ASTRAALScene mCurrentScene;
	
	/** State of our journal system. */
	private JournalStatus mJournalState = new JournalStatus();

	/** 
	 * Map of Scene states, so that we can restore object positions in
	 * a persistent fashion. 
	 */
	private HashMap<String, Bundle> mSceneStates;

	/** An execution state which we persist for all scenes. */
	private AALExecutionState mExecutionState;
	
	/** A buffer of AAL commands to execute when returning to the main game state. */
	private AALStatement mStatementBuffer;
	
	/** Create new GameExecutor. We need both a game and an ASTRAAL tree to use. */
	public GameExecutor(GameThread gt, ASTRAALRoot root) {
		this.mGame = gt;
		this.mAstraal = root;
		this.mExecutionState = new AALExecutionState();
		
		this.mSceneStates = new HashMap<String, Bundle>();
		
		mExecutionState.attachToGameExecutor(this);
	}
	
	/** Find a starting point, i.e. the first scene, and switch to it. */
	public void startGame() {
		this.switchToScene(mAstraal.getFirstScene());
	}
	
	/** Save this game to an output stream. */
	public void save(OutputStream out) {
		try {
			ObjectOutputStream s = new ObjectOutputStream(out);
			
			// Save our state to a bundle, then convert it to a HashMap and write to file.
			Bundle saveBundle = new Bundle();
			this.saveToBundle(saveBundle);
			
			HashMap<String, Serializable> saveMap = 
				Utility.convertBundleToHashMap(saveBundle);
			
			s.writeObject(saveMap);
			
			s.close();			
			
		} catch (IOException e) {
			System.out.println("Error saving to file:");
			e.printStackTrace();
		}
	}

	/** Load this game from an input stream.  */
	@SuppressWarnings("unchecked")
	public void load(InputStream in) {
		try {
			ObjectInputStream s = new ObjectInputStream(in);
			
			// Load a state map from the file, then convert it to a bundle and load that.
			try {
				HashMap<String, Serializable> loadMap = 
					(HashMap<String, Serializable>) s.readObject();
				
				Bundle loadBundle = Utility.convertHashMapToBundle(loadMap);
				
				this.loadFromBundle(loadBundle);
				
			} catch (ClassNotFoundException e) {
				System.out.println("Invalid file format!");
				e.printStackTrace();
			}
								
			s.close();			
			
		} catch (IOException e) {
			System.out.println("Error loading from file!");
			e.printStackTrace();
		}
	}
	
	/** Save the game's state as needed into the bundle. */
	public void saveToBundle(Bundle b) {
		/* What needs to happen:
		 *  1. Save current scene ID.
		 *  2. Save current scene data.
		 *  3. Save current execution state. */
		b.putString(KEY_CURRENT_SCENE_ID, mCurrentScene.getID()); // Scene doesn't really care, but we do.
		
		// Update our statemap, so we only have to save it.
		saveStateForCurrentScene();
		
		// Create a sub-bundle for all scenes' data.
		Bundle scenesBundle = new Bundle();
		for (Entry<String, Bundle> entry : mSceneStates.entrySet()) {
			scenesBundle.putBundle(entry.getKey(), entry.getValue());
		}
		b.putBundle(KEY_SCENE_STATES, scenesBundle);
					
		// Save the execution state as well.
		mExecutionState.saveToBundle(b);	
		
		// We keep track of the journal and inventory, too.
		saveJournalStatus(b);		
		saveInventoryStatus(b);
	}

	/** Save list of inventory items into bundle. */
	private void saveInventoryStatus(Bundle b) {
		mGame.getInventory().saveToBundle(b);		
	}

	/**
     * Restore the game's state from a bundle. This must (obviously) be called after
	 * the constructor, so ensure that the ASTRAAL tree has been created properly first. 
	 */
	public void loadFromBundle(Bundle b) {
		String sceneID = b.getString(KEY_CURRENT_SCENE_ID);
		
		// Update our scene states data.
		Bundle scenesBundle = b.getBundle(KEY_SCENE_STATES);
		for (String key : scenesBundle.keySet()) {
			mSceneStates.put(key, scenesBundle.getBundle(key));
		}

		// From our new map, update the current scene.
		// restoreStateForCurrentScene();
				
		// Reload our journal and inventory states.		
		updateJournalPages(b);
		updateInventory(b);

		// Reload our script state.
		mExecutionState.loadFromBundle(b);				
		
		// Switch to that scene, reloading what we need to.
		switchToScene(sceneID);
	}

	/** Update the inventory with items as loaded from the given bundle. */
	private void updateInventory(Bundle b) {
		mGame.getInventory().loadFromBundle(b, this);		
	}

	/** Save the status of our journal pages. */
	private void saveJournalStatus(Bundle b) {		
		mJournalState.saveToBundle(b);		
	}

	
	/** Unlock journal pages as per the bundle. */
	private void updateJournalPages(Bundle b) {
		mJournalState.loadFromBundle(b);
		
		for (JournalPagePath p : mJournalState.unlockedPages) {
			if (p == null) break;
			// TODO: Use a JournalPagePath in all places?
			unlockJournalPage(p.journalID, p.pageID);
		}
	}

	/** Save the state of the current scene, so we can restore it when revisiting. */
	private void saveStateForCurrentScene() {		
		Bundle sceneBundle = new Bundle();
		mGame.getCurrentScene().saveToBundle(sceneBundle);
		mSceneStates.put(mCurrentScene.getID(), sceneBundle);		
	}
	
	/** Restore the state for the new scene, if applicable. */
	private boolean restoreStateForCurrentScene() {
		if (mSceneStates.containsKey(mCurrentScene.getID())) {
			mGame.getCurrentScene().loadFromBundle(
				mSceneStates.get(mCurrentScene.getID()));
			return true;
		} else {
			return false;
		}		
	}
	
	/** Switch to a given scene from its ID. */
	public void switchToScene(String sceneID) {		
		// If we have a current scene, save its state first.
		if (mCurrentScene != null) {
			saveStateForCurrentScene();
		}
		
		switchToScene(mAstraal.getSceneByID(sceneID));
	}
	
	/** Load the resources for and set up the given scene. */
	public void switchToScene(ASTRAALScene scene) {
		// Throw up the loading screen.
		mGame.setState(StateType.Loading);
		
		mCurrentScene = scene;
		mAstraal.loadResourcesFor(mCurrentScene);
		
		/* What needs to happen:
		 * 	1. All current objects removed.
		 * 	2. Background object placed.
		 * 	3. Foreground objects placed.
		 * 	4. Set up any necessary triggers.
		 */
		
		// Create a new scene of the needed dimensions. We only need the width 
		// and height because the scene needs to know its bounds.		
		mGame.resetScene(scene.getWidth(), scene.getHeight());
		
		// Reset the camera and re-attach it to the new scene.
		mGame.resetCamera();
		
		// Set the background for the scene. We rely here on the assumption that 
		// the background is an animation; however, this may not always be the 
		// case.
		mGame.setBackground((Animation)(scene.getBackground().getResource()));
		
		// Place foreground objects.
		attachForegroundObjects();
		
		// Give the scene an execution state for executing scripts.
		mGame.setExecutionState(mExecutionState);
		
		// Flush our execution buffer.
		flushBuffer();
				
		// Reload the scene's state (object positions and such).
		if (!restoreStateForCurrentScene()) {
			// If this returns false, the scene did not currently exist. So,
			// call all objects' onLoad events.
			doOnLoadForSceneObjects();
		}

		// Call onEnter events for all scene objects.
		doOnEnterForSceneObjects();

		// Remove the loading screen if it's still up.
		if (mGame.getCurrentStateType() == StateType.Loading) {
			mGame.setState(StateType.Main);
		}
	}
	
	/** Call the onLoad event for all objects in the current scene. */
	private void doOnLoadForSceneObjects() {		
		for (DrawnObject obj : mGame.getCurrentScene().getAllObjects()) {
			// Call the object's onload event.
			obj.doOnLoad(mExecutionState);
		}
	}

  /** Call the onEnter event for all objects in the current scene. */
	private void doOnEnterForSceneObjects() {		
		for (DrawnObject obj : mGame.getCurrentScene().getAllObjects()) {
			// Call the object's onenter event.
			obj.doOnEnter(mExecutionState);
		}
	}
	
  /**
	 * Attach all known foreground objects for the current scene to the game.
	 * FIXME: Refactor the separate components of this method out.
	 */
	public void attachForegroundObjects() {
		// Prepare the resources object.		
		ASTRAALResourceBlock block = mCurrentScene.getResourceBlock();
		
		// Iterate across all objects in this scene.		
		Iterator<ASTRAALObject> i = mCurrentScene.getObjects().iterator();
		while (i.hasNext()) {
			// Attach this object.
			ASTRAALObject obj = i.next();
			
			// Find the sprite for the object and make a new object.
			DrawnObject dObj;						
			if (obj.getSpriteID().equals("")) {
				dObj = new ForegroundObject((Animation)null, obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
			} else {
				Animation animation;
				ASTRAALResAnimation anim;

				anim = (ASTRAALResAnimation)(block.getResourceByID(obj.getSpriteID()));
				animation = (Animation)anim.getResource();
				dObj = new ForegroundObject(animation, obj.getX(), obj.getY());
			}
			
			

			// Attach scripting logic.
			dObj.setID(obj.getID());
			dObj.attachEventHandler(obj.getEventHandler());
			
			// Attach the object to the game's scene.
			mGame.addObject(dObj);
		}		

		// Now, add the navigation cues.
		for (ASTRAALNavigationCue cueTemplate : mCurrentScene.getNavigationCues()) {
			// TODO: Is the call to getMainGameState knowing too much? Maybe it's too late at this point, anyway.
			mGame.getMainGameState().addNavigationCue(cueTemplate.getPosition(), cueTemplate.getNewSceneID());
		}
		
		// Force the scene to update and actually add the queues. This is bad design... =/
		mGame.getCurrentScene().doQueues();
	}

	public GameThread getGame() {
		return mGame;
	}

	public Camera getMainGameCamera() {
		return mGame.getMainGameState().getCamera();
	}

	/** Put the given item in the player's inventory. */
	public void giveItemToPlayer(String itemID) {
		ASTRAALInventoryItem aItem = mAstraal.getItemById(itemID);
		mGame.getMainGameState().getInventory().addItem(aItem);		
	}
	
	/** Return a list of journals. */
	public Vector<ASTRAALJournal> getJournalList() {
		return mAstraal.getJournals();
	}
	
	/** Return a specific journal. */
	public ASTRAALJournal getJournal(String id) {
		return mAstraal.getJournalByID(id);
	}
	
	/** Return a specific journal page. */
	public ASTRAALJournalPage getJournalPage(String jid, String pid) {
		return mAstraal.getJournalByID(jid).getPageByID(pid);
	}

	/** Get all pages for a given journal. */
	public Vector<ASTRAALJournalPage> getJournalPages(String jid) {
		return mAstraal.getJournalByID(jid).getPages();
	}
	
	/** Unlock the given journal page. */
	public void unlockJournalPage(String journalID, String pageID) {
		ASTRAALJournal j = mAstraal.getJournalByID(journalID);
		if (j == null) {
			Log.w("Miskatonic", "Attempted to unlock page from nonexistant journal " + journalID + ".");			
		} else {
			ASTRAALJournalPage p = j.getPageByID(pageID);
			if (p == null) {
				Log.w("Miskatonic", "Attempted to unlock nonexistant page " + pageID + " in journal " + journalID + ".");
			} else {
				p.setUnlocked(true);
			}
		}
		
		mJournalState.addUnlockedPage(journalID, pageID);
	}
	
	/** Save the given statements into a buffer, which will be executed when
	 *  the MainGameState is re-entered. */
	public void saveCommandsToBuffer(AALStatement b) {
		mStatementBuffer = b;		
	}
	
	/** Execute the commands in our buffer. */
	public void executeBuffer() {
		if (mStatementBuffer != null) { 
			mStatementBuffer.execute(mExecutionState);
			flushBuffer(); 
		}
	}

	/** Flush our execution buffer. */
	public void flushBuffer() {
		mStatementBuffer = null;
	}

	/** Play the given sound. */
	public void playSound(String id) {
		mGame.playSound(id);
	}
	
	/** Set a timer for the given object. */
	public void setTimer(String objID, long milliseconds) {
		mGame.setTimer(objID, milliseconds);
	}

	/** Return a reference to the execution state. */
	public AALExecutionState getExecutionState() {
		return mExecutionState;
	}
}
