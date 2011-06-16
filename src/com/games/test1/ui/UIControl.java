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
	protected UIControl mParent;
	
	/** Our containing UI. */
	public GameUI mUI;
	
	/** A generic property for use as a flag. */
	public int userProperty1;
	
	/** Hook executed after being attached to a GameUI. */
	public void doAfterAttach() { }

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
	
	public GameThread getGameThread() {
		return mUI.getGameThread();
	}

	/** 
	 * Trigger a click event on this control. 
	 * TODO: Remove game as a parameters. Use getGameThread() instead.
	 * @param mouseX
	 * @param mouseY
	 * @return whether or not this click event should block further events. 
	 * */
	public boolean trigger(GameThread game, int mouseX, int mouseY) {
		return false;
	}

	public void draw(Canvas c, int x, int y) {
		c.drawRect(x, y, x + mWidth, y + mHeight, DrawnObject.basicPaint);		
	}
	
	public void addChild(UIControl child) {
		mChildren.add(child);
		child.mParent = this; // Establish back-reference.
	}
	
	public Vector<UIControl> getChildren() {
		return mChildren;
	}
	
	public UIControl getParent() {
		return mParent;
	}
}
