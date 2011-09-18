package org.github.rnubel.miskatonic;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/** A struct-like class to hold mutable object data. */
public class ObjectData implements Parcelable, Serializable {
	private float mX;
	private float mY;
	private int mWidth;
	private int mHeight;
	private String mSpriteID, mObjectID;
	
	
	public static final Parcelable.Creator<ObjectData> CREATOR
			= new Parcelable.Creator<ObjectData>() {
		public ObjectData createFromParcel(Parcel in) {
			return new ObjectData(in);
		}

		public ObjectData[] newArray(int size) {
			return new ObjectData[size];
		}
	};

	public ObjectData(Parcel in) {		
		mX = in.readFloat();
		mY = in.readFloat();
		mWidth = in.readInt();
		mHeight = in.readInt();
		mSpriteID = in.readString();
		mObjectID = in.readString();
	}

	public ObjectData(float x, float y, int w, int h, String spr, String obj) {
		mX = x;
		mY = y;
		mWidth = w;
		mHeight = h;
		mSpriteID = spr;
		mObjectID = obj;
	}

	public ObjectData(DrawnObject drawnObject) {
		mX = drawnObject.getX();
		mY = drawnObject.getY();
		mWidth = drawnObject.getW();
		mHeight = drawnObject.getH();
		mSpriteID = drawnObject.getSpriteID();
		mObjectID = drawnObject.getID();
	}

	public void writeToParcel(Parcel p, int flags) {
		p.writeFloat(mX);
		p.writeFloat(mY);
		p.writeInt(mWidth);
		p.writeInt(mHeight);
		p.writeString(mSpriteID);
		p.writeString(mObjectID);
	}

	public float getX() {
		return mX;
	}

	public void setX(float mX) {
		this.mX = mX;
	}

	public float getY() {
		return mY;
	}

	public void setY(float mY) {
		this.mY = mY;
	}

	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	public String getSpriteID() {
		return mSpriteID;
	}

	public void setSpriteID(String mSpriteID) {
		this.mSpriteID = mSpriteID;
	}
	
	public void setObjectID(String objectID) {
		mObjectID = objectID;
	}

	public String getObjectID() {
		return mObjectID;
	}

	public int describeContents() {
		return 0;
	}




	
}
