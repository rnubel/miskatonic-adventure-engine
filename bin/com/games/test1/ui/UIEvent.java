package com.games.test1.ui;

import android.util.Log;

import com.games.test1.GameView;
import com.games.test1.GameView.GameThread;

/** A functor to be triggered as a UI event. */
public class UIEvent {
	public void execute(GameThread game) {
		Log.w("Miskatonic", "DEFAULT EVENT TRIGGERED");
	}
}
