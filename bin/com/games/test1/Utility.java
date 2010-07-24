package com.games.test1;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

import com.games.test1.aal.AALValue;
import com.games.test1.ui.GameUI;

public class Utility {
	public static PointF moveTowards(float mX, float mY, int x2, int y2, float dist) {
		double dir = Math.atan2(y2 - mY, x2 - mX);		
		return new PointF((float)(mX + dist * Math.cos(dir)), (float)(mY + dist * Math.sin(dir)));
	//	System.out.println((float)(mX + dist * Math.cos(dir)) + ", " + (float)(mY + dist * Math.sin(dir)));
	//	return null;
	}



	/** Because Android can't figure out that we might not want to write separate code for restoring state in
	    two slightly-different situations (restart and cold start), this function converts a non-serializable
	    Bundle into a serializable HashMap. */
	public static HashMap<String, Serializable> convertBundleToHashMap(Bundle b) {
		HashMap<String, Serializable> map = new HashMap<String, Serializable>();
		// Add each key/value pair to the HashMap.
		for (String key : b.keySet()) {
			Object o = b.get(key);
			if (o.getClass() == Bundle.class) {
				// Bundle is not serializable.
				map.put(key, convertBundleToHashMap((Bundle) o));
			} else {		
				map.put(key, (Serializable) o);
			}
		}

		return map;
	}

	/** Converts the passed-in HashMap to a bundle. However, only a few types are supported. */
	public static Bundle convertHashMapToBundle(HashMap<String, Serializable> map) {
		Bundle b = new Bundle();
		for (Entry<String, Serializable> e : map.entrySet()) {
			String key = e.getKey();
			Object o = e.getValue();
			// Blerg.
			if (o.getClass() == HashMap.class) {
				b.putParcelable(key, convertHashMapToBundle((HashMap<String, Serializable>) o));
			} else if (o.getClass() == AALValue.class) {
				b.putParcelable(key, (AALValue) o);
			} else if (o.getClass() == Integer.class) {
				b.putInt(key, (Integer) o);
			} else if (o.getClass() == Float.class) {
				b.putFloat(key, (Float) o);
			} else if (o.getClass() == Boolean.class) {
				b.putBoolean(key, (Boolean) o);
			} else if (o.getClass() == String.class) {
				b.putString(key, (String) o);
			} else if (o.getClass() == ObjectData[].class) {
				b.putParcelableArray(key, (ObjectData[]) o);
			} else {
				// ERROR
				b.putInt(key, 0);
			}
		}

		return b;
	}



	/** Typeset the given text into a box of size width. */
	public static void typesetText(String text, int width, Vector<String> output) {
		typesetText(text, width, output, GameUI.scratchPaint);
	}
	
	public static void typesetText(String text, int width, Vector<String> output, Paint textPaint) {
		int offset;				
		while ((offset = textPaint.breakText(text, true, width, null)) != 0) {
			// Move backwards until we find a space.
			if (text.length() > offset && text.charAt(offset) != ' ') {
				int roffset = text.substring(0, offset - 1).lastIndexOf(' ');
				if (roffset != -1) {
					offset = roffset;
				}
			}
			if (text.trim().length() > 0) {
				output.add(text.substring(0, offset));
			} else return;
			text = text.substring(offset).trim();					
		}
	}
}
