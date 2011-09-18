package org.github.rnubel.miskatonic;

import android.os.Bundle;

public class JournalStatus {
	private static final String KEY_JOURNAL_PAGES = "unlockedJournalPages";
	public JournalPagePath[] unlockedPages;
	private int mSize = 16;
	private int mIndex = 0;
	
	public JournalStatus() {		
		unlockedPages = new JournalPagePath[mSize]; 
	}
	
	public void saveToBundle(Bundle b) {
		b.putParcelableArray(KEY_JOURNAL_PAGES, unlockedPages);
	}
	
	public void loadFromBundle(Bundle b) {
		unlockedPages = (JournalPagePath[]) b.getParcelableArray(KEY_JOURNAL_PAGES);
	}
	
	public void addUnlockedPage(String jid, String pid) {
		// TODO: Refactor to a vector.
		if (mIndex >= mSize) {
			mSize *= 2;
			JournalPagePath[] newarr = new JournalPagePath[mSize];
			System.arraycopy(unlockedPages, 0, newarr, 0, mSize/2);
			unlockedPages = newarr;
		}
		
		unlockedPages[mIndex++] = new JournalPagePath(jid, pid); 
	}

	public JournalPagePath[] getUnlockedPages() {
		return unlockedPages;
	}
}
