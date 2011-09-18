package org.github.rnubel.miskatonic.astraal;

import java.util.HashMap;
import java.util.Vector;

/**
 * A Journal is a collection of text pages; it can be used either
 * as a character's literal journal, or as a compendium for found
 * notes, books, etc.
 */
public class ASTRAALJournal {
	private String mID;
	private String mTitle;
	private Vector<ASTRAALJournalPage> mPages;
	private HashMap<String, ASTRAALJournalPage> mPageMap = new HashMap<String, ASTRAALJournalPage>();

	/**
	 * Make a new, empty journal.
	 * @param id ID to reference this journal by in AAL.
	 * @param title Title to display to the user for this journal.
	 */
	public ASTRAALJournal(String id, String title) {		
		mID = id;
		mTitle = title;
		mPages = new Vector<ASTRAALJournalPage>();		
	}

	/** Add a page to this journal. */
	public void addPage(ASTRAALJournalPage page) {		
		mPages.add(page);
		mPageMap.put(page.getID(), page);		
	}
	
	/** Look up a page. */
	public ASTRAALJournalPage getPageByID(String id) {
		return mPageMap.get(id);
	}
	
	// Accessors:
	
	public String getID() {
		return mID;
	}

	public void setID(String iD) {
		mID = iD;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Vector<ASTRAALJournalPage> getPages() {
		return mPages;
	}

	public void setPages(Vector<ASTRAALJournalPage> pages) {
		mPages = pages;
	}


	

}
