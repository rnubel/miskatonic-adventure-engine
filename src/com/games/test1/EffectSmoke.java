package com.games.test1;

import com.games.test1.ui.GameUI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/** An effect that creates a small cloud of smoke. */
public class EffectSmoke extends Effect {
	private static final int DEFAULT_EFFECT_TIME = 30;
	private int mTimer;
	
	public static Paint smokePaint;

	public EffectSmoke(DrawnObject target) {
		mX = target.getX() + target.getW()/2;
		mY = target.getY() + target.getH()/2 + 5; // Eeeeh.
	
		mTimer = DEFAULT_EFFECT_TIME;
	}
	
	public static Paint getPaint() {
		if (smokePaint == null) {
			// setup paint
		}
		return smokePaint;
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
		
	}
}
