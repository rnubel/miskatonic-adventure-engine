package org.github.rnubel.miskatonic.astraal;

/**
 * A page of a journal.
 */
public class ASTRAALJournalPage {
	private String mID;
	private String mTitle;
	private String mText;
	private boolean mUnlocked;
	
	public ASTRAALJournalPage(String id, String title, String text) {
		mID = id;
		mTitle = title;
		mText = text.trim();
		mUnlocked = false;
	}

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

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text.trim();
	}

	public void setUnlocked(boolean unlocked) {
		mUnlocked = unlocked;
	}

	public boolean isUnlocked() {
		return mUnlocked;
	}
}
