package com.raggamuffin.protorunnerv2.audio;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import android.util.Log;

public class GameAudioManager 
{
	private final int NUM_AUDIO_CLIPS = AudioClips.values().length;
    private final double ATTENUATION = 0.01;
	
	private ChaseCamera m_Listener;
	private AudioService m_AudioService;

	private Vector3 m_ListenerForward;
	private Vector3 m_ToListener;

	private int[] m_IDs;
	
	public GameAudioManager(Context context, ChaseCamera listener)
	{
		m_Listener = listener;
		m_AudioService = new AudioService(context);
		
		m_IDs = new int[NUM_AUDIO_CLIPS];
		
		m_ListenerForward  = new Vector3();
		m_ToListener = new Vector3();
		
		LoadSounds();
	}
	
	private void LoadSounds()
	{
        // Weapons.
        m_IDs[AudioClips.Blaster_Friendly.ordinal()] = m_AudioService.LoadClip(R.raw.blaster);
        m_IDs[AudioClips.Laser_Enemy.ordinal()] = m_AudioService.LoadClip(R.raw.enemyblaster);

		// UI Sounds.
		m_IDs[AudioClips.UI_Positive.ordinal()] = m_AudioService.LoadClip(R.raw.ui_positive);
        m_IDs[AudioClips.UI_Negative.ordinal()] = m_AudioService.LoadClip(R.raw.ui_negative);
		m_IDs[AudioClips.UI_Play.ordinal()] = m_AudioService.LoadClip(R.raw.ui_positive);

        m_IDs[AudioClips.EnemyDown.ordinal()] = m_AudioService.LoadClip(R.raw.enemydown);
		m_IDs[AudioClips.TouchMe.ordinal()] = m_AudioService.LoadClip(R.raw.touchme);
        m_IDs[AudioClips.Dodge.ordinal()] = m_AudioService.LoadClip(R.raw.dodgewoosh);

		// Music
		int music = R.raw.skeletons;

		m_AudioService.LoadMusic(music);
	}
	
	public void StartMusic()
	{
		m_AudioService.PlayMusic();
	}

	public void PauseMusic() { m_AudioService.PauseMusic();}

	public int PlaySound(AudioClips clip)
	{
		return PlaySound(clip, 1.0, 1.0, false);
	}

	public int PlaySound(Vector3 pos, AudioClips clip, EAudioRepeatBehaviour repeatBehaviour)
	{
		Vector3 listenerPos = m_Listener.GetPosition();
        m_ToListener.SetVectorAsDifference(pos, listenerPos);
		
		double volume  = CalculateVolume(m_ToListener);

		int streamID = -1;

		if(volume > 0.0)
		{
			Vector3 listenerLookat = m_Listener.GetLookAt();
			m_ListenerForward.SetVector(listenerLookat);

		//	double panning = CalculatePanning(m_ToListener, m_ListenerForward);

			double leftVolume = volume ;//* panning;
			double rightVolume = volume;// * (1.0 - panning);

            boolean loop = repeatBehaviour == EAudioRepeatBehaviour.Manual;

			streamID = PlaySound(clip, leftVolume, rightVolume, loop);
		}

        return streamID;
	}
	
	public int PlaySound(AudioClips clip, double left, double right, boolean loop)
	{
		return m_AudioService.PlayClip(clip.ordinal() + 1, left, right, loop);
	}

    public void StopSound(int streamID)
    {
        m_AudioService.StopClip(streamID);
    }
	
	private double CalculateVolume(Vector3 toListener)
	{
        double distanceSqr = toListener.GetLengthSqr();
		double volume = 1 / (ATTENUATION * distanceSqr);

        return volume;
	}
	
	private double CalculatePanning(Vector3 displacement, Vector3 forward)
	{
		double theta = Vector3.RadiansBetween(displacement, forward);
		
		if(theta > MathsHelper.PI_OVER_2)
		{
			theta = MathsHelper.PI_OVER_2 -(theta - MathsHelper.PI_OVER_2);
		}
		
		theta = MathsHelper.Normalise(theta, 0, MathsHelper.PI_OVER_2);

		return Vector3.Determinant(forward, displacement) >= 0.0 ? 0.5 - (theta * 0.5)
																 : 0.5 + (theta * 0.5);
	}
	
	public void Pause()
	{
		m_AudioService.Pause();
	}
	
	public void Resume()
	{
		m_AudioService.Resume();
	}

    public void Stop()
    {
        m_AudioService.Stop();
    }
}	




























