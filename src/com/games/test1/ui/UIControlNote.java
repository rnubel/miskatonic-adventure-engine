package com.games.test1.ui;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.games.test1.GameView;
import com.games.test1.GameView.GameThread;

public class UIControlNote extends UIControlMultiTextDisplayer {
	public static int LIFETIME_INCREMENT = 300;
	
	public int mLifetime;
	
	/** Create a new note. */
	public UIControlNote(int w, int h, Vector<String> captions) {
		mWidth = w;
		mHeight = h;
		mCaptions = captions;
		mLifetime = LIFETIME_INCREMENT * mCaptions.size();

		setupCaptionFont();
		computeTextHeight();
		updateCaption();
	}
	
	/** Trigger the event attached to this control. */
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
	
	/** Draw this note. */
	public void draw(Canvas c, int x, int y) {
		if (mLifetime-- <= 0) {
			removeSelf();
		}
		
		c.drawBitmap(GameView.imageUINote, new Rect(0,0,GameView.imageUINote.getWidth(), GameView.imageUINote.getHeight()),
										   new Rect(x, y, x + mWidth, y + mHeight), GameUI.scratchPaint);
		
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
