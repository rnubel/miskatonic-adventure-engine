package org.github.rnubel.miskatonic;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/** Struct-like class to store the path to a journal page. */
public class JournalPagePath implements Parcelable, Serializable {
	public String journalID;
	public String pageID;

	public JournalPagePath(Parcel in) {
		journalID = in.readString();
		pageID = in.readString();
	}

	public JournalPagePath(String jid, String pid) {
		journalID = jid;
		pageID = pid;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void writeToParcel(Parcel p, int opts) {			
		p.writeString(journalID);
		p.writeString(pageID);
	}

	public static final Parcelable.Creator<JournalPagePath> CREATOR
		= new Parcelable.Creator<JournalPagePath>() {
		public JournalPagePath createFromParcel(Parcel in) {
			return new JournalPagePath(in);
		}

		public JournalPagePath[] newArray(int size) {
			return new JournalPagePath[size];
		}
	};

}