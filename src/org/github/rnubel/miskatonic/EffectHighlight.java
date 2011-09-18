package org.github.rnubel.miskatonic;

import org.github.rnubel.miskatonic.ui.GameUI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/** A highlight effect to be overlaid on an object just clicked on. */
public class EffectHighlight extends Effect {
	private static final int DEFAULT_EFFECT_TIME = 30;
	private int mRadius, mTimer;

	public EffectHighlight(DrawnObject target) {
		mX = target.getX() + target.getW()/2;
		mY = target.getY() + target.getH()/2 + 5; // Eeeeh.
		mRadius = target.getW()/2;
		mTimer = DEFAULT_EFFECT_TIME;
	}
	
	public boolean update() {
		mTimer--;
		if (mTimer == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void draw(Canvas c, float x, float y) {
		GameUI.scratchPaint.setColor(Color.WHITE);
		GameUI.scratchPaint.setStyle(Paint.Style.STROKE);
		GameUI.scratchPaint.setStrokeWidth(4.0f);
		GameUI.scratchPaint.setAlpha(128);
		c.drawCircle(x, y, mRadius - mRadius * (1 - (float)mTimer/DEFAULT_EFFECT_TIME)/2, GameUI.scratchPaint);
		// Reset uncommon stuff.
		GameUI.scratchPaint.setAlpha(255);
		GameUI.scratchPaint.setStrokeWidth(1.0f);
	}
}
