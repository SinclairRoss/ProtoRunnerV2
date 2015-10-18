package com.raggamuffin.protorunnerv2.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.SoundPool;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener, OnErrorListener, OnAudioFocusChangeListener
{	
	private static String TAG = "AudioService";
	
	private Context m_Context;
	private AudioManager m_AudioManager;
	
	private MediaPlayer m_MediaPlayer;
	private SoundPool m_SoundPool;
	private float m_MasterVolume;
	
	public AudioService(Context context, int numSounds)
	{
		m_Context = context;
		m_MasterVolume = 1.0f;

		m_AudioManager =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int result = m_AudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

		if(result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
			new RuntimeException("AudioService.java - Audio focus denied.");

		m_SoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);		
	}
	
	public void LoadMusic(int id)
	{
		m_MediaPlayer = MediaPlayer.create(m_Context, id);
		m_MediaPlayer.setLooping(true);
	}
	
	public void PlayMusic()
	{
		m_MediaPlayer.start();
	}
	
	public int LoadClip(int id)
	{
		return m_SoundPool.load(m_Context, id, 1);
	}
	
	public void PlayClip(int id, double leftVolume, double rightVolume, boolean loop)
	{
        int loopCount = loop ? -1 : 0;
		
		m_SoundPool.play(id, (float) leftVolume * m_MasterVolume, (float) rightVolume * m_MasterVolume, 1, loopCount, 1);
	}
	
	public void Pause()
	{
		Log.e(TAG, "Pause");
		m_SoundPool.autoPause();
		m_MediaPlayer.pause();
	}
	
	public void Resume()
	{
		Log.e(TAG, "Resume");
		m_MasterVolume = 1.0f;
		m_SoundPool.autoResume();

        if(m_MediaPlayer != null)
        {
            Log.e("AudioService", "Null pointer: m_MediaPlayer");
            m_MediaPlayer.start();
            m_MediaPlayer.setVolume(m_MasterVolume, m_MasterVolume);
        }
	}
	
	public void Stop()
	{
		Log.e(TAG, "Stop");
		m_SoundPool.release();
		m_AudioManager.abandonAudioFocus(this);
		m_MediaPlayer.stop();
		m_MediaPlayer.release();
		m_MediaPlayer = null;
	}

    private void UnDuck()
    {
        m_MasterVolume = 1.0f;
        m_MediaPlayer.setVolume(m_MasterVolume, m_MasterVolume);
    }

	private void Duck()
	{
		m_MasterVolume = 0.3f;
		m_MediaPlayer.setVolume(m_MasterVolume, m_MasterVolume);
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra)
	{
		return false;
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	@Override
	public void onPrepared(MediaPlayer arg0) 
	{
		
	}

	@Override
	public void onAudioFocusChange(int focusChange) 
	{
		switch(focusChange)
		{
			case AudioManager.AUDIOFOCUS_GAIN:	
				Log.e(TAG, "AUDIOFOCUS_GAIN");
                UnDuck();
				break;
				
			case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
				Log.e(TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                UnDuck();
				break;
				
			case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:	
				Log.e(TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                UnDuck();
				break;
				
			case AudioManager.AUDIOFOCUS_LOSS: 
				Log.e(TAG, "AUDIOFOCUS_LOSS");
				Stop();
				break;
				
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
				Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
				Pause();
				break;
				
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: 
				Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ");
				Duck();
				break;
		}
	}
}