package com.games.test1.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.games.test1.GameView;
import com.games.test1.GameView.GameThread;

public class UIControlNavigator extends UIControl {
	public UIControlNavigator() {
		mWidth = GameView.iconCompass.getWidth();
		mHeight = GameView.iconCompass.getHeight();
	}
	
	/** Trigger the event attached to this button. */
	public boolean trigger(GameThread game, int mouseX, int mouseY) {
		game.getMainGameState().showInventory();
		return true;
	}

	/** Draw this button. */
	public void draw(Canvas c, int x, int y) {		
		c.drawBitmap(GameView.iconCompass, x, y, GameUI.scratchPaint);
	}

}
