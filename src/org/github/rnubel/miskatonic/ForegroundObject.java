package org.github.rnubel.miskatonic;

import org.github.rnubel.miskatonic.astraal.ASTRAALResAnimation;

import android.graphics.Bitmap;

public class ForegroundObject extends DrawnObject {
	
		public ForegroundObject(Bitmap mBitmap, float mX, float mY, int mW, int mH) {
			super(mBitmap, mX, mY, mW,mH);
		}
		
		
		public ForegroundObject(Bitmap mBitmap, float mX, float mY, int mW, int mH, int frameCount, int FPS)
		{
			super(mBitmap, mX, mY, mW, mH,frameCount, FPS);
		}
		
		public ForegroundObject(Animation anim, float mX, float mY, int mW, int mH) {
			super(anim, mX, mY, mW, mH);
		}

		public ForegroundObject(Animation anim, float x, float y) {
			super(anim, x, y);
		}

		public ForegroundObject() {
			// Do nothing.
		}

		
}
