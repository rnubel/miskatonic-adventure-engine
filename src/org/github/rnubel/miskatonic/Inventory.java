package org.github.rnubel.miskatonic;

import java.util.HashMap;
import java.util.Vector;

import org.github.rnubel.miskatonic.astraal.ASTRAALInventoryItem;

import android.os.Bundle;


/** 
 * Manages the player's inventory, which contains items (not to be confused with objects, which
 * are in-game interaction points. An object in a scene does not have any connection to items,
 * though clicking on an object may result via an AAL hook in that object disappearing and a
 * similar item appearing in one's inventory, but this is not a hard-coded relationship. */
public class Inventory {
	private static final String KEY_INVENTORY_ITEMS = "inventoryItems";
	private static final String KEY_INVENTORY = "inventory";
	private Vector<InventoryItem> mItems = new Vector<InventoryItem>();
	private HashMap<String, InventoryItem> mItemMap = new HashMap<String, InventoryItem>(); 
	
	/** Add an item to this inventory. */
	public void addItem(InventoryItem item) {
		mItems.add(item);
		mItemMap.put(item.getID(), item);
	}
	
	/** Add an item to this inventory from an ASTRAAL template. */
	public void addItem(ASTRAALInventoryItem item) {
		addItem(new InventoryItem(item));		
	}
	
	/** Remove an item from the inventory. */
	public void removeItem(InventoryItem item) {
		mItems.remove(item);
	}
	
	/** Return our items. */
	public Vector<InventoryItem> getItems() {
		return mItems;
	}
	
	/** Returns true if we have the given item. */
	public boolean hasItem(String itemID) {
		// TODO: This is O(n) for items times O(m) for
		// string comparisons -- replace with a hash
		// map to get O(1) performance. Not critical.
		for (InventoryItem i : mItems) {
			if (i.getID().equals(itemID)) {
				return true;
			}
		}
		
		return false;
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

	public String getItemName(String itemID) {
		if (mItemMap.containsKey(itemID)) {
			return mItemMap.get(itemID).getName();
		} else {
			return "unknown";
		}
	}


 }
