package com.games.test1;

import android.graphics.Bitmap;

public class BackgroundObject extends DrawnObject {
	
		public BackgroundObject(Bitmap mBitmap, float mX, float mY, int mW, int mH) {
			super(mBitmap, mX, mY, mW,mH);
		}		
		
		public BackgroundObject(Bitmap mBitmap, float mX, float mY, int mW, int mH, int frameCount, int FPS)
		{
			super(mBitmap, mX, mY, mW, mH,frameCount, FPS);		
		}
				
		public BackgroundObject(Animation anim, float mX, float mY, int mW, int mH) {
			super(anim, mX, mY, mW, mH);
		}
}
