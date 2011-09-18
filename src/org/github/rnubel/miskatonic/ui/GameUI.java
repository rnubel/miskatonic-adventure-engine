package org.github.rnubel.miskatonic.ui;

import java.util.ConcurrentModificationException;
import java.util.Vector;

import org.github.rnubel.miskatonic.GameView;
import org.github.rnubel.miskatonic.GameView.GameThread;

import android.graphics.Canvas;
import android.graphics.Paint;


/**
 * This class is used within the GameThread to define and handle
 * UI events. Clicks that cause a UI event will not "drop down" to
 * the scene.
 */
public class GameUI {
	public static final int POSITION_TOP 			= 1;
	public static final int POSITION_LEFT 			= 2;
	public static final int POSITION_RIGHT 			= 3;
	public static final int POSITION_BOTTOM 		= 4;
	public static final int POSITION_TOPLEFT 		= 5;
	public static final int POSITION_TOPRIGHT 		= 6;
	public static final int POSITION_BOTTOMLEFT 	= 7;
	public static final int POSITION_BOTTOMRIGHT 	= 8;
	public static final int POSITION_CENTER			= 9;
	
	public static Paint scratchPaint = new Paint();
	
	private class PositionedControl {
		public UIControl mControl;
		public int mX, mY, mWidth, mHeight, mPosition;
		
		public PositionedControl(UIControl control, int x, int y, int pos) {
			mControl = control;
			mWidth = control.getWidth();
			mHeight = control.getHeight();			
			mX = x;
			mY = y;			
			mPosition = pos;
			control.mUI = GameUI.this;
			
			control.doAfterAttach();
		}
		
		public void draw(Canvas c) {
			mControl.draw(c, mX, mY);
		}
		
		public boolean handleClick(int x, int y) {			
			if (x > mX && y > mY && x < mX + mWidth && y < mY + mHeight) {
				mControl.trigger(mGame, x - mX, y - mY);
				
				return true;
			} else {
				return false;
			}
		}

		public boolean shouldRemove() {
			return mControl.mShouldRemove;
		}

		public int getWidth() {
			return mWidth;
		}

		public int getHeight() {
			return mHeight;
		}
	}

	/** A positioned control with uniform padding. */
	private class PaddedPositionedControl extends PositionedControl {
		private int mPadding;		

		public PaddedPositionedControl(UIControl control, int x, int y, int pos, int padding) {
			super(control, x, y, pos);
			mPadding = padding;
		}

		public void draw(Canvas c) {
			mControl.draw(c, mX + mPadding, mY + mPadding);
		}

		@Override
		public int getWidth() {
			return mWidth + 2 * mPadding;
		}

		@Override
		public int getHeight() {
			return mHeight + 2 * mPadding;
		}

	}
	
	/** List of controls. */
	private Vector<PositionedControl> mControls;
	
	private int mWidth;
	private int mHeight;
	private GameThread mGame;
	private Vector<PositionedControl> mRemovalQueue = new Vector<PositionedControl>();
	private Vector<PositionedControl> mAdditionQueue = new Vector<PositionedControl>();
	
	/** Create the UI with given dimensions. */
	public GameUI(int width, int height, GameThread game) {
		mWidth = width;
		mHeight = height;
		mGame = game;
		
		mControls = new Vector<PositionedControl>();
	}
	
	/**
	 * Respond to a click event. 
	 * @return True if the event was handled; false otherwise.
	 */
	public boolean onClick(int x, int y) {
		// Iterate through all PositionedControls and see which one
		// is being clicked on.
		// ADDED: Iterate in reverse.
		for (int i = mControls.size() - 1; i >= 0; i--) {
			PositionedControl control = mControls.get(i);
			if (control.handleClick(x, y))
				return true;
		}
		
		return false;
	}
	
	/** Draw the UI to the given canvas. */
	public synchronized void draw(Canvas c) {		
		doQueue();
		try {
			for (PositionedControl control : mControls) {
				if (control.shouldRemove()) {				
					mRemovalQueue.add(control);
				}
				control.draw(c);
			}				
		} catch (ConcurrentModificationException e) {
			// Screw concurrency.
		}
	}
	
	/**
	 * Add a UI control to this interface. At this point, we compute
	 * its actual X and Y coordinates.
	 */
	public void addControl(UIControl control, int position) {
		addControl(control, position, true);
	}
	
	/**
	 * Add a UI control to this interface. At this point, we compute
	 * its actual X and Y coordinates. 
	 * @param shift specifies whether or not to shift controls afterward. 
	 */
	public void addControl(UIControl control, int position, boolean shift) {
		PositionedControl pControl = new PositionedControl(
				control, 
				getXForPosition(position, control),
				getYForPosition(position, control),
				position);
		mControls.add(pControl);
		if (shift)
			adjustPositionForControl(pControl);
	}
	
	/**
	 * Add a UI control to this interface. At this point, we compute
	 * its actual X and Y coordinates.
	 * @param padding how much padding to add. 
	 */
	public void addControl(UIControl control, int position, boolean shift, int padding) {
		PositionedControl pControl = new PaddedPositionedControl(
				control, 
				getXForPosition(position, control),
				getYForPosition(position, control),
				position,
				padding);
		mControls.add(pControl);
		if (shift)
			adjustPositionForControl(pControl);
	}
	
