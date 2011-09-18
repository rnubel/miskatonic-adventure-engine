package org.github.rnubel.miskatonic.astraal;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;




import android.content.res.Resources;

/**
 * Root of an ASTRAAL tree, which contains:
 * <ul>
 *  <li>Basic metadata
 *	<li>A list of resources
 *	<li>A list of rooms
 * </ul>
 */
public class ASTRAALRoot {
	// Basic data.
	private String mGameName;
	private String mVersion;

	// Raw storage vectors for traversal.
	private Vector<ASTRAALResourceBlock> mResourceBlocks;
	private Vector<ASTRAALJournal> mJournals;
	private Vector<ASTRAALInventoryItem> mItems;
	private Vector<ASTRAALScene> mScenes;
	
	
	// Maps to facilitate faster ID-to-object lookups.
	private Map<String, ASTRAALResourceBlock> mResourceBlockMap;
	private Map<String, ASTRAALInventoryItem> mItemMap;
	private Map<String, ASTRAALJournal> mJournalMap;
	private Map<String, ASTRAALScene> mSceneMap;
	
	
	// Reality-tethering fields.
	private Resources mResources;
	
	// Cache.
	private ASTRAALResourceBlock mResourceBlockCurrentlyLoaded;
	
	
	/**
	 * Construct an ASTRAAL root with metadata info.
	 * @param name - Name of the game being defined.
	 * @param version - What version of ASTRAAL is being used.
	 */
	public ASTRAALRoot(String name, String version) {
		mGameName = name;
		mVersion = version; // May be used down the line.
		
		mResourceBlocks = new Vector<ASTRAALResourceBlock>();
		mResourceBlockMap = new HashMap<String, ASTRAALResourceBlock>();
		mItems = new Vector<ASTRAALInventoryItem>();
		mItemMap = new HashMap<String, ASTRAALInventoryItem>();
		mJournals = new Vector<ASTRAALJournal>();
		mJournalMap = new HashMap<String, ASTRAALJournal>();
		mScenes = new Vector<ASTRAALScene>();
		mSceneMap = new HashMap<String, ASTRAALScene>();
	}

	/**
	 * Attach this ASTRAALRoot to necessary Android resources,
	 * most notably the Resources object we'll need to load
	 * in our resources.
	 */
	public void attachToReality(Resources res) {
		mResources = res;
	}
	
	/**
	 * Add the specified block to the root's list of blocks.
	 * @param block
	 */
	public void addResourceBlock(ASTRAALResourceBlock block) {
		mResourceBlocks.add(block);
		
		// Add to our map for faster reference.
		mResourceBlockMap.put(block.getID(), block);
	}

	/**
	 * Add the item to this root's list of items.
	 * @param item
	 */
	public void addItem(ASTRAALInventoryItem item) {
		mItems.add(item);
		mItemMap.put(item.getID(), item);
	}
	
	/**
	 * Add a room to the root's list of scenes.
	 * @param scene 
	 */
	public void addScene(ASTRAALScene scene) {
		mScenes.add(scene);
		mSceneMap.put(scene.getID(), scene);
	}
	
	/**
	 * Add the journal to the root's list of journals.
	 * @param newJournal
	 */
	public void addJournal(ASTRAALJournal newJournal) {
		mJournals.add(newJournal);
		mJournalMap.put(newJournal.getID(), newJournal);
	}
	
	/**
	 * Load the given scene's needed resources into memory.  
	 */
	public void loadResourcesFor(ASTRAALScene scene) {
		// Each scene has a single resource block that contains its resources. Load
		// that block, unless we already have.		
		ASTRAALResourceBlock block = getResourceBlockByID(scene.getResourceBlockID());
				
		// Do we need to load up a different block?
		//if (mResourceBlockCurrentlyLoaded != block) {
		//			block.loadAllResources(mResources);			
		//	}
		
		// Make sure we have the resources we need loaded.
		Vector<String> neededResourceIDs = new Vector<String>();
		// Need scene BG...
		neededResourceIDs.add(scene.getBackgroundID());
		// Need objects...
		for (ASTRAALObject obj : scene.getObjects()) {
			if (!obj.getSpriteID().equals("")) {
				neededResourceIDs.add(obj.getSpriteID());
			}
		}
		// Need items in inventory...
		for (ASTRAALInventoryItem item : getItems()) {
			neededResourceIDs.add(item.getSpriteID());
		}
		
		// Do an n^2 pass to ensure every needed resource is loaded.
		for (ASTRAALResource res : block.getResources()) {
			if (neededResourceIDs.contains(res.getID())) {
				if (!res.isLoaded())
					res.load(mResources);
			} else if (res.isLoaded()){
				res.unload();
			}
		}	
		
		// The background should be in there, so find it for quick reference.
		scene.setBackground(block.getResourceByID(scene.getBackgroundID()));
		
		// Also, attach this resource block to the scene.
		scene.setResourceBlock(block);
		
		mResourceBlockCurrentlyLoaded = block;
	}

	/** Look up a resource block by id. */
	public ASTRAALResourceBlock getResourceBlockByID(String id) {
		return mResourceBlockMap.get(id);
	}
	
	/** Look up a scene by id. */
	public ASTRAALScene getSceneByID(String id) {
		return mSceneMap.get(id);
	}
	
	/** Look up an inventory item by id. */
	public ASTRAALInventoryItem getItemById(String itemID) {
		return mItemMap.get(itemID);
	}
	
	public String getGameName() {
		return mGameName;
	}

	public void setGameName(String mGameName) {
		this.mGameName = mGameName;
	}

	public String getVersion() {
		return mVersion;
	}

	public void setVersion(String mVersion) {
		this.mVersion = mVersion;
	}


	public Vector<ASTRAALResourceBlock> getResourceBlocks() {
		return mResourceBlocks;
	}


	public Vector<ASTRAALScene> getScenes() {
		return mScenes;
	}

	/**
	 * Return the first scene in the game.
	 */
	public ASTRAALScene getFirstScene() {
		return mScenes.get(0);
	}

	public Vector<ASTRAALInventoryItem> getItems() {
		return mItems;
	}

	public Vector<ASTRAALJournal> getJournals() {
		return mJournals;
	}

	public ASTRAALJournal getJournalByID(String id) {
		// TODO Auto-generated method stub
		return mJournalMap.get(id);
	}

	




}
