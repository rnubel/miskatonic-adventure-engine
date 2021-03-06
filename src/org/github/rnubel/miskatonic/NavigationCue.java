package org.github.rnubel.miskatonic;

import org.github.rnubel.miskatonic.*;
import org.github.rnubel.miskatonic.aal.*;

import android.graphics.Canvas;


/**
 * A NavigationCue (or navigator) is a pseudo-object that is added on top of a Scene at the edges,
 * to allow the player to transition between scenes that don't have obvious
 * doors/exits/etc.
 */
class NavigationCue extends DrawnObject {
	public static final int NAVIGATOR_WIDTH = 60;
	public static final int NAVIGATOR_HEIGHT = 300;

	public enum Direction {
		Left,
		Right,
		Bottom,
		Top
	}

	private Direction mDirection;
	
	public static Animation getNavigatorAnimation() {
		return new Animation(GameView.imageNavigatorRight, 2, NAVIGATOR_WIDTH * 2, NAVIGATOR_HEIGHT, 1);
	}
		
	/** Mocked out, since a Navigation cue has no actual sprite. */
	@Override
	public String getSpriteID() {
		return DrawnObject.DO_NOT_DRAW_SPRITE_ID;
	}
	
	/** Construct the navigator given a position and target scene. */
	public NavigationCue(int x, int y, String sceneID, Direction direction) {
		super(x, y, NAVIGATOR_WIDTH, NAVIGATOR_HEIGHT);
	
		// Rotate the image to match our direction. We avoid setRot() just to save a little time.
		switch (direction) {
		case Left:
			mRot = 180.0f;
			break;
		case Top:
			mRot = 270.0f;
			break;
		case Bottom:
			mRot = 90.0f;
			break;
		default:
			mRot = 0.0f;
			break;
		}

		mAnimation = getNavigatorAnimation();	
			
		// Attach the changeScene event, going through AAL.
		AALExpression[] args = { new AALExpressionValue(new AALValue(sceneID)) };
		AALStatement onclickStatement = new AALStatementExpression(
			new AALExpressionFunctionCall("changeScene", args)
		);
		EventHandler e = new EventHandler();
		e.attachEvent(EventHandler.Type.OnClick, onclickStatement);
		this.attachEventHandler(e);
	}
	

}
