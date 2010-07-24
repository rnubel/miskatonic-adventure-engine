package com.games.test1;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * 
 * A SoundClip is a sound designed to be played only once.
 *
 */
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
