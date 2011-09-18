package org.github.rnubel.miskatonic;

import org.github.rnubel.miskatonic.ui.GameUI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/** An effect that creates a small cloud of smoke. */
public class EffectSmoke extends Effect {
	private Animation mSmoke;
	private int lastFrame;
	
	public EffectSmoke(int x, int y) {
		mX = x;
		mY = y;
	
		mSmoke = GameView.animationSmoke;
		mSmoke.setFrameNum(0);
		lastFrame = 0;
	}
	
	public boolean update() {		
		if (mSmoke.getFrameNum() < lastFrame) {
			return true;
		} else {
			lastFrame = mSmoke.getFrameNum();
			return false;
		}
		 
	}
	
	public void draw(Canvas c, float x, float y) {	
		mSmoke.draw(c, (int)x, (int)y);		
	}
}
