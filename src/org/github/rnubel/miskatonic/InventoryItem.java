package org.github.rnubel.miskatonic;

import org.github.rnubel.miskatonic.astraal.ASTRAALInventoryItem;
import org.github.rnubel.miskatonic.astraal.ASTRAALResourceFactory;
import org.github.rnubel.miskatonic.ui.UIControlInventory;

import android.graphics.Canvas;


/** An item in the player's inventory. */
public class InventoryItem {
	private Animation mSprite;
	private String mID, mName, mDescription;
	
	public static final int SIZE_ICON = 1;
	public static final int SIZE_BIG = 2;
	
	/** Construct an inventory item from the item template object. */
	public InventoryItem(ASTRAALInventoryItem item) {
		mSprite = ASTRAALResourceFactory.getAnimation(item.getSpriteID());
		mID = item.getID();
		mName = item.getName();
		mDescription = item.getDescription();
	}
	
	/** Draw this inventory item at the given coordinates. */
	public void draw(Canvas c, int x, int y) {
		this.draw(c, x, y, SIZE_ICON);		
	}
	
	public void draw(Canvas c, int x, int y, int size) {
		int w = UIControlInventory.DEFAULT_ITEM_WIDTH,
			h = UIControlInventory.DEFAULT_ITEM_HEIGHT;
		if (size == SIZE_BIG) {
			w *= 2;
			h *= 2;
			// Uh... also center it.
			x -= w/2;
			y -= h/2;
		}
		mSprite.draw(c, x, y, w, h);
	}

	public Animation getSprite() {
		return mSprite;
	}

	public void setSprite(Animation sprite) {
		mSprite = sprite;
	}

	public String getID() {
		return mID;
	}

	public void setID(String iD) {
		mID = iD;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}
}
