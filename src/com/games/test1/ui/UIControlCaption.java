package com.games.test1.ui;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.games.test1.GameView;
import com.games.test1.Utility;
import com.games.test1.GameView.GameThread;

public class UIControlCaption extends UIControl {
	private Vector<String> mCaptions;
	private Vector<String> mLines = new Vector<String>();
	private int mTextHeight;
	private int yMod;
	
	public static Paint captionPaint;
	
	/** Create a new button. */
	public UIControlCaption(int w, int h, Vector<String> captions) {
		mWidth = w;
		mHeight = h;
		mCaptions = captions;

		setupCaptionFont();
		computeTextHeight();
		updateCaption();
	}
	
	/** Append to our list of captions. */	
	public void addCaptions(Vector<String> captions) {		
		for (String caption : captions) {
			mCaptions.add(caption);
		}
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
	
	/** Set up the caption font if needed. */
	public void setupCaptionFont() {
		if (captionPaint == null) {
			captionPaint = new Paint();
			captionPaint.setColor(Color.WHITE);
			captionPaint.setTextSize(15.0f);
			captionPaint.setTextAlign(Paint.Align.CENTER);		
			captionPaint.setAntiAlias(true);
			captionPaint.setTypeface(GameView.captionTypeface);
		}
	}
	
	public void computeTextHeight() {
		setupCaptionFont();
		Rect bounds = new Rect();
		captionPaint.getTextBounds("AAA", 0, 1, bounds);
		mTextHeight = (int) (Math.abs(bounds.top - bounds.bottom) * 1.5) + 3;
	}
	
	private void updateCaption() {
		synchronized (mLines) {
			mLines = new Vector<String>(); 
			String nextCaption = mCaptions.size() > 1 ? mCaptions.get(0) + " [tap for more] " : mCaptions.get(0);
			Utility.typesetText(nextCaption, (int) (getWidth() * .95), mLines, captionPaint);
		
			yMod = getHeight()/2 - (mLines.size() * mTextHeight) / 2 + 13;
		}
	}

	/** Draw this button. */
	public void draw(Canvas c, int x, int y) {		
		GameUI.scratchPaint.setStyle(Paint.Style.FILL);
		GameUI.scratchPaint.setColor(Color.rgb(60, 60, 60));		
		c.drawRect(x, y, x + mWidth, y + mHeight, GameUI.scratchPaint);
		
		GameUI.scratchPaint.setColor(Color.rgb(120, 60, 60));		
		c.drawRect(x+1, y+1, x + mWidth - 1, y + mHeight - 1, GameUI.scratchPaint);
		
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
