package com.games.test1;

import java.util.ConcurrentModificationException;
import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;


/**
 * Class used to draw objects in a scene (passed into it). 
 * The Camera maintains a current camera position.
 *
 */
public class Camera {
	private static final float CAMERA_SCROLL_SPEED = 5.0f;
	private float mX; //X position
	private float mY; //Y position
	private int mWidth; //Width
	private int mHeight; //Height
	private float mMag = 1.0f; //Magnification
	private float cRotation; // Rotation
	private Scene mScene;
	private CameraEffect mEffect;
	
	public Camera()	{
				mX =0; mY = 0;
				mWidth = 800; mHeight = 480;
				mMag = 1;
				mScene = null;
		
	}
	
	public Camera(float x, float y, int w, int h, Scene s)	{		
		mX = x;
		mY = y;
		mWidth = w;
		mHeight = h;
		mScene = s;
	}

	
	/**
	 * Draw all the objects in this scene.
	 * TODO: There is too much going on here. Refactor!
	 * @param s
	 * @param c
	 */
	public void drawScene(Canvas c)	{				
		// Scale?
		if (mMag != 1) {		
			c.scale(mMag, mMag);
		}
		
		// Rotate?
		if (cRotation != 0.0f) {
			c.rotate(cRotation);
		}
		
		// Draw objects.
		try {
			Vector<DrawnObject> lObjsToDraw = mScene.getAllObjects();
			for (DrawnObject lDrawn : lObjsToDraw) {		 
				float lX = (lDrawn.getX() - mX);
				float lY = (lDrawn.getY() - mY);
				lDrawn.draw(c, lX, lY);
			}
			
			// Draw effects on top.		
			mScene.doQueues();
			Vector<Effect> lEffects = mScene.getAllEffects();
			for (Effect e : lEffects) {
				e.draw(c, e.getX() - mX, e.getY() - mY);
			}
		} catch (ConcurrentModificationException e) {
			
		}
		
		// Draw (camera) effect?
		if (mEffect != null) {
			mEffect.draw(c);
			if (mEffect.update()) {
				mEffect = null;
			}
		}
	}
	


	/**
	 * Center the camera around the given point, as much as possible
	 * given the current magnification. x and y are points in scene-
	 * space.
	 * @param sX - x coordinate IN THE SCENE.
	 * @param sY - y coordinate IN THE SCENE.
	 */
	public void centerAt(int sX, int sY) {
		int cX = sX, cY = sY;
		
		// TODO: REPLACE WITH fitToBounds() call
		// First, correct for going off to the right or down.
		int right = cX + getMagnifiedWidth()/2, bottom = cY + getMagnifiedHeight()/2;
		cX -= Math.max(0, right - mScene.getWidth() + 1);
		cY -= Math.max(0, bottom - mScene.getHeight() + 1);
				
		// Then, correct for going off to the top and left.
		int left = cX - getMagnifiedWidth()/2, top = cY - getMagnifiedHeight()/2;
		cX += Math.max(0, -left);
		cY += Math.max(0, -top);
		
		// Adjust magnification to fit our bounds. Note: this will stretch horizontally.
	//	int actualWidth = right - left;
	//	setMag(getWidth() / actualWidth);
		
		mX = cX - getMagnifiedWidth()/2;
		mY = cY - getMagnifiedHeight()/2;
	}
	
	/**
	 * Correct the camera to stay within scene bounds.
	 */
	public void fitToBounds() {
		// First, correct for going off to the right or down.
		int right = (int) (mX + getMagnifiedWidth()), bottom = (int) (mY + getMagnifiedHeight());
		mX -= Math.max(0, right - mScene.getWidth());
		mY -= Math.max(0, bottom - mScene.getHeight());
				
		// Then, correct for going off to the top and left.
		int left = (int) (mX), top = (int) (mY);
		mX += Math.max(0, -left);
		mY += Math.max(0, -top);		
	}
	
	/** Move the camera towards the given point. */
	public void moveTowards(int x, int y) {		
		PointF newPos = Utility.moveTowards(getCenterX(), getCenterY(), x, y, CAMERA_SCROLL_SPEED);
		mX = newPos.x - mWidth/2;
		mY = newPos.y - mHeight/2;
		
		fitToBounds();
	}
	
	
	/** Return how much scene-space our width takes. */
	public int getMagnifiedWidth() {
		return (int)((float)mWidth / mMag);
	}
	
	public int getMagnifiedHeight() {
		return (int)((float)mHeight / mMag);
	}
	
	/** Return center coordinates. */
	public int getCenterX() {
		return (int) (mX + getMagnifiedWidth()/2);
	}
	
	public int getCenterY() {
		return (int) (mY + getMagnifiedHeight()/2);
	}
	
	//Restricts movement to within scene bounds.
	public void moveBy(float x, float y) {
		mX = Math.min((mX+x), mScene.getWidth() - mWidth);
		mY = Math.min((mY+y), mScene.getHeight() - mHeight);
		if (mX < 0) mX = 0;
		if (mY < 0) mY = 0;
	}
	
	public Scene getScene()	{
		return mScene;
	}
	
	public void setScene(Scene s)	{
		mScene = s;
	}

	/**
	 * Covert absolute coordinates to real coordinates.
	 * @param objX
	 * @return
	 */
	public float getRelativeX(float objX) {		
		return (objX - mX) * mMag;		
	}
	
	public float getRelativeY(float objY) {		
		return (objY - mY) * mMag;				
	}
	
	public int getAbsoluteX(int camX) {
		return (int) (mX + camX / mMag);
	}
	
	public int getAbsoluteY(int camY) {
		return (int) (mY + camY / mMag);
	}
	
	public float getX()
	{
		return mX;
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	public float getY()
	{
		return mY;
	}
	
	public void setWidth(int w) {
		mWidth = w;
	}
	
	public void setHeight(int h) {
		mHeight = h;
	}
	
	public void setMag(float mag) {
		mMag = mag;
	}
	
	public void setRotation(float rot) {
		cRotation = rot;
	}

	public float getMag() {
		return mMag;
	}
	
	public float getRotation() {
		return cRotation;
	}

	public void setX(int x) {
		mX = x;		
	}

	public void setY(int y) {
		mY = y;		
	}

	public void setEffect(CameraEffect effect) {
		mEffect = effect;
	}	
	
}
