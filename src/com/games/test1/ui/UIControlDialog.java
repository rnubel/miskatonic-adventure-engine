package com.games.test1.ui;

import java.util.Vector;

import com.games.test1.Utility;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class UIControlDialog extends UIControl {
	private static final float DEFAULT_TEXT_SIZE = 13.0f;
	
	private String mTitle;
	private String mText;
	private String mLeftLabel;
	private String mRightLable;
	private UIEvent mLeftEvent;
	private UIEvent mRightEvent;
	private int mWidth;
	private int mHeight;

	// Typesetting-related variables.
	private Vector<String> mLines;
	private int mTextHeight;
	private int mYStart;
	
	private static Paint dialogPaint;

	/** Creates a new dialog box. When either button is pressed, the respective
	 *  event is called. */
	public UIControlDialog(String title, String text, String leftButtonLabel,
			UIEvent leftEvent, String rightButtonLabel, UIEvent rightEvent,
			int width, int height) {
		
		mTitle = title;
		mText = text;
		mLeftLabel = leftButtonLabel;
		mRightLable = rightButtonLabel;
		mLeftEvent = leftEvent;
		mRightEvent = rightEvent;
		mWidth = width;
		mHeight = height;
						
		setupText();
	}

	private void setupText() {
		setupFont();
		
		// Prepare text for being drawn to the dialog.
		mLines = new Vector<String>(); 
		Utility.typesetText(mText, mWidth, mLines);
		mYStart = mHeight/2 - (mLines.size() * mTextHeight) / 2 + 20;
	}
	
	private void setupFont() {
		if (dialogPaint == null) {
			dialogPaint = new Paint();
			dialogPaint.setTextSize(DEFAULT_TEXT_SIZE);
			dialogPaint.setTextAlign(Paint.Align.CENTER);
			dialogPaint.setAntiAlias(true);
			dialogPaint.setColor(Color.WHITE);			
		}
		
		Rect bounds = new Rect();
		dialogPaint.getTextBounds("AAA", 0, 1, bounds);
		mTextHeight = (int) (Math.abs(bounds.top - bounds.bottom) * 1.5);
	}

	/** Draw the dialog box. */
	public void draw(Canvas c, int x, int y) {
		GameUI.scratchPaint.setStyle(Paint.Style.FILL);
		GameUI.scratchPaint.setColor(Color.GRAY);
		
		c.drawRect(x, y, x + mWidth, y + mHeight, GameUI.scratchPaint);
		c.drawText(mTitle, x, y + 4, dialogPaint);
		for (int i = 0; i < mLines.size(); i++) {					
			c.drawText(mLines.get(i), 
					x + mWidth/2, 
					y + mYStart + i * mTextHeight,
					dialogPaint);					
		}				
		
		GameUI.scratchPaint.setStyle(Paint.Style.STROKE);
	}
}
