// Author:	Sinclair Ross.
// Date:	22/10/2014.

package com.raggamuffin.protorunnerv2.weapons;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
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

    protected ProjectileType m_ProjectileType;

    protected Vector3 m_Target;
		
	protected AudioClips m_AudioClip;
	
	protected FireControl m_FireMode;

	protected double m_Damage;
	protected double m_MuzzleVelocity;
	protected double m_Accuracy;
	protected double m_LifeSpan;

	protected ArrayList<WeaponBarrel> m_WeaponBarrels;
	private int m_MuzzleIndex;

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
		m_MuzzleVelocity = 1.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 1.0;

		m_WeaponBarrels = new ArrayList<>();
		m_MuzzleIndex = 0;
		
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
			NextBarrel();
			
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

	public void GetFirePosition(Vector3 out)
	{
		WeaponBarrel barrel = m_WeaponBarrels.get(m_MuzzleIndex);
		out.SetVector(barrel.GetPosition());

		out.RotateY(m_Anchor.GetYaw());
		out.Add(m_Anchor.GetPosition());
	}

    public WeaponBarrel GetActiveWeaponBarrel()
    {
        return m_WeaponBarrels.get(m_MuzzleIndex);
    }
	
	protected void NextBarrel()
	{
		m_MuzzleIndex ++;
		
		if(m_MuzzleIndex >= m_WeaponBarrels.size())
			m_MuzzleIndex = 0;
	}
	
	protected void AddBarrel(double x, double y, double z)
	{
		AddBarrel(x, y, z, 0);
	}

	protected void AddBarrel(double x, double y, double z, double rotation)
	{
		m_WeaponBarrels.add(new WeaponBarrel(x, y, z, rotation));
	}

	protected void Fire()
	{
		while(m_FireMode.ShouldFire())
		{
			m_BulletManager.CreateProjectile(this);
			m_FireMode.NotifyOfFire();

            m_IsFiring = true;
		}
	}

    protected void CalculateProjectileHeading(Vector3 out)
    {
        out.SetVector(m_Anchor.GetForward());
    }

	public void ResetMuzzleIndex()
	{
		m_MuzzleIndex = 0;
	}

    public double GetBaseDamage()
    {
        return m_Damage;
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
		return m_WeaponBarrels.size();
	}

	public Vehicle GetAnchor()
	{
		return m_Anchor;
	}
	
	public boolean IsTriggerPulled()
	{
		return m_TriggerPulled;
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
