package org.github.rnubel.miskatonic.ui;

import org.github.rnubel.miskatonic.GameView;
import org.github.rnubel.miskatonic.GameView.GameThread;

import android.util.Log;


/** A functor to be triggered as a UI event. */
public class UIEvent {
	public void execute(GameThread game, UIControl caller) {
		Log.w("Miskatonic", "DEFAULT EVENT TRIGGERED");
	}
}
