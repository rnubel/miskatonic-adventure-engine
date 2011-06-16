package com.games.test1.astraal;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.games.test1.Animation;
import com.games.test1.Resource;

import java.lang.reflect.Field;

/**
 * An animation resource template, which when loaded via loadResource
 * creates an Animation object. This can be obtained via getResource.
 */
public class ASTRAALResAnimation extends ASTRAALResource {
	private int mFPS;	
	private int mWidth;
	private int mHeight;
	private int mFrameCount;
	private int mImageID;
	private String mSpriteID;

	public ASTRAALResAnimation(String mID) {
		this.mID = mID;
	}
	
	/**
	 * Read in the details of this resource from the attributes
	 * and/or children of the given node.
	 * @param node Node to pull data from.
	 */
	public void readFromNode(Node node) {		
		// First, get the basic data for this animation.
		String 	sFPS = ASTRAALParser.getAttributeValue(node, "fps", "1"),
				sWidth = ASTRAALParser.getAttributeValue(node, "width", "1"),
				sHeight = ASTRAALParser.getAttributeValue(node, "height", "1"),
				sFrameCount = ASTRAALParser.getAttributeValue(node, "framecount", "1");
		mSpriteID = ASTRAALParser.getAttributeValue(node, "id", "");
		mFPS = Integer.parseInt(sFPS);
		mWidth = Integer.parseInt(sWidth);
		mHeight = Integer.parseInt(sHeight);
		mFrameCount = Integer.parseInt(sFrameCount);
		
		// Next, get the internal content of the node, which should be the
		// name of the imagestrip. [For later versions: add multiple formats.]
		String frameName = ASTRAALParser.getNodeContents(node);
		mImageID = ASTRAALResource.getDrawableID(frameName.trim());
		
		// We have all the info we need to make this animation, so we're done
		// until loadResource is called.
		ASTRAALResourceFactory.addByID(mID, this);
	}
		
	
	/**
	 * Actually load this resource.
	 * @param res - Resources context to use. 
	 */
	public void load(Resources res) {		
		super.load(res);
		
		Log.w("Miskatonic", "Loading resource: " + mSpriteID);
		Bitmap bmp = BitmapFactory.decodeResource(res, mImageID, null);
		mResource = new Animation(bmp, mFrameCount, mWidth, mHeight, mFPS);
		((Animation) mResource).setSpriteID(mSpriteID);
	}
	
	public void unload() {
		super.unload();
		
		Log.w("Miskatonic", "Unloading resource: " + mSpriteID);
		Animation temp = ((Animation) mResource);
		mResource = null;
		temp.getBitmap().recycle();
		
	}

	public int getEstimatedSize() {
		return mResource.getEstimatedSize();	
	}

	
	public int getFPS() {
		return mFPS;
	}

	public void setFPS(int mFPS) {
		this.mFPS = mFPS;
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
}
