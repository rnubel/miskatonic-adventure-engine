package com.games.test1.astraal;

import java.lang.reflect.Field;

import org.w3c.dom.Node;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.games.test1.*;

/**
 * Wraps around a Resource, allowing us to load that resource only
 * when needed.
 */
public class ASTRAALResource {
	protected Resource mResource;
	protected String mID;
	protected boolean mIsLoaded;

	/**
	 * Read in the details of this resource from the attributes
	 * and/or children of the given node.
	 * @param resource
	 */
	public void readFromNode(Node resource) {	
		
	}
	
	/** Load this resource's actual resource into being. */
	public void load(Resources res) {
		mIsLoaded = true;
	}
	
	/** Unload this resource and free up space. */
	public void unload() {
		mIsLoaded = false;
	}
	
	/** Return this resource's actual resource. */
	public Resource getResource() {
		return mResource;
	}
	
	/** Estimate the size of this resource. Should be an upper-bound. */
	public int getEstimatedSize() {
		return 0;		
	}
	
	/** Whether or not this resource is currently loaded. */
	public boolean isLoaded() {
		return mIsLoaded;
	}
	
	/**
	 * Dynamically find the ID of a drawable resource.
	 * @param basename - Name of drawable resource.
	 */
	public static int getDrawableID(String basename) {
		Class c = R.drawable.class;
		try {
			Field f = c.getDeclaredField(basename);
			return f.getInt(null);
		} catch (SecurityException e) {
			return 0; // We screwed yo.
		} catch (NoSuchFieldException e) {
			return 0;
		} catch (IllegalArgumentException e) {
			return 0;
		} catch (IllegalAccessException e) {
			return 0;
		}		
	}
	
	public static int getRawID(String basename) {
		Class c = R.raw.class;
		try {
			Field f = c.getDeclaredField(basename);
			return f.getInt(null);
		} catch (SecurityException e) {
			return 0; // We screwed yo.
		} catch (NoSuchFieldException e) {
			return 0;
		} catch (IllegalArgumentException e) {
			return 0;
		} catch (IllegalAccessException e) {
			return 0;
		}	
	}


	public String getID() {
		return mID;
	}






}
