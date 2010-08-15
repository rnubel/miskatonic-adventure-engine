package com.games.test1.ui;

import java.util.Vector;

import com.games.test1.GameView.GameThread;

import android.graphics.Canvas;

/**
 * Superclass for UI controls which contain other controls.
 */
public class UIContainer extends UIControl {
	public GameUI mInnerUI;
	
	public UIContainer(int w, int h) {
		mWidth = w;
		mHeight = h;
		mInnerUI = new GameUI(w, h, null);
	}
	
	public void draw(Canvas c, int x, int y) {
		c.save();
		c.translate(x, y);
		mInnerUI.draw(c);
		c.restore();
	}
	
	@Override
	public boolean trigger(GameThread game, int x, int y) {
		return mInnerUI.onClick(x, y);
	}
}
