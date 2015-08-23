// Author:	Sinclair Ross.
// Date:	22/10/2014.
// Notes: 	This class represents a weapon. Extend this class to create new weapons.
//			Subclasses must define where the weapons muzzles are, otherwise there will be an array index out of bounds exception.

package com.raggamuffin.protorunnerv2.weapons;

import java.util.Vector;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.PostFireAction;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.vehicles.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Weapon 
{
	protected Vehicle m_Anchor;
    protected GameLogic m_Game;
	protected BulletManager m_BulletManager;
	protected ParticleManager m_ParticleManager;
	protected GameAudioManager m_AudioService;
		
	protected AudioClips m_AudioClip;
	
	protected FireControl m_FireMode;
	protected ProjectileTemplate m_ProjectileTemplate;

	protected double m_Damage;
    protected double m_Drain;
	protected double m_MuzzleVelocity;
	protected double m_Accuracy;
	protected double m_LifeSpan;
	protected double m_ProjectileFadeInTime;
    protected double m_ProjectileFadeOutTime;

	private Vector<Vector3> m_MuzzleOffsets;
	private Vector3 m_MuzzleOffset;
	private int m_MuzzleIndex;
	
	protected boolean m_HasLasers;
	private Vector<LaserPointer> m_Lasers;
	
	protected PostFireAction m_PostFireAction;
	
	private boolean m_TriggerPulled;
    private boolean m_IsFiring;

	public Weapon(Vehicle anchor, GameLogic game)
	{
		m_Anchor = anchor;
        m_Game = game;
		m_BulletManager = m_Game.GetBulletManager();
		m_ParticleManager = m_Game.GetParticleManager();
		m_AudioService = m_Game.GetGameAudioManager();
		
		m_AudioClip = AudioClips.PulseLaser;
		
		m_FireMode = null;
		m_ProjectileTemplate = null;

		m_Damage = 1.0;
        m_Drain = 1.0;
		m_MuzzleVelocity = 1.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 1.0;
		m_ProjectileFadeInTime = 0.02;
        m_ProjectileFadeOutTime = 0.9;

		m_MuzzleOffsets = new Vector<>();
		m_MuzzleOffset = new Vector3();
		m_MuzzleIndex = 0;
		m_HasLasers = false;
		m_Lasers = new Vector<>();

		m_PostFireAction = m_Anchor.GetPostFireAction();
		
		m_TriggerPulled = false;
        m_IsFiring = false;
	}
	
	// Override to add functionality.
	public void Update(double deltaTime)
	{
        m_IsFiring = false;

        m_FireMode.Update(deltaTime);
		
		if(m_FireMode.ShouldFire())
		{
			Fire();
			NextMuzzle();
			
			m_AudioService.PlaySound(m_Anchor.GetPosition(), m_AudioClip);
		}
	}
	
	public void OpenFire()
	{	
		m_FireMode.PullTrigger();
		m_TriggerPulled = true;
	}
	
	public void CeaseFire()
	{
		m_FireMode.ReleaseTrigger();
		m_TriggerPulled = false;
	}
	
	public Vector3 GetFirePosition()
	{
		m_MuzzleOffset.SetVector(m_MuzzleOffsets.elementAt(m_MuzzleIndex));

        m_MuzzleOffset.RotateY(m_Anchor.GetYaw());
		m_MuzzleOffset.Add(m_Anchor.GetPosition());
		
		return m_MuzzleOffset;
	}
	
	public Vector3 GetMuzzlePosition(Vector3 Muzzle)
	{
		m_MuzzleOffset.SetVector(Muzzle);
		
		m_MuzzleOffset.RotateY(m_Anchor.GetYaw());
		m_MuzzleOffset.Add(m_Anchor.GetPosition());
		
		return m_MuzzleOffset;
	}
	
	protected void NextMuzzle()
	{
		m_MuzzleIndex ++;
		
		if(m_MuzzleIndex >= m_MuzzleOffsets.size())
			m_MuzzleIndex = 0;
	}

	public Vector3 GetMuzzle()
	{
		return m_MuzzleOffsets.elementAt(m_MuzzleIndex);
	}
	
	protected void AddMuzzle(double I, double J, double K)
	{
		Vector3 Muzzle = new Vector3(I,J,K);
		m_MuzzleOffsets.add(Muzzle);
		
		if(m_HasLasers)
		{		
			LaserPointer Laser = new LaserPointer(this, Muzzle);
			m_Lasers.add(Laser);
		}
	}
	
	protected void Fire()
	{
		while(m_FireMode.ShouldFire())
		{
			m_BulletManager.CreateBullet(m_ProjectileTemplate);
			m_FireMode.NotifyOfFire();
			m_PostFireAction.Update();

            m_IsFiring = true;
		}
	}

	public void ResetMuzzleIndex()
	{
		m_MuzzleIndex = 0;
	}
	
	public void LasersOn()
	{
		for(LaserPointer Pointer : m_Lasers)
            Pointer.On();
	}
	
	public void LasersOff()
	{
		for(LaserPointer Pointer : m_Lasers)
			Pointer.Off();
	}

    public Colour GetBaseColour()
    {
        return m_Anchor.GetBaseColour();
    }

    public Colour GetAltColour()
    {
        return m_Anchor.GetAltColour();
    }

	public Vector3 GetVelocity()
	{
		return m_Anchor.GetVelocity();
	}
	
	public Vector3 GetForward()
	{
		return m_Anchor.GetForward();
	}
	
	public Vector3 GetPosition()
	{
		return m_Anchor.GetPosition();
	}

	public double GetAccuracy()
	{
		return m_Accuracy;
	}

	public AffiliationKey GetAffiliation()
	{
		return m_Anchor.GetAffiliation();
	}

	public void AddChild(GameObject obj)
	{
		m_Anchor.AddChild(obj);
	}
	
	public double GetOrientation()
	{
		return m_Anchor.GetYaw();
	} 
	
	public int GetNumMuzzles()
	{
		return m_MuzzleOffsets.size();
	}

	public Vehicle GetAnchor()
	{
		return m_Anchor;
	}
	
	public boolean IsTriggerPulled()
	{
		return m_TriggerPulled;
	}
	
	public int GetMuzzleIndex()
	{
		return m_MuzzleIndex;
	}

    public double GetDrain()
    {
        return m_Drain;
    }

    public Boolean IsFiring()
    {
        return m_IsFiring;
    }
}
