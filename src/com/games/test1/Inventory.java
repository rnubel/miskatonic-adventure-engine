package com.games.test1;

import java.util.Vector;

import android.os.Bundle;

import com.games.test1.astraal.ASTRAALInventoryItem;

/** 
 * Manages the player's inventory, which contains items (not to be confused with objects, which
 * are in-game interaction points. An object in a scene does not have any connection to items,
 * though clicking on an object may result via an AAL hook in that object disappearing and a
 * similar item appearing in one's inventory, but this is not a hard-coded relationship. */
public class Inventory {
	private static final String KEY_INVENTORY_ITEMS = "inventoryItems";
	private static final String KEY_INVENTORY = "inventory";
	private Vector<InventoryItem> mItems = new Vector<InventoryItem>();	
	
	/** Add an item to this inventory. */
	public void addItem(InventoryItem item) {
		mItems.add(item);
	}
	
	/** Add an item to this inventory from an ASTRAAL template. */
	public void addItem(ASTRAALInventoryItem item) {
		this.addItem(new InventoryItem(item));
	}
	
	/** Remove an item from the inventory. */
	public void removeItem(InventoryItem item) {
		mItems.remove(item);
	}
	
	/** Return our items. */
	public Vector<InventoryItem> getItems() {
		return mItems;
	}
	
	/** Save state to a bundle. 
	 *  @param b - Bundle to save to. */
	public void saveToBundle(Bundle b) {
		// Put in a sub-bundle to avoid conflicts.
		Bundle ib = new Bundle();
	
		// Collect item IDs into vector.
		Vector<String> itemIDs = new Vector<String>();
		for (InventoryItem i : mItems) {
			itemIDs.add(i.getID());			
		}
		// Convert vector into an array, which we can parcel.
		String[] itemIDsArray = new String[mItems.size()];
		itemIDs.toArray(itemIDsArray);
		
		ib.putStringArray(KEY_INVENTORY_ITEMS, itemIDsArray);
		
		// Save the sub-bundle to the parent bundle.
		b.putBundle(KEY_INVENTORY, ib);
	}
	
	/** Load state from a bundle. 
	 *  @param executor - Instance of GameExecutor, needed to load
	 *  item data. 
	 *  @param b - Bundle to load from. */
	public void loadFromBundle(Bundle b, GameExecutor executor) {
		Bundle ib = b.getBundle(KEY_INVENTORY);
		String[] itemIDsArray = ib.getStringArray(KEY_INVENTORY_ITEMS);
		for (String itemID : itemIDsArray) {
			executor.giveItemToPlayer(itemID);
		}
	}
 }
