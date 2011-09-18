package org.github.rnubel.miskatonic;

import android.graphics.Canvas;

/**
 * A state of the game which can be entered, exited, etc. For 
 * example, a typical game might have MENU, GAME, and PAUSE 
 * states -- but States can also cover cases like minigames,
 * etc. For this purpose, States should be implemented by
 * inner classes of the main game, allowing them to use
 * functionality of the outer class without weighing down the
 * application.
 */
public class State {
	// Used? Not sure.
	protected static State instance = null;
	
	private StateType mType;

	
	/**
	 * Instantiate this state. Should be called only once...
	 */
	public State() {  
		instance = this;
	}	
	
	public static State getInstance() {
		return instance;
	}
		
	public StateType getType() {
		return mType;
	}

	/** 
	 * 	Callback for when a key is pressed down. Reliance on key
	 *  events is discouraged when possible, as not all devices 
	 *  have keyboards (i.e., the Archos). 
	 */
	public void onKeyDown(int keyCode) { }
	
	/** 
	 * Input callback for mouse press. I might use a more 
	 * elegant system, but the fact is we only have three of 
	 * these, ever. 
	 */
	public void onMousePress(int x, int y) { }
	
	/**
	 * Called when the mouse is dragged.
	 */
	public void onMouseMove(int x, int y) { }
	
	/** 
	 * Input callback for relase. 
	 */
	public void onMouseRelease(int x, int y) { }
	
	/**
	 * Start this state.  
	 */
	public void start() {	}
	
	/**
	 * Stop this state.
	 */
	public void stop() {	}
	
	/**
	 * Execute an iteration of this state.
	 */	
	public void run() {		}
	
	/**
	 * Draw to the given Canvas.
	 */
	public void draw(Canvas c) {	}

}
