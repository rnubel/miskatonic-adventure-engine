package org.github.rnubel.miskatonic;

import org.github.rnubel.miskatonic.GameView.GameThread;

import org.github.rnubel.miskatonic.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Window;

/**
 * Basic idea: spawn thread to manage actual game. The activity
 * is only a wrapper which interfaces with Android and creates
 * Android-UI components. The game thread is contained within
 * a view, so it can draw to the screen.
 */
public class Miskatonic extends Activity {
	private GameView mGameView;
	private GameThread mGameThread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

		// Turn off the window's title bar.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Display the view.
		setContentView(R.layout.main);

		// Start up the game. 
		mGameView = (GameView) findViewById(R.id.gameview);
		mGameThread = mGameView.getThread();                

		// Reload our state if this was a resume or restart.
		if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
			mGameThread.loadFromBundle(savedInstanceState);			
   		}
	}


	/**
	 * Invoked when the Activity loses user focus.
	 */
	@Override
	protected void onPause() {
		Log.w("Miskatonic", "LIFECYCLE: Pausing");
		super.onPause();
		mGameView.getThread().pause(); // pause game when Activity pauses
	}

	/**
	 * Invoked when the Activity regains user focus.
	 */
	protected void onResume() {
		Log.w("Miskatonic", "LIFECYCLE: Resuming");
		super.onResume();
	}

	/**
	 * Invoked when the Activity is temporarily shut down.
	 */
	protected void onStop() {
		Log.w("Miskatonic", "LIFECYCLE: Stopping");
		super.onStop();
		mGameView = null;
		System.gc();
	}	

	/**
	 * Invoked when the Activity is started or restarted.
	 */
	protected void onStart() {
		Log.w("Miskatonic", "LIFECYCLE: Starting");
		super.onStart();    	
		
		//Debug.startMethodTracing("profile");
	}

	/**
	 * Invoked when the Activity is restarted.
	 */
	protected void onRestart() {
		Log.w("Miskatonic", "LIFECYCLE: Restarting");
		super.onRestart();
	}

	/**
	 * Notification that something is about to happen, to give the Activity a
	 * chance to save state.
	 * 
	 * @param outState a Bundle into which this Activity should save its state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//Debug.stopMethodTracing();
		// just have the View's thread save its state into our Bundle
		super.onSaveInstanceState(outState);
		mGameThread.saveToBundle(outState);    	
	}

}
