package com.games.test1.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.games.test1.Inventory;
import com.games.test1.InventoryItem;
import com.games.test1.GameView.GameThread;

/** Draw the inventory panel to the screen. */
public class UIControlInventory extends UIControl {
	public static final int DEFAULT_ITEM_WIDTH = 48;
	public static final int DEFAULT_ITEM_HEIGHT = 48;
	private static final int PADDING_LEFT = 6;
	private static final int PADDING_TOP = 6;
	private static final int COL_WIDTH = 50;
	private static final int ROW_HEIGHT = 50;
	
	private Inventory mInventory;
	private int mCols;
	
	private static Paint scratchPaint = new Paint();

	/** Create the inventory control with an attached inventory. */
	public UIControlInventory(Inventory inv, int width, int height) {
		mInventory = inv;
		mWidth = width;
		mHeight = height;
		
		mCols = mWidth / DEFAULT_ITEM_WIDTH;
	}
	
	/** Respond to a click. */
	public void trigger(GameThread game, int mouseX, int mouseY) {
		int col = (mouseX - PADDING_LEFT) / COL_WIDTH,
			row = (mouseY - PADDING_TOP) / ROW_HEIGHT;
		int itemNum = row * mCols + col;		
		if (itemNum < 0 || itemNum >= mInventory.getItems().size()) {
			// Did not click on an item.						
		} else {			
			Log.w("Miskatonic", "CLICKED ON ITEM:" + mInventory.getItems().get(itemNum).getName());		
			game.getMainGameState().selectInventoryItem(mInventory.getItems().get(itemNum));
		}
		removeSelf();
	}
	
	/** Draw the inventory panel. */
	public void draw(Canvas c, int x, int y) {
		Rect r = new Rect(x, y, x+mWidth, y+mHeight);
		
		scratchPaint.setStyle(Paint.Style.FILL);
		scratchPaint.setColor(Color.GRAY);
		c.drawRect(r, scratchPaint);
		
		scratchPaint.setStyle(Paint.Style.STROKE);
		scratchPaint.setColor(Color.WHITE);		
		c.drawRect(r, scratchPaint);
		
		// Draw items in a grid. TODO: Scrolling.
		int row = 0, col = 0;
		for (InventoryItem i : mInventory.getItems()) {
			i.draw(c, x + PADDING_LEFT + col * COL_WIDTH, y + PADDING_TOP + row * ROW_HEIGHT);
			col++;
			if (col >= mCols) {
				col = 0;
				row++;
			}
		}
		
	}
}
