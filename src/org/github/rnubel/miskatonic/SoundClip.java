package org.github.rnubel.miskatonic;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;

public class SoundClip extends Resource {
	private static final int DEFAULT_VOLUME = 1;
	private static final int MAX_SIMULTANEOUS_STREAMS = 10;
	private static SoundPool soundPool;
	
	/** Obtain a reference to our SoundPool, initializing it if needed. */
	public static SoundPool getSoundPool() {
		if (soundPool == null) {
			soundPool = new SoundPool(MAX_SIMULTANEOUS_STREAMS, AudioManager.STREAM_MUSIC, 0);
		}
		return soundPool;
	}
	
	/** Clear the sound pool until getSoundPool() re-initializes it. */
	public static void clearSoundPool() {
		soundPool.release();
		soundPool = null;
	}


	
	private String mSoundID;
	private int mSPSoundID;
	private int mStreamID;	
		
	/** Create a new sound clip from an asset file descriptor. */
	public SoundClip(String soundID, AssetFileDescriptor afd) {
		setSoundID(soundID);
		mSPSoundID = getSoundPool().load(afd, 1); // second arg unused.
	}
	
	/** Play this sound. */
	public void play(int loop) {
		// FIXME: extract arguments.
		mStreamID = getSoundPool().play(mSPSoundID, DEFAULT_VOLUME, DEFAULT_VOLUME, 1, loop, 1);
	}
	
	/** Play this sound; do not loop. */
	public void play() {
		play(0);
	}
	
	/** Play this sound; loop until stopped. */
	public void loop() {
		play(1);
	}
	
	/** Stop this sound. */
	public void stop() {
		getSoundPool().stop(mStreamID);
	}

	
	
	public void setSoundID(String mSoundID) {
		this.mSoundID = mSoundID;
	}

	public String getSoundID() {
		return mSoundID;
	}
}

