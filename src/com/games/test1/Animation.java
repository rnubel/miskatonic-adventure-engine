package com.games.test1;

import java.util.HashMap;

import com.games.test1.aal.AALValue;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Animation extends Resource {
  	private Bitmap mBitmap;
    private int mFrameNum;
    private int mFrameCount;
    private int mWidth;
    private int mHeight;
    private long mLastChange;
    private long mDelta; 
    //in seconds
    private int mFPS;
    Rect mFrameRect;
	private String mSpriteID;	    
    
	/** Testing constructor. */
	public Animation() {
		super();
		this.mWidth = 1;
		this.mHeight = 1;
	}
	
    public Animation(Bitmap bitmap, int mFrameMax, int width,
			int height, int FPS) {
		super();
					
		this.mFrameCount = mFrameMax;
		this.mFrameNum = 0;
		this.mWidth = width;
		this.mHeight = height;			
		
		this.mBitmap = Bitmap.createScaledBitmap(bitmap, width*mFrameMax, height, true);			
		
		mFrameRect = new Rect(mWidth*mFrameNum, 0, mWidth*(mFrameNum+1),
           mHeight);
		mLastChange = System.currentTimeMillis();
			
		setFPS(FPS);
	}

	public void update()
    {
		if (mFrameCount == 1 || mDelta == 0) return;
		
    	long curTime = System.currentTimeMillis();
    	
    	//Edge case: timer has reset
    	if (curTime < mLastChange) mLastChange = curTime; 

    	if ((curTime - mLastChange) >= mDelta)
    	{
    		mLastChange = curTime;
    		mFrameNum = (++mFrameNum) % mFrameCount;
    		mFrameRect = new Rect(mWidth*mFrameNum, 0, mWidth*(mFrameNum+1), mHeight); 
    	}    
    }
	
	/** Draw this animation to the screen. */	
	public void draw(Canvas c, int x, int y) {
		draw(c, x, y, mWidth, mHeight);
	}
	
	/** Draw this animation to the screen. */
	public void draw(Canvas c, int x, int y, int w, int h) {
		Rect dst = new Rect((int)x,(int)y,(int)(x+w),(int)(y+h));
		c.drawBitmap(mBitmap, mFrameRect, dst, DrawnObject.basicPaint);
		update(); 
	}
	
    public Rect getFrame() {     
        return mFrameRect;
    }

    
	public Bitmap getBitmap() {
		return mBitmap;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}
	public int getFPS() {
		return mFPS;
	}	
	
	public void setFPS(int FPS) {
		mFPS = FPS;
		if (mFPS == 0) {
			mDelta = 0;
		} else {
			mDelta = 1000/mFPS;
		}
	}
	
	public void setFrameNum(int fNum) {
		mFrameNum = fNum;
	}

	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}

	public void setSpriteID(String imageID) {
		mSpriteID = imageID;		
	}
	
	public String getSpriteID() {
		return mSpriteID;
	}

	public int getFrameNum() {
		return mFrameNum;
	}
}
