package com.games.test1.aal;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.games.test1.CameraEffectFade;
import com.games.test1.DrawnObject;
import com.games.test1.GameExecutor;
import com.games.test1.Scene;
import com.games.test1.StateType;
import com.games.test1.astraal.ASTRAALResourceFactory;

/**
 * A state object, which carries the state of the game as
 * a whole -- this includes the symbol table, functions, etc.
 */
public class AALExecutionState {
	private static final String KEY_VARIABLE_MAP = "variableMap";

	/** Map of variable names to values. */
	private HashMap<String, AALValue> variableMap = new HashMap<String, AALValue>();
	
	/** Currently-attached scene. */
	private Scene mScene;

	/** Back-reference to game executor for calls that we can't do via scene. */
	private GameExecutor mExecutor;
	
	/** Return the value of a variable in the state. */
	public AALValue getVariable(String varname) {
		if (variableMap.containsKey(varname)) {
			return variableMap.get(varname);
		} else {
			return null; // Whuh oh... should throw an ACTUAL error.
		}
	}
	
	/** Set the value of a variable in the state. */
	public void setVariable(String varname, AALValue value) {
		if (variableMap.containsKey(varname)) {
			variableMap.remove(varname);			
		}
		
		variableMap.put(varname, value);		
	}

	/** Attach a Scene to ourselves, so we can modify it in callFunction as needed. */
	public void attachToScene(Scene scene) {
		mScene = scene;
	}
	
	/** Attach the GameExecutor to ourself, so we can make calls as needed to it. */  
	public void attachToGameExecutor(GameExecutor ex) {
		mExecutor = ex;
	}

	/** Save this state into a Bundle. */
	public void saveToBundle(Bundle b) {
		// Put all these in a sub-bundle to avoid conflicts.
		Bundle varbundle = new Bundle();
		for (Entry e : variableMap.entrySet()) {
			varbundle.putParcelable((String) e.getKey(), (AALValue) e.getValue());
		}

		b.putBundle(KEY_VARIABLE_MAP, varbundle);
	}

	/** Load this state from a Bundle. */
	public void loadFromBundle(Bundle b) {
		Bundle varbundle = b.getBundle(KEY_VARIABLE_MAP);
		for (String key : varbundle.keySet()) {
			// Convert from a parceled AALValue to a real one and put 
			// it in our map.
			variableMap.put(key, (AALValue) varbundle.getParcelable(key));
		}
	}
	

