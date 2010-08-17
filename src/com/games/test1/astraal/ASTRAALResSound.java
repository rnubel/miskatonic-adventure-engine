package com.games.test1.astraal;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.games.test1.Animation;
import com.games.test1.Resource;

import java.lang.reflect.Field;

/**
 * A sound resource template, which when loaded via loadResource
 * creates a Sound object. This can be obtained via getResource.
 */
public class ASTRAALResSound extends ASTRAALResource {


	public ASTRAALResSound(String mID) {
		this.mID = mID;
	}
	
	/**
	 * Read in the details of this resource from the attributes
	 * and/or children of the given node.
	 * @param node Node to pull data from.
	 */
	public void readFromNode(Node node) {		
		
				
		// We have all the info we need to make this animation, so we're done
		// until loadResource is called.
		ASTRAALResourceFactory.addByID(mID, this);
	}
	
	/**
	 * Actually load this resource.
	 * @param res - Resources context to use. 
	 */
	public void loadResource(Resources res) {		
		
	}

	
	
}
