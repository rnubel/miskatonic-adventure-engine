package com.games.test1.astraal;

import org.w3c.dom.Node;

import android.content.res.Resources;

import com.games.test1.SoundClip;

/**
 * A sound resource template, which when loaded via loadResource
 * creates a Sound object. This can be obtained via getResource.
 */
public class ASTRAALResSound extends ASTRAALResource {
	private int mSoundID;

	/**
	 * Initialize the resource with an ID.
	 */
	public ASTRAALResSound(String mID) {
		this.mID = mID;
	}
	
	/**
	 * Read in the details of this resource from the attributes
	 * and/or children of the given node.
	 * @param node Node to pull data from.
	 */
	public void readFromNode(Node node) {
		// Get a handle to the sound path.
		String resourceName = ASTRAALParser.getNodeContents(node);
		mSoundID = ASTRAALResource.getRawID(resourceName.trim());
				
		// We have all the info we need to make this animation, so we're done
		// until loadResource is called.
		ASTRAALResourceFactory.addByID(mID, this);
	}
	
	/**
	 * Actually load this resource.
	 * @param res - Resources context to use. 
	 */
	public void loadResource(Resources res) {		
		mResource = new SoundClip(mID, res.openRawResourceFd(mSoundID));
	}

	
}
