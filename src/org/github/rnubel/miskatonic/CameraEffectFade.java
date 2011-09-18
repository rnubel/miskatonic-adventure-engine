package org.github.rnubel.miskatonic;

import android.graphics.Canvas;

public class CameraEffectFade extends CameraEffect {
	private int mLevel;
	private boolean mFadeIn;
	private int mSpeed;
	
	public CameraEffectFade(boolean in) { this(in, 5); }
	public CameraEffectFade(boolean in, int speed) {
		mFadeIn = in;
		mSpeed = speed;
		if (mFadeIn) {
			mLevel = 255;
		} else {
			mLevel = 0;
		}
	}
	
	public void draw(Canvas c) {
		c.drawARGB(mLevel, 0, 0, 0);
	}

	public boolean update() {
		if (mFadeIn) {
			return (mLevel-=mSpeed) <= 0;
		} else {
			return (mLevel+=mSpeed) >= 255;
		}
	}

}
