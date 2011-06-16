package com.games.test1.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.games.test1.GameView;
import com.games.test1.GameView.GameThread;

/** A basic button. */
public class UIControlButton extends UIControl {
	protected UIEvent mEvent;
	protected String mLabel;
		
	public UIControlButton() { }
	
	/**
	 * Create a new button.
	 * @param width
	 * @param height
	 * @param label
	 * @param event - Event to trigger when clicked. 
	 */
	public UIControlButton(int w, int h, String label, UIEvent e) {
		mWidth = w;
		mHeight = h;
		mEvent = e;
		mLabel = label;
	}
	
	/** Trigger the event attached to this button. */
	public boolean trigger(GameThread game, int mouseX, int mouseY) {
		mEvent.execute(game, this);
		return true;
	}

	/** Draw this button. */
	public void draw(Canvas c, int x, int y) {
		/*
		GameUI.scratchPaint.setStyle(Paint.Style.FILL);
		GameUI.scratchPaint.setColor(Color.rgb(60, 60, 60));		
		c.drawRect(x, y, x + mWidth, y + mHeight, GameUI.scratchPaint);
		
		GameUI.scratchPaint.setColor(Color.rgb(120, 60, 60));		
		c.drawRect(x+1, y+1, x + mWidth - 1, y + mHeight - 1, GameUI.scratchPaint);
		
		GameUI.scratchPaint.setColor(Color.rgb(50, 50, 50));		
		c.drawRect(x+2, y+2, x + mWidth - 2, y + mHeight - 2, GameUI.scratchPaint);
		*/
		
		GameUI.scratchPaint.setColor(Color.WHITE);
		GameUI.scratchPaint.setTextSize(16.0f);
		GameUI.scratchPaint.setTextAlign(Paint.Align.CENTER);
		c.drawText(mLabel, x + mWidth / 2, y + mHeight / 2 + 5, GameUI.scratchPaint);
	}
}
