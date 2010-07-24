package com.games.test1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

public class TextObject extends DrawnObject {
	
	protected String mText;
	private Paint mTextPaint;
	
	public TextObject(float x, float y, String mText) {
		this.mX = x;
		this.mY = y;
		this.mText = mText;
		this.mClickZone = new Rect(0,0,1,1);
		this.mTextPaint = new Paint(DrawnObject.basicPaint);
	}
	
	public void draw(Canvas c, float x, float y)
	{	
		c.drawText(mText, (float)x, (float)y, mTextPaint);
	}
	
	public void setFontColor(int color) {
		this.mTextPaint.setColor(color);
	}
	
	public void setFontSize(float size) {
		this.mTextPaint.setTextSize(size);
	}
	
	public void setFontAlign(Paint.Align align) {
		this.mTextPaint.setTextAlign(align);
	}
	
	public void setTypeface(Typeface t) {
		this.mTextPaint.setTypeface(t);
	}
}
