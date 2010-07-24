package com.games.test1.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class UIControlButtonImage extends UIControlButton {
	private Bitmap mBitmap;

	/** Make a button that uses a defined Bitmap, rather than a text button. */
	public UIControlButtonImage(int w, int h, Bitmap image, UIEvent e) {
		mWidth = w;
		mHeight = h;
		mEvent = e;
		mBitmap = image;
	}
	
	/** Draw this button. */
	public void draw(Canvas c, int x, int y) {		
		c.drawBitmap(mBitmap, x, y, GameUI.scratchPaint);
	}
}
