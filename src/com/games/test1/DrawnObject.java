package com.games.test1;

import java.util.HashMap;

import com.games.test1.aal.AALExecutionState;
import com.games.test1.astraal.ASTRAALResourceFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Abstract superclass of objects which are drawn by the
 * system; e.g., backgrounds, foreground objects, etc.
 *
 */
public abstract class DrawnObject {
	
	public static final String DO_NOT_DRAW_SPRITE_ID = "N/A";
	/** Most drawn objects will use an animation. If not, override the draw function. */
	protected Animation mAnimation;
	protected float mX;
	protected float mY;
	protected int mW = 1;
	protected int mH = 1;
	protected float mRot = 0.0f;
	protected Rect mClickZone;
	
	/** Identifier correlating with ID of ASTRAAL Object that this represents. */
	public String mID;
	
	/** Event handler for events for this object. */
	private EventHandler mEventHandler;
 	
	public static Paint basicPaint = new Paint();

	/**
	 * Constructor for all inheriting classes. Call with super(args).
	 */
	public DrawnObject() { }
	
	public DrawnObject(float mX, float mY, int mW, int mH) {
		this.mX = mX;
		this.mY = mY;
		this.mW = mW;
		this.mH = mH;	
		mClickZone = new Rect((int)mX,(int)mY,(int)mX+mW,(int)mY+mH);
		this.mAnimation = null;
	}
	
	// Constructor for an animation.
	public DrawnObject(Animation anim, float mX, float mY, int mW, int mH) {
		this(mX, mY, mW, mH);
		this.mAnimation = anim;
	}
	
	// Constructor for an animation that assumes width and height from sprite.
	public DrawnObject(Animation anim, float mX, float mY) {
		this(mX, mY, anim.getWidth(), anim.getHeight());
		mClickZone = new Rect((int)mX,(int)mY,(int)mX+mW,(int)mY+mH);
		this.mAnimation = anim;
	}

	// Single-frame constructor.
	public DrawnObject(Bitmap mBitmap, float mX, float mY, int mW, int mH) {		
		this(new Animation(mBitmap,1,mW,mH,1), mX, mY, mW, mH);		
	}
	
	// Create a new drawn object.
	public DrawnObject(Bitmap mBitmap, float mX, float mY, int mW, int mH, int frameCount, int FPS)
	{
		this(new Animation(mBitmap,frameCount,mW,mH,FPS), mX, mY, mW, mH);
	}
	
	
	
	/**
	 * Draw this object at its current position onto the canvas.
	 * A Canvas is transformation onto an attached bitmap. We don't
 	 * care which bitmap that is, but we need to remember to save and
	 * restore the Canvas before and after transforming it.
	 * 
	 * @param c - Canvas to draw onto.
	 * @param x - X position to draw at (can be different from mX due to camera, etc).
	 * @param y - Y position to draw at (can be different from mY due to camera, etc).
	 */
	public void draw(Canvas c, float x, float y) {
		if (mAnimation == null) return; // TODO: Refactor to subclass (or superclass).
		
		// Save current transform.
		c.save();
		// Apply a rotational transform if needed.		
		if (mRot != 0.0f) { 
			c.rotate(mRot, x + mW/2, y + mH/2);
		}
				
		mAnimation.draw(c, (int)x, (int)y, mW, mH);
				
		c.restore();
	}
	
	public void doOnClick(AALExecutionState execState)	{		
		// If we have an event handler, use it to respond.
		if (hasEventHandler()) {
			mEventHandler.respondTo(EventHandler.Type.OnClick, execState);
		}
	}
	
	public void doOnCombine(AALExecutionState execState) {		
		if (hasEventHandler()) {
			mEventHandler.respondTo(EventHandler.Type.OnCombine, execState);
		}
	}
	
	public void doOnLoad(AALExecutionState state) {
		if (hasEventHandler()) {
			mEventHandler.respondTo(EventHandler.Type.OnLoad, state);
		}
	}

	public void doOnEnter(AALExecutionState state) {
 	  if (hasEventHandler()) {
			mEventHandler.respondTo(EventHandler.Type.OnEnter, state);
		}
  }

	/**
	 * Update method. This is called by the Scene when it updates all objects it possesses.
	 * @return whether or not to remove this object.
	 */
	public boolean update() {
		return false;
	}
	
	private void updateRect() {
		this.mClickZone.set((int)mX,(int)mY,(int)mX+mW,(int)mY+mH);
		
	}

	public float getX() {
		return mX;
	}

	public void setX(float mX) {
		this.mX = mX;
		updateRect();
	}

	public float getY() {
		return mY;
	}

	public void setY(float mY) {
		this.mY = mY;
		updateRect();
	}

	public int getW() {
		return mW;
		
	}

	public void setW(int mW) {
		this.mW = mW;
		updateRect();
	}


	public int getH() {
		return mH;
	}


	public void setH(int mH) {
		this.mH = mH;
		updateRect();
	}


	public float getRot() {
		return mRot;
	}


	public void setRot(float mRot) {
		this.mRot = mRot;
	}


	public Rect getClickRect() {
		return mClickZone;
	}

	public String getID() {
		return mID;
	}
	
	public void setID(String id) {
		mID = id;
	}
	
	public void attachEventHandler(EventHandler eventHandler) {
		mEventHandler = eventHandler;		
	}
	
	public boolean hasEventHandler() {
		return mEventHandler != null;
	}

	public String getSpriteID() {
		if (mAnimation == null) return "";
		return mAnimation.getSpriteID();
	}
	
	public Animation getAnimation() {
		return mAnimation;
	}

	public void setAnimation(Animation animation) {
		mAnimation = animation;
	}

	public void setSprite(String spriteID) {
		mAnimation = ASTRAALResourceFactory.getAnimation(spriteID);		
	}




	
}