	/** Remove UI controls from given position. */
	public void removeControlsFromPosition(int position) {
		for (PositionedControl control : mControls) {
			if (control.mPosition == position) {				
				mRemovalQueue.add(control);
			}		
		}
		
		doQueue();
	}

	/** Sum up the width of controls currently at a given position. */
	public int getWidthOfControlsAtPosition(int position) {
		int width = 0;
		for (PositionedControl control : mControls) {
			if (control.mPosition == position) {				
				width += control.getWidth();
			}		
		}
		return width;
	}

	/** Sum up the height of controls currently at a given position. */
	public int getHeightOfControlsAtPosition(int position) {
		int height = 0;
		for (PositionedControl control : mControls) {
			if (control.mPosition == position) {
				height += control.getHeight();
			}
		}
		return height;
	}
		
	/** Process the removal queue. */
	private synchronized void doQueue() {		
		while (!mRemovalQueue.isEmpty()) {
			mControls.removeElement(mRemovalQueue.get(0));			
			mRemovalQueue.remove(0);
		}		
		
		while (!mAdditionQueue.isEmpty()) {
			mControls.add(mAdditionQueue.get(0));
			mAdditionQueue.remove(0);
		}
	}

	
	/** FIXME: Refactor all of these. */
	public void shiftControlRight(PositionedControl control, int dx) {
		control.mX += dx;
	}
	public void shiftControlLeft(PositionedControl control, int dx) {
		control.mX -= dx;
	}
	public void shiftControlUp(PositionedControl control, int dy) {
		control.mY -= dy;
	}
	public void shiftControlDown(PositionedControl control, int dy) {
		control.mY += dy;
	}
	public void shiftControlsLeft(int position, int dx) {
		for (PositionedControl control : mControls) {
			if (control.mPosition == position) {
				shiftControlLeft(control, dx);
			}
		}
	}
	public void shiftControlsRight(int position, int dx) {
		for (PositionedControl control : mControls) {
			if (control.mPosition == position) {
				shiftControlRight(control, dx);
			}
		}
	}
	public void shiftControlsUp(int position, int dy) {
		for (PositionedControl control : mControls) {
			if (control.mPosition == position) {
				shiftControlUp(control, dy);
			}
		}
	}
	public void shiftControlsDown(int position, int dy) {
		for (PositionedControl control : mControls) {
			if (control.mPosition == position) {
				shiftControlDown(control, dy);
			}
		}
	}


	/** Adjust the control so that it does not overlap with the other controls. This assumes that all existing controls 
	 *  have had this method called after they were added, except for the given control. */
	public void adjustPositionForControl(PositionedControl control) {
		int position = control.mPosition;
		switch (control.mPosition) {
			case POSITION_TOPLEFT:
			case POSITION_BOTTOMLEFT:
				shiftControlRight(control, getWidthOfControlsAtPosition(position) - control.getWidth());
				break;
			case POSITION_TOP:
			case POSITION_BOTTOM:
				shiftControlsLeft(control.mPosition, control.getWidth()/2);
				shiftControlRight(control, getWidthOfControlsAtPosition(position)/2);
				break;
			case POSITION_CENTER:
			case POSITION_LEFT:
			case POSITION_RIGHT:
				shiftControlsUp(control.mPosition, control.getHeight()/2);
				shiftControlDown(control, getHeightOfControlsAtPosition(position)/2);
				break;
			case POSITION_TOPRIGHT:
			case POSITION_BOTTOMRIGHT:
				shiftControlLeft(control, getWidthOfControlsAtPosition(position) - control.getWidth());
				break;
			default:
				break;
		}
	}	

	/** Get the position for a control. */
	public int getXForPosition(int position, UIControl control) {		
		switch (position) {
		case POSITION_TOPLEFT:
		case POSITION_BOTTOMLEFT:
		case POSITION_LEFT:
			return 0;
			
		case POSITION_TOP:
		case POSITION_CENTER:
		case POSITION_BOTTOM:
			return mWidth / 2 - control.getWidth() / 2;
			
		case POSITION_TOPRIGHT:
		case POSITION_RIGHT:
		case POSITION_BOTTOMRIGHT:
			return mWidth - control.getWidth();
			
		default :
			return 0;
		}
	}
	
	/** Get the position for a control. */
	public int getYForPosition(int position, UIControl control) {				 
		switch (position) {
		case POSITION_TOPLEFT:
		case POSITION_TOP:
		case POSITION_TOPRIGHT:
			return 0;
			
		case POSITION_LEFT:
		case POSITION_CENTER:
		case POSITION_RIGHT:
			return mHeight / 2 - control.getHeight() / 2;
			
		case POSITION_BOTTOMLEFT:
		case POSITION_BOTTOM:
		case POSITION_BOTTOMRIGHT:
			return mHeight - control.getHeight();
			
		default :
			return 0;
		}
	}

	public GameThread getGameThread() {
		return mGame;
	}
}
