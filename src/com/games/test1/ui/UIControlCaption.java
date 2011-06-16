package com.games.test1.ui;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.games.test1.GameView.GameThread;

public class UIControlCaption extends UIControlMultiTextDisplayer {
	/** Create a new caption display. */
	public UIControlCaption(int w, int h, Vector<String> captions) {
		mWidth = w;
		mHeight = h;
		mCaptions = captions;

		setupCaptionFont();
		computeTextHeight();
		updateCaption();
	}
	
	/** Trigger the event attached to this button. */
	public boolean trigger(GameThread game, int mouseX, int mouseY) {		
		mCaptions.remove(0);
		if (mCaptions.isEmpty()) {
			removeSelf();
			mCaptions.add(""); // In case draw() gets called again.
		} else {
			updateCaption();					
		}		
		return true;
	}
	
	/** Draw this button. */
	public void draw(Canvas c, int x, int y) {		
		GameUI.scratchPaint.setStyle(Paint.Style.FILL);
		GameUI.scratchPaint.setColor(Color.rgb(60, 60, 60));		
		c.drawRect(x, y, x + mWidth, y + mHeight, GameUI.scratchPaint);
		
		GameUI.scratchPaint.setColor(Color.rgb(120, 60, 60));		
		GameUI.scratchPaint.setStrokeWidth(2.0f);
		c.drawRect(x+1, y+1, x + mWidth - 1, y + mHeight - 1, GameUI.scratchPaint);
		
		GameUI.scratchPaint.setStrokeWidth(1.0f);
		GameUI.scratchPaint.setColor(Color.rgb(50, 50, 50));		
		c.drawRect(x+2, y+2, x + mWidth - 2, y + mHeight - 2, GameUI.scratchPaint);
		
		synchronized (mLines) {			
			for (int i = 0; i < mLines.size(); i++) {					
				c.drawText(mLines.get(i), 
					x + getWidth()/2, 
					y + yMod + i * mTextHeight,
					captionPaint);					
			}
		}
	}

	
}
