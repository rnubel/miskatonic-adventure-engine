package org.github.rnubel.miskatonic.astraal;

/** The ASTRAAL template for an inventory item. */
public class ASTRAALInventoryItem {
	private String mID;
	private String mName;
	private String mDescription;
	private String mSpriteID;
	
	public ASTRAALInventoryItem(String id, String name, String description, String spriteID) {
		mID = id;
		mName = name;
		mDescription = description;
		mSpriteID = spriteID;
	}

	
	/**
	 * @return the ID
	 */
	public String getID() {
		return mID;
	}

	/**
	 * @param id the ID to set
	 */
	public void setID(String id) {
		mID = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		mDescription = description;
	}


	public String getSpriteID() {
		return mSpriteID;
	}


	public void setSpriteID(String spriteID) {
		mSpriteID = spriteID;
	}
}
