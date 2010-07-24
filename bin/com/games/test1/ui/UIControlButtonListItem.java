package com.games.test1.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class UIControlButtonListItem extends UIControlButton {
	/** Make a button that uses a defined Bitmap, rather than a text button. */
	public UIControlButtonListItem(int w, int h, String label, UIEvent e) {
		super(w, h, label, e);
	}
	
	/** Draw this button. */ 
	public void draw(Canvas c, int x, int y) {
		GameUI.scratchPaint.setStyle(Paint.Style.FILL);
		GameUI.scratchPaint.setColor(Color.rgb(60, 60, 60));		
		c.drawRect(x, y, x + mWidth, y + mHeight, GameUI.scratchPaint);
		
		GameUI.scratchPaint.setColor(Color.WHITE);
		GameUI.scratchPaint.setTextSize(16.0f);
		GameUI.scratchPaint.setTextAlign(Paint.Align.CENTER);
		c.drawText(mLabel, x + mWidth / 2, y + mHeight / 2 + 5, GameUI.scratchPaint);
	}
}
