package com.games.test1.astraal;

import com.games.test1.EventHandler;

/**
 * An object in a room. 
 *
 */
public class ASTRAALObject {
	private String mID;
	private int mX, mY, mWidth, mHeight;
	private String mSpriteID;
	private EventHandler mEventHandler;
		
	/**
	 * Construct a new object with basic parameters.
	 * @param mID
	 * @param mX
	 * @param mY
	 * @param mWidth
	 * @param mHeight
	 * @param spriteID
	 */
	public ASTRAALObject(String mID, int mX, int mY, int mWidth, int mHeight,
			String spriteID) {
		super();
		this.mID = mID;
		this.mX = mX;
		this.mY = mY;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		this.mSpriteID = spriteID;
	}

	public String getID() {
		return mID;
	}
	
	public int getX() {
		return mX;
	}

	public void setX(int mX) {
		this.mX = mX;
	}

	public int getY() {
		return mY;
	}

	public void setY(int mY) {
		this.mY = mY;
	}

	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	public String getSpriteID() {
		return mSpriteID;
	}

	public void setSpriteID(String mSpriteID) {
		this.mSpriteID = mSpriteID;
	}

	public void attachEventHandler(EventHandler handler) {
		this.mEventHandler = handler;		
	}
	
	public EventHandler getEventHandler() {
		return mEventHandler;
	}

}
