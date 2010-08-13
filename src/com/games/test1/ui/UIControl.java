package com.games.test1.ui;

import java.util.Vector;

import android.graphics.Canvas;

import com.games.test1.DrawnObject;
import com.games.test1.GameView;
import com.games.test1.GameView.GameThread;

/**
 * A UI control.
 */
public class UIControl {
	protected int mWidth;
	protected int mHeight;	
	protected boolean mShouldRemove = false;
	
	/** Controls that get removed when we do. */
	protected Vector<UIControl> mChildren = new Vector<UIControl>();

	public void removeSelf() {
		mShouldRemove = true;
		
		// Remove our children, too.
		for (UIControl child : mChildren) {
			child.removeSelf();
		}
	}
	
	public int getWidth() {
		// TODO Auto-generated method stub
		return mWidth;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return mHeight;
	}

	/** 
	 * Trigger a click event on this control. 
	 * @param mouseX
	 * @param mouseY
	 * */
	public void trigger(GameThread game, int mouseX, int mouseY) {
		
	}

	public void draw(Canvas c, int x, int y) {
		c.drawRect(x, y, x + mWidth, y + mHeight, DrawnObject.basicPaint);		
	}
	
	public void addChild(UIControl child) {
		mChildren.add(child);
	}
	
	public Vector<UIControl> getChildren() {
		return mChildren;
	}
}
