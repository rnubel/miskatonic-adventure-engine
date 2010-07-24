package com.games.test1.astraal;

/** An element in a scene that provides for scene transitions without explicit object triggers. */
public class ASTRAALNavigationCue {
	private String mPosition, mNewSceneID;
	
	public ASTRAALNavigationCue(String position, String newSceneID) {
		mPosition = position;
		mNewSceneID = newSceneID;
	}

	public String getPosition() {
		return mPosition;
	}

	public void setPosition(String position) {
		mPosition = position;
	}

	public String getNewSceneID() {
		return mNewSceneID;
	}

	public void setNewSceneID(String newSceneID) {
		mNewSceneID = newSceneID;
	}
}
