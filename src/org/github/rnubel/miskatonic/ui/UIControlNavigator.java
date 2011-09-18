package org.github.rnubel.miskatonic.ui;

import org.github.rnubel.miskatonic.GameView;
import org.github.rnubel.miskatonic.GameView.GameThread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


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
