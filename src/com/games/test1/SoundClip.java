package com.games.test1;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;

public class SoundClip extends Resource {
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
		mSPSoundID = soundPool.load(afd, 1); // second arg unused.
	}
	
	/** Play this sound. */
	public void play(int loop) {
		// FIXME: extract args.
		mStreamID = soundPool.play(mSPSoundID, 1, 1, 1, loop, 1);
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
		soundPool.stop(mStreamID);
	}

	
	
	public void setSoundID(String mSoundID) {
		this.mSoundID = mSoundID;
	}

	public String getSoundID() {
		return mSoundID;
	}
}


/*
 
 OLD CRAPPY CODE - I BLAME TED NOT UNDERSTANDING ANDROID'S INTRICACIES
 AND MY ENTIRE FRAMEWORK.

public class SoundClip extends Resource {

	private int mResource;
	private MediaPlayer mMP;
	private Context mContext;
	
	public SoundClip()
	{
		mMP = new MediaPlayer();
		init();
	}
	
	public SoundClip(int mR, Context c)
	{
		mResource = mR;
		mMP = MediaPlayer.create(c, mResource);
		init();	
	}
	
	private void init()
	{
		//Add handler to release resources when done.
		mMP.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer mp) {
				mp.stop();
				try {
					mp.prepare();
				}
				catch (Exception e) {
					//Man i hope this doesnt happen
				}
			} });
	}
	

	
	public void play()
	{
		if (mMP.isPlaying()) { mMP.seekTo(0); }
		mMP.start();
	}
	
	public void setResource(int mR)
	{
		mResource = mR;
		mMP = MediaPlayer.create(mContext,mResource);
	}
	
	public int getResource()
	{
		return mResource;
	}
	
	public void setContext(Context c)
	{
		mContext = c;
	}
	
	
}
 */