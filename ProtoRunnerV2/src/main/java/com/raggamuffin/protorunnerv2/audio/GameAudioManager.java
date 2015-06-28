package com.raggamuffin.protorunnerv2.audio;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector2;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GameAudioManager 
{
	final int NUM_AUDIO_CLIPS = AudioClips.values().length;
	final double MIN_AUDIO_THRESHOLD_SQR = 1.0;
	final double MAX_AUDIO_THRESHOLD_SQR = 60 * 60;
	
	ChaseCamera m_Listener;
	AudioService m_AudioService;
	
	Vector2 m_ListenerForward;
	Vector2 m_RelativePosition;
	
	int[] m_IDs;
	
	public GameAudioManager(Context context, ChaseCamera listener)
	{
		m_Listener = listener;
		m_AudioService = new AudioService(context, NUM_AUDIO_CLIPS);
		
		m_IDs = new int[NUM_AUDIO_CLIPS];
		
		m_ListenerForward  = new Vector2();
		m_RelativePosition = new Vector2();
		
		LoadSounds();
		StartMusic();
	}
	
	private void LoadSounds()
	{
		// SFX
		m_IDs[AudioClips.PulseLaser.ordinal()] 		= m_AudioService.LoadClip(R.raw.pulse_laser);
		m_IDs[AudioClips.PulseLaserPunk.ordinal()] 	= m_AudioService.LoadClip(R.raw.pulse_laser_punk);
		m_IDs[AudioClips.RailGun.ordinal()] 		= m_AudioService.LoadClip(R.raw.rail_gun);
		m_IDs[AudioClips.Explosion.ordinal()] 		= m_AudioService.LoadClip(R.raw.explosion);	
		m_IDs[AudioClips.MissileSpawned.ordinal()] 	= m_AudioService.LoadClip(R.raw.missile_spawn);	
		m_IDs[AudioClips.MissileEngaged.ordinal()] 	= m_AudioService.LoadClip(R.raw.missile_engaged);	
		
		// UI Sounds.
		m_IDs[AudioClips.UIClick.ordinal()] 		= m_AudioService.LoadClip(R.raw.ui_click);
		m_IDs[AudioClips.UITick.ordinal()] 			= m_AudioService.LoadClip(R.raw.ui_tick);
		
		// Music
		m_AudioService.LoadMusic(R.raw.duality_dimrain47);
	}
	
	public void StartMusic()
	{
		m_AudioService.PlayMusic();
	}
	
	public void PlaySound(AudioClips clip)
	{
		PlaySound(clip, 1.0, 1.0);
	}
	
	public void PlaySound(Vector3 pos, AudioClips clip)
	{
		Vector3 listenerPos = m_Listener.GetPosition();
		m_RelativePosition.SetVector(pos.I - listenerPos.I, pos.K - listenerPos.K);
		
		double volume  = CalculateVolume(m_RelativePosition);
		
		if(volume <= 0.0)
			return;

		Vector3 listenerLookat = m_Listener.GetLookAt();
		m_ListenerForward.SetVector(listenerLookat.I, listenerLookat.K);

		double panning = CalculatePanning(m_RelativePosition, m_ListenerForward);
		
		double leftVolume  = volume * panning;
		double rightVolume = volume * (1.0 - panning);

		PlaySound(clip, leftVolume, rightVolume);
	}
	
	public void PlaySound(AudioClips clip, double left, double right)
	{
		m_AudioService.PlayClip(clip.ordinal() + 1, left, right, false);
	}
	
	private double CalculateVolume(Vector2 displacement)
	{
		double volume = MathsHelper.Normalise(displacement.GetLengthSqr(), MIN_AUDIO_THRESHOLD_SQR, MAX_AUDIO_THRESHOLD_SQR);
		return 1.0 - volume;
	}
	
	private double CalculatePanning(Vector2 displacement, Vector2 forward)
	{
		double theta = Vector2.RadiansBetween(displacement, forward);	
		
		if(theta > MathsHelper.PI_OVER_2)
			theta = MathsHelper.PI_OVER_2 -(theta - MathsHelper.PI_OVER_2);
		
		theta = MathsHelper.Normalise(theta, 0, MathsHelper.PI_OVER_2);

		if (Vector2.Determinant(forward, displacement) >= 0.0)
			return 0.5 - (theta * 0.5);
		else
			return 0.5 + (theta * 0.5);
	}
	
	public void Stop()
	{
		m_AudioService.Pause();
	}
	
	public void Resume()
	{
		m_AudioService.Resume();
	}
}	




























