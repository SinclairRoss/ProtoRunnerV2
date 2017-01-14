package com.raggamuffin.protorunnerv2.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
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
	
	private MediaPlayer m_MediaPlayer;	// For game music.
	private SoundPool m_SoundPool;		// For sound effects.

    private float m_MusicVolumeStandard;
    private float m_MusicVolumeDucked;
    private float m_MusicVolume;

    private float m_SFXVolumeStandard;
    private float m_SFXVolumeDucked;
    private float m_SFXVolume;

	public AudioService(Context context)
    {
        m_Context = context;

        m_MusicVolumeStandard = 0.3f;
        m_MusicVolumeDucked = 0.1f;
        m_MusicVolume = m_MusicVolumeStandard;

        m_SFXVolumeStandard = 1.0f;
        m_SFXVolumeDucked = 0.3f;
        m_SFXVolume = m_SFXVolumeStandard;

        m_AudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        m_AudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        m_SoundPool = new SoundPool.Builder()
                .setMaxStreams(1024)
                .setAudioAttributes(audioAttributes)
                .build();
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
	
	public int PlayClip(int id, double leftVolume, double rightVolume, boolean loop)
	{
        int loopCount = loop ? -1 : 0;

		return m_SoundPool.play(id, (float) leftVolume * m_SFXVolume, (float) rightVolume * m_SFXVolume, 1, loopCount, 1);
	}

	public void StopClip(int streamID)
	{
		m_SoundPool.stop(streamID);
	}

	public void Pause()
	{
		Log.e(TAG, "Pause");

		//TODO: INVESTIVATE NULL PTR WITH SOUNDPOOL.
		m_SoundPool.autoPause();
        m_MediaPlayer.pause();
	}
	
	public void Resume()
	{
		Log.e(TAG, "Resume");

        m_MusicVolume = m_MusicVolumeStandard;
        m_SFXVolume = m_SFXVolumeStandard;

        m_SoundPool.autoResume();

        m_MediaPlayer.start();
        m_MediaPlayer.setVolume(m_MusicVolume, m_MusicVolume);
	}
	
	public void Stop()
	{
		Log.e(TAG, "Stop");

		m_SoundPool.release();
        m_SoundPool = null;

		m_MediaPlayer.stop();
		m_MediaPlayer.release();
		m_MediaPlayer = null;

        m_AudioManager.abandonAudioFocus(this);
        m_AudioManager = null;
	}

	private void Duck()
	{
		m_MusicVolume = m_MusicVolumeDucked;
        m_SFXVolume = m_SFXVolumeDucked;

		m_MediaPlayer.setVolume(m_MusicVolume, m_MusicVolume);
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
	{}

	@Override
	public void onAudioFocusChange(int focusChange) 
	{
		switch(focusChange)
		{
			case AudioManager.AUDIOFOCUS_GAIN:
            {
                Log.e(TAG, "AUDIOFOCUS_GAIN");
                Resume();
                break;
            }
			case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
            {
                Log.e(TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                Resume();
                break;
            }
			case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
            {
                Log.e(TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                Resume();
                break;
            }
			case AudioManager.AUDIOFOCUS_LOSS:
            {
                Log.e(TAG, "AUDIOFOCUS_LOSS");
                Stop();
                break;
            }
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            {
                Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                Pause();
                break;
            }
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
            {
                Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ");
                Duck();
                break;
            }
		}
	}
}