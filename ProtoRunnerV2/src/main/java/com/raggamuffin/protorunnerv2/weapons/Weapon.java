// Author:	Sinclair Ross.
// Date:	22/10/2014.

package com.raggamuffin.protorunnerv2.weapons;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.PostFireAction;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Weapon 
{
	protected Vehicle m_Anchor;
    protected GameLogic m_Game;
	protected BulletManager m_BulletManager;
	protected ParticleManager m_ParticleManager;
	protected GameAudioManager m_AudioService;

    protected ProjectileType m_ProjectileType;

    protected Vector3 m_Target;
		
	protected AudioClips m_AudioClip;
	
	protected FireControl m_FireMode;

	protected double m_Damage;
    protected double m_Drain;
	protected double m_MuzzleVelocity;
	protected double m_Accuracy;
	protected double m_LifeSpan;

	protected ArrayList<Vector3> m_MuzzleOffsets;
	private Vector3 m_MuzzleOffset;
	private int m_MuzzleIndex;

	protected PostFireAction m_PostFireAction;
	
	protected boolean m_TriggerPulled;
    protected boolean m_IsFiring;

    protected EquipmentType m_EquipmentType;

	public Weapon(Vehicle anchor, GameLogic game)
	{
		m_Anchor = anchor;
        m_Game = game;
		m_BulletManager = m_Game.GetBulletManager();
		m_ParticleManager = m_Game.GetParticleManager();
		m_AudioService = m_Game.GetGameAudioManager();

        m_ProjectileType = ProjectileType.PlasmaShot;

        m_Target = anchor.GetForward();

		m_AudioClip = AudioClips.PulseLaser;
		
		m_FireMode = null;

		m_Damage = 1.0;
        m_Drain = 1.0;
		m_MuzzleVelocity = 1.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 1.0;

		m_MuzzleOffsets = new ArrayList<>();
		m_MuzzleOffset = new Vector3();
		m_MuzzleIndex = 0;

		m_PostFireAction = m_Anchor.GetPostFireAction();
		
		m_TriggerPulled = false;
        m_IsFiring = false;

        m_EquipmentType = EquipmentType.Weapon;
	}

    public void SetTargetVector(Vector3 target)
    {
        m_Target = target;
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

    public void WeaponUnequipped()
    {

    }

    public void WeaponEquipped()
    {

    }

	public Vector3 GetFirePosition()
	{
		m_MuzzleOffset.SetVector(m_MuzzleOffsets.get(m_MuzzleIndex));

        m_MuzzleOffset.RotateY(m_Anchor.GetYaw());
		m_MuzzleOffset.Add(m_Anchor.GetPosition());
		
		return m_MuzzleOffset;
	}

    public Vector3 GetMuzzleOffset()
    {
        return m_MuzzleOffsets.get(m_MuzzleIndex);
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
	
	protected void AddMuzzle(double i, double j, double k)
	{
		Vector3 Muzzle = new Vector3(i,j,k);
		m_MuzzleOffsets.add(Muzzle);
	}
	
	protected void Fire()
	{
		while(m_FireMode.ShouldFire())
		{
			m_BulletManager.CreateProjectile(this);
			m_FireMode.NotifyOfFire();
			m_PostFireAction.Update();

            m_IsFiring = true;
		}
	}

	public void ResetMuzzleIndex()
	{
		m_MuzzleIndex = 0;
	}

    public int GetMuzzleIndex()
    {
        return m_MuzzleIndex;
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
		return m_Target;
	}

    public Vector3 GetUp()
    {
        return m_Anchor.GetUp();
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

    public double GetDrain()
    {
        return m_Drain;
    }

    public Boolean IsFiring()
    {
        return m_IsFiring;
    }

    public ProjectileType GetProjectileType()
    {
        return m_ProjectileType;
    }

    public EquipmentType GetEquipmentType()
    {
        return m_EquipmentType;
    }
}
