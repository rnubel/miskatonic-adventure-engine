package org.github.rnubel.miskatonic;

import android.graphics.Canvas;

public abstract class CameraEffect {
	/** Return true if the effect is done and can be removed. */
	public abstract boolean update();
	
	/** Draw the effect on top of the given canvas. */
	public abstract void draw(Canvas c);
}
