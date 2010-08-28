package com.games.test1;

import java.util.HashMap;

import android.util.Log;

import com.games.test1.aal.AALExecutionState;
import com.games.test1.aal.AALStatement;

/**
 * An EventHandler is attached to both an ASTRAALObject (the "template" object) and 
 * a DrawnObject (the "actual" object), and it holds the functionality for
 * responding to whatever events the object was declared to respond to. It requires
 * an ExecutionState to work off of, passed in to the responseTo method, since it
 * executes AAL code.
 */
public class EventHandler {
	public enum Type {
		OnClick,
		OnLoad, 
		OnCombine,
		OnEnter,
		OnTimeout
	}
	
	public static Type getTypeFromEventName(String event) {
		if (event.equals("onclick")) {
			return Type.OnClick;
		} else if (event.equals("onload")) {
			return Type.OnLoad;
		} else if (event.equals("oncombine")) {
			return Type.OnCombine;
		} else if (event.equals("onenter")) {
      return Type.OnEnter;
    } else if (event.equals("ontimeout")) {
      return Type.OnTimeout;
    }

		return null;
	}
	
	private HashMap<Type, AALStatement> eventMap = new HashMap<Type, AALStatement>();
	
	public void attachEvent(Type type, AALStatement action) {
		eventMap.put(type, action);
	}
	
	public void respondTo(Type type, AALExecutionState state) {
		if (eventMap.containsKey(type)) {
			if (eventMap.get(type) != null) {
				eventMap.get(type).execute(state);
			}
		} 		
	}
}
