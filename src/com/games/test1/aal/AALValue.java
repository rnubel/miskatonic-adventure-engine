package com.games.test1.aal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Polymorphic type to hold values. Can be parceled.
 */
public class AALValue implements Parcelable {
	public enum Type {
		Int,
		Float,
		String,
		Bool
	}
		
	private Object mValue;
	private Type mType;

	
	public int describeContents() { return 0; }

	/** Write this value to a parcel. */
	public void writeToParcel(Parcel out, int flags) {
		// Write an integer version of our Type.
		out.writeInt(getTypeID());
		// Now, write our type.
		out.writeValue(mValue);
	}

	/** Read this value from a parcel. */
	public static final Parcelable.Creator<AALValue> CREATOR =
  	new Parcelable.Creator<AALValue>() {
		public AALValue createFromParcel(Parcel in) {
			return new AALValue(in);
		}

		public AALValue[] newArray(int arg0) {
			return new AALValue[arg0];
		}
	};
	
	public AALValue(Parcel in) {
		// Read the type.
		int type = in.readInt();
		mType = getTypeFromID(type);
		// Create the right type of value.		
		switch (mType) {
			case Int: mValue = (Integer) in.readInt(); break;
			case Float: mValue = (Float) in.readFloat(); break;
			case Bool: mValue =  (Boolean) in.readValue(ClassLoader.getSystemClassLoader()); break;
			case String: mValue = (String) in.readString(); break;
		}	
	}
	
	/** Initialize an integer value. */
	public AALValue(Integer val) {
		mValue = val;
		mType = Type.Int;	
	}
	
	/** Initialize a float value. */
	public AALValue(Float val) {
		mValue = val;
		mType = Type.Float;	
	}
	
	/** Initialize a string value. */
	public AALValue(String val) {
		mValue = val;
		mType = Type.String;	
	}
	
	/** Initialize a float value. */
	public AALValue(Boolean val) {
		mValue = val;
		mType = Type.Bool;	
	}
	
	/** Initialize a null value. */
	public AALValue() {
		mType = Type.Int;
		mValue = 0;
	}

	/** Return the type of this value. */
	public Type getType() {
		return mType;
	}

	/** Return an integer Type. */
	public int getTypeID() {
		switch (mType) {
			case Int: return 0;
			case Float: return 1;
			case Bool: return 2;
			case String: return 3;
			default: return 0;
		}
	}

	/** Convert an integer type to a Type. */
	public static Type getTypeFromID(int id) {
		switch (id) {
			case 0: return Type.Int;
			case 1: return Type.Float;
			case 2: return Type.Bool;
			case 3: return Type.String;
			default: return Type.Int;
		}
	}

	/** Return an uncasted value. */
	public Object getValue() {
		return mValue;
	}
	
	/** Operator to add. */
	public AALValue plus(AALValue other) {
		switch (mType) {
		case Int:
			return new AALValue((Integer)mValue + (Integer)other.getValue());
		case Float:
			return new AALValue((Float)mValue + (Float)other.getValue());
		case String:
			return new AALValue((String)mValue + (String)other.getValue());
		}
		
		// Failsafe case.
		return this;
	}
	
	/** Operator to subtract. */
	public AALValue minus(AALValue other) {
		switch (mType) {
		case Int:
			return new AALValue((Integer)mValue - (Integer)other.getValue());
		case Float:
			return new AALValue((Float)mValue - (Float)other.getValue());		
		}
		
		// Failsafe case.
		return this;
	}
	
	public AALValue divide(AALValue other) {
		switch (mType) {
		case Int:
			return new AALValue((Integer)mValue / (Integer)other.getValue());
		case Float:
			return new AALValue((Float)mValue / (Float)other.getValue());		
		}
		
		// Failsafe case.
		return this;
	}

	public AALValue times(AALValue other) {
		switch (mType) {
		case Int:
			return new AALValue((Integer)mValue * (Integer)other.getValue());
		case Float:
			return new AALValue((Float)mValue * (Float)other.getValue());		
		}
		
		// Failsafe case.
		return this;
	}
	
	
	/** Operator for testing inequality. Returns a boolean. */
	public AALValue lessThan(AALValue other) {
		switch (mType) {
		case Int:
			return new AALValue((Integer)mValue < (Integer)other.getValue());
		case Float:
			return new AALValue((Float)mValue < (Float)other.getValue());		
		}
		
		// Failsafe case.
		return this;
	}
	
	/** Operator for testing inequality. Returns a boolean. */
	public AALValue greaterThan(AALValue other) {
		switch (mType) {
		case Int:
			return new AALValue((Integer)mValue > (Integer)other.getValue());
		case Float:
			return new AALValue((Float)mValue > (Float)other.getValue());		
		}
		
		// Failsafe case.
		return this;
	}
	
	/** Operator for testing equality. Returns a boolean. */
	public AALValue equalTo(AALValue other) {
		switch (mType) {
		case Int:
			return new AALValue((Integer)mValue == (Integer)other.getValue());
		case Float:
			return new AALValue((float)(Float)(mValue) == (float)(Float)(other.getValue()));
		case Bool:
			return new AALValue((Boolean)mValue == (Boolean)other.getValue());
		case String:
			return new AALValue(((String)mValue).equals((String)other.getValue()));
		}
		
		// Failsafe case.
		return this;
	}
	
	/** Boolean operator. */
	public AALValue and(AALValue other) {
		switch (mType) {
		case Bool:
			return new AALValue((Boolean)mValue && (Boolean)other.getValue());				
		}
		
		// Failsafe case.
		return this;
	}
	
	/** Boolean operator. */
	public AALValue or(AALValue other) {
		switch (mType) {
		case Bool:
			return new AALValue((Boolean)mValue && (Boolean)other.getValue());				
		}
		
		// Failsafe case.
		return this;
	}
	
	/** Boolean operator. */
	public AALValue not(AALValue other) {
		switch (mType) {
		case Bool:
			// Ignore right operand.
			return new AALValue(!(Boolean)mValue);				
		}
		
		// Failsafe case.
		return this;
	}
	
	
	
	public boolean equals(Object _other) {
		AALValue other = (AALValue) _other;
		return mType.equals(other.mType) && (Boolean) equalTo(other).getValue();
	}
	
	public String toString() {
		return mValue.toString();
	}


}