	/** 
	 * Called by AALExpressionFunctionCall to execute a function. This is
	 * the only means through which scripts can affect the actual game.
	 */
	public AALValue callFunction(String functionName, AALValue[] args) {
		/** Check against a list of functions. */
		
		// print [message]: PRINT A MESSAGE TO THE SCREEN.
		if (functionName.equals("print")) {
			System.out.println((String)(args[0].getValue()));
		} else
			
		// increment [var]: INCREMENTS var BY ONE. 
		if (functionName.equals("increment")) {
			String varname = (String)(args[0].getValue());
			AALValue cur = getVariable(varname);
			setVariable(varname, cur.plus(new AALValue(1)));			
		} else
			
		// changeScene [scene_id]: CHANGE TO GIVEN SCENE.
		if (functionName.equals("changeScene")) {
			String sceneID = (String)(args[0].getValue());				  
			
			mExecutor.switchToScene(sceneID);
		} else
		
		// moveObject [obj_id] [x] [y]: MOVE GIVEN OBJECT TO (x,y).
		if (functionName.equals("moveObject")) {
			String objID = (String)(args[0].getValue());				  
			int x = (Integer)(args[1].getValue()),
				y = (Integer)(args[2].getValue());
			
			mScene.moveObject(objID, x, y);
		} else
			
		// moveObjectBy [obj_id] [dx] [dy]: MOVE GIVEN OBJECT BY (dx,dy).
		if (functionName.equals("moveObjectBy")) {
			String objID = (String)(args[0].getValue());				  
			int dx = (Integer)(args[1].getValue()),
				dy = (Integer)(args[2].getValue());
			DrawnObject obj = mScene.getObjectFromID(objID);			
			mScene.moveObject(objID, (int) obj.getX() + dx, (int) obj.getY() + dy);
		} else
		
		// getObjectX [obj_id]: RETURNS INTEGER X COORDINATE OF OBJECT.
		if (functionName.equals("getObjectX")) {
			String objID = (String)(args[0].getValue());
			return new AALValue((int) mScene.getObjectFromID(objID).getX());
		} else
		
		// getObjectY [obj_id]: RETURNS INTEGER Y COORDINATE OF OBJECT.
		if (functionName.equals("getObjectY")) {
			String objID = (String)(args[0].getValue());
			return new AALValue((int) mScene.getObjectFromID(objID).getY());
		} else
		
		// getObjectSprite [obj_id]: RETURNS ID OF OBJECT'S CURRENT SPRITE.
		if (functionName.equals("getObjectSprite")) {
			String objID = (String)(args[0].getValue());
			DrawnObject o = mScene.getObjectFromID(objID);
			return new AALValue(o.getSpriteID());
		} else
			
		// setObjectSprite [obj_id] [spr_id]: SETS THE OBJECT'S SPRITE.
		if (functionName.equals("setObjectSprite")) {
			String  objID = (String)(args[0].getValue()),
					sprID = (String)(args[1].getValue());
			DrawnObject o = mScene.getObjectFromID(objID);
			o.setSprite(sprID);
		} else
						
		// setObjectAnimationFPS [obj_id] [fps]: SETS THE OBJECT'S ANIMATION'S
		// 	FRAMES-PER-SECOND.
		if (functionName.equals("setObjectAnimationFPS")) {
			String objID = (String)(args[0].getValue());
			int fps = (Integer)(args[1].getValue());
			
			DrawnObject o = mScene.getObjectFromID(objID);
			o.getAnimation().setFPS(fps);
		} else		
				
		// getObjectAnimationFrame [obj_id] [frame_num]: GETS THE OBJECT'S ANIMATION'S
		// 	FRAME NUMBER.
		if (functionName.equals("GetObjectAnimationFrame")) {
			String objID = (String)(args[0].getValue());
						
			DrawnObject o = mScene.getObjectFromID(objID);
			return new AALValue(o.getAnimation().getFrameNum());
		} else
			
		// setObjectAnimationFrame [obj_id] [frame_num]: SETS THE OBJECT'S ANIMATION'S
		// 	FRAME TO THE GIVEN FRAME NUMBER.
		if (functionName.equals("setObjectAnimationFrame")) {
			String objID = (String)(args[0].getValue());
			int frameNum = (Integer)(args[1].getValue());
			// TODO: Make this work as well.
			DrawnObject o = mScene.getObjectFromID(objID);
			o.getAnimation().setFrameNum(frameNum);
		} else
			
		// showCaptionCard [message]: SHOW THE GIVEN CAPTION CARD.
		if (functionName.equals("showCaptionCard")) {
			// Dynamically read in the arguments. If we need any special flags/etc, put
			// those first.
			Vector<String> captions = new Vector<String>();
			for (AALValue v : args) {
				captions.add((String) v.getValue());
			}
			
			mExecutor.getGame().showFullCaption(captions);
		} else
			
		// showCaption [message]: SHOW AN INLINE CAPTION (as UI element).
		if (functionName.equals("showCaption")) {
			// Dynamically read in the arguments. If we need any special flags/etc, put
			// those first.
			Vector<String> captions = new Vector<String>();
			for (AALValue v : args) {
				captions.add((String) v.getValue());
			}
			
			mExecutor.getGame().showHalfCaption(captions);
		} else

		// showMoreCaptions [message]: ADD MORE CAPTIONS TO CURRENT CAPTION LIST.
		if (functionName.equals("showMoreCaptions")) {
			// Dynamically read in the arguments. If we need any special flags/etc, put
			// those first.
			Vector<String> captions = new Vector<String>();
			for (AALValue v : args) {
				captions.add((String) v.getValue());
			}
			
			mExecutor.getGame().getMainGameState().addToHalfCaption(captions);
		} else
			
		// fadeIn [speed assumed 5]: FADE IN FROM BLACK.
		if (functionName.equals("fadeIn")) {
			int speed = (args.length > 0 ? (Integer) args[0].getValue() : 5);

			mExecutor.getMainGameCamera().setEffect(
				new CameraEffectFade(true, speed));
		} else
			
		// giveItem [item_id]: GIVE ITEM TO PLAYER (VIA INVENTORY).
		if (functionName.equals("giveItem")) {
			String itemID = (String) args[0].getValue();
			
			mExecutor.giveItemToPlayer(itemID);
		} else
			
		// promptTakeItem [item_id]: PROMPT PLAYER TO TAKE ITEM.
		if (functionName.equals("promptTakeItem")) {
			String itemID = (String) args[0].getValue();
			
			mExecutor.getGame().promptPlayerToTakeItem(itemID);
		} else
			
		// unlockJournalPage [journal_id] [page_id]: ALLOW THE PLAYER TO READ THE GIVEN JOURNAL PAGE.
		if (functionName.equals("unlockJournalPage")) {
			String journalID = (String) args[0].getValue(),
				   pageID = (String) args[1].getValue();
			
			mExecutor.unlockJournalPage(journalID, pageID);
		} else 
			
		// enterSanityMiniGame: PUTS THE PLAYER INTO THE SANITY MINIGAME UNTIL COMPLETED.
		if (functionName.equals("enterSanityMiniGame")) {			
			mExecutor.getGame().setState(StateType.SanityMiniGame);
		} else 
			
		// doOnReturnToMainGame [str_code]: EXECUTES THE GIVEN CODE AFTER RETURNING TO THE MAIN GAME.
		if (functionName.equals("doOnReturnToMainGame")) {
			String code = (String) args[0].getValue();
			AALStatement b = new AALInterpreter().interpret(code);
			mExecutor.saveCommandsToBuffer(b);
		} else
		
		// playSound [sound_id]: PLAYS THE GIVEN SOUND ONCE.
		if (functionName.equals("playSound")) {
			String id = (String) args[0].getValue();			
			mExecutor.playSound(id);
		} else
		
		// setTimerForObject [object_id] [delay]: SETS THE TIMER FOR THE GIVEN OBJECT.
		if (functionName.equals("setTimerForObject")) {
			String id = (String) args[0].getValue();
			int delay = (Integer) args[1].getValue();
			mExecutor.setTimer(id, delay);			
		}
			

		
		
		
	
		
			
		
		/** Got nothin'. Return null object. */
		return new AALValue();
	}	
}
