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
		m_AudioService = new AudioService(context);
		
		m_IDs = new int[NUM_AUDIO_CLIPS];
		
		m_ListenerForward  = new Vector2();
		m_RelativePosition = new Vector2();
		
		LoadSounds();
	}
	
	private void LoadSounds()
	{
        // Weapons.
        m_IDs[AudioClips.Blaster_Friendly.ordinal()] = m_AudioService.LoadClip(R.raw.pulse_laser_punk);
        m_IDs[AudioClips.Blaster_Enemy.ordinal()] = m_AudioService.LoadClip(R.raw.pulse_laser);
        m_IDs[AudioClips.Laser_Friendly.ordinal()] = m_AudioService.LoadClip(R.raw.pulse_laser_punk);
        m_IDs[AudioClips.Laser_Enemy.ordinal()] = m_AudioService.LoadClip(R.raw.pulse_laser);

        // Missile.
        m_IDs[AudioClips.Missile_Spawned.ordinal()] = m_AudioService.LoadClip(R.raw.missile_spawn);
        m_IDs[AudioClips.Missile_Engaged.ordinal()] = m_AudioService.LoadClip(R.raw.missile_engaged);
        m_IDs[AudioClips.Missile_Ambient.ordinal()] = m_AudioService.LoadClip(R.raw.missile_engaged);

        // Flare.
        m_IDs[AudioClips.Flare_Spawned.ordinal()] = m_AudioService.LoadClip(R.raw.missile_spawn);
        m_IDs[AudioClips.Flare_Engaged.ordinal()] = m_AudioService.LoadClip(R.raw.missile_engaged);
        m_IDs[AudioClips.Flare_Ambient.ordinal()] = m_AudioService.LoadClip(R.raw.missile_engaged);

        // Misc SFX.
        m_IDs[AudioClips.Explosion.ordinal()] = m_AudioService.LoadClip(R.raw.explosion);
        m_IDs[AudioClips.Respawn.ordinal()] = m_AudioService.LoadClip(R.raw.explosion);
        m_IDs[AudioClips.EnemyDestroyed.ordinal()]= m_AudioService.LoadClip(R.raw.silence);
        m_IDs[AudioClips.WingmanDestroyed.ordinal()] = m_AudioService.LoadClip(R.raw.explosion);
		m_IDs[AudioClips.PlayerDamaged.ordinal()] = m_AudioService.LoadClip(R.raw.player_damaged);
        m_IDs[AudioClips.Silence.ordinal()] = m_AudioService.LoadClip(R.raw.silence);

		// UI Sounds.
		m_IDs[AudioClips.UI_Positive.ordinal()] = m_AudioService.LoadClip(R.raw.ui_positive);
        m_IDs[AudioClips.UI_Negative.ordinal()] = m_AudioService.LoadClip(R.raw.ui_negative);
        m_IDs[AudioClips.UI_Play.ordinal()] = m_AudioService.LoadClip(R.raw.ui_play);
		
		// Music
		m_AudioService.LoadMusic(R.raw.music_exceeder);
	}
	
	public void StartMusic()
	{
		m_AudioService.PlayMusic();
	}

	public int PlaySound(AudioClips clip)
	{
		return PlaySound(clip, 1.0, 1.0, false);
	}

	public int PlaySound(Vector3 pos, AudioClips clip, EAudioRepeatBehaviour repeatBehaviour)
	{
		Vector3 listenerPos = m_Listener.GetPosition();
		m_RelativePosition.SetVector(pos.I - listenerPos.I, pos.K - listenerPos.K);
		
		double volume  = CalculateVolume(m_RelativePosition);

		int streamID = -1;

		if(volume > 0.0)
		{
			Vector3 listenerLookat = m_Listener.GetLookAt();
			m_ListenerForward.SetVector(listenerLookat.I, listenerLookat.K);

			double panning = CalculatePanning(m_RelativePosition, m_ListenerForward);

			double leftVolume = volume * panning;
			double rightVolume = volume * (1.0 - panning);

            boolean loop = false;

            switch (repeatBehaviour)
            {
                case Manual:
                    loop = true;
                    break;
                case Single:
                    loop = false;
                    break;
            }

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
	
	private double CalculateVolume(Vector2 displacement)
	{
		double volume = MathsHelper.Normalise(displacement.GetLengthSqr(), MIN_AUDIO_THRESHOLD_SQR, MAX_AUDIO_THRESHOLD_SQR);
		return 1.0 - volume;
	}
	
	private double CalculatePanning(Vector2 displacement, Vector2 forward)
	{
		double theta = Vector2.RadiansBetween(displacement, forward);	
		
		if(theta > MathsHelper.PI_OVER_2)
		{
			theta = MathsHelper.PI_OVER_2 -(theta - MathsHelper.PI_OVER_2);
		}
		
		theta = MathsHelper.Normalise(theta, 0, MathsHelper.PI_OVER_2);

		return Vector2.Determinant(forward, displacement) >= 0.0 ? 0.5 - (theta * 0.5)
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

	public Vector3 GetListenerPosition()
	{
		return m_Listener.GetPosition();
	}
}	




























