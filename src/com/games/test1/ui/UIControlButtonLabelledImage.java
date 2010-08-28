package com.games.test1.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class UIControlButtonLabelledImage extends UIControlButton {
	private Bitmap mBitmap;

	/** Make a button that uses a defined Bitmap, rather than a text button. */
	public UIControlButtonLabelledImage(int w, int h, String label, Bitmap image, UIEvent e) {
		mWidth = w;
		mHeight = h;
		mEvent = e;
		mBitmap = image;
		mLabel = label;
	}
	
	/** Draw this button. */
	public void draw(Canvas c, int x, int y) {		
		c.drawBitmap(mBitmap, x, y, GameUI.scratchPaint);
		GameUI.scratchPaint.setColor(Color.WHITE);
		GameUI.scratchPaint.setTextSize(16.0f);
		GameUI.scratchPaint.setTextAlign(Paint.Align.CENTER);
		c.drawText(mLabel, x + mWidth / 2, y + mHeight / 2 + 5, GameUI.scratchPaint);
	}
}
