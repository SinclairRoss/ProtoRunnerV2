// Author:	Sinclair Ross.
// Date:	22/10/2014.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.AudioEmitter;
import com.raggamuffin.protorunnerv2.audio.AudioEmitter_Point;
import com.raggamuffin.protorunnerv2.audio.EAudioRepeatBehaviour;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public abstract class Weapon
{
    protected Vehicle m_Anchor;
    protected GameLogic m_Game;
    protected BulletManager m_BulletManager;
    protected ParticleManager m_ParticleManager;
    protected GameAudioManager m_AudioService;

    protected ProjectileType m_ProjectileType;

    protected Vector3 m_Target;

    protected FireControl m_FireMode;

    protected double m_Damage;
    protected double m_FiringSpeed;
    protected double m_Accuracy;
    protected double m_LifeSpan;

    protected WeaponComponent m_WeaponComponent;

    protected ArrayList<WeaponBarrel> m_WeaponBarrels;
    private int m_MuzzleIndex;

    protected boolean m_TriggerPulled;
    protected boolean m_IsFiring;

    protected EquipmentType m_EquipmentType;

    private AudioEmitter m_AudioEmitter;

    public Weapon(Vehicle anchor, GameLogic game, AudioClips fireClip)
    {
        m_Anchor = anchor;
        m_Game = game;
        m_BulletManager = m_Game.GetBulletManager();
        m_ParticleManager = m_Game.GetParticleManager();
        m_AudioService = m_Game.GetGameAudioManager();

        m_ProjectileType = ProjectileType.PlasmaShot;

        m_Target = anchor.GetForward();

        m_AudioEmitter = new AudioEmitter_Point(m_Anchor, m_Game.GetGameAudioManager(), fireClip, EAudioRepeatBehaviour.Single);

        m_FireMode = null;

        m_Damage = 1.0;
        m_FiringSpeed = 1.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 1.0;

        m_WeaponComponent = InitialiseWeaponComponent(EWeaponComponents.None);

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
        m_WeaponComponent.Update(deltaTime);

        if (m_FireMode.ShouldFire())
        {
            Fire();
            NextBarrel();

            m_AudioEmitter.Start();
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
    {}

    public void WeaponEquipped()
    {}

    public Vector3 GetFirePosition()
    {
        WeaponBarrel barrel = m_WeaponBarrels.get(m_MuzzleIndex);
        Vector3 out = new Vector3(barrel.GetPosition());
        out.RotateY(m_Anchor.GetForward().Yaw());
        out.Add(m_Anchor.GetPosition());

        return out;
    }

    public WeaponBarrel GetActiveWeaponBarrel()
    {
        return m_WeaponBarrels.get(m_MuzzleIndex);
    }

    protected void NextBarrel()
    {
        m_MuzzleIndex++;

        if (m_MuzzleIndex >= m_WeaponBarrels.size())
        {
            m_MuzzleIndex = 0;
        }
    }

    protected void AddBarrel(double x, double y, double z)
    {
        AddBarrel(x, y, z, 0);
    }

    protected void AddBarrel(double x, double y, double z, double rotation)
    {
        WeaponBarrel barrel = new WeaponBarrel(x, y, z, rotation);
        m_WeaponBarrels.add(barrel);
    }

    protected void Fire()
    {
        while (m_FireMode.ShouldFire())
        {
            m_BulletManager.CreateProjectile(this);
            m_FireMode.NotifyOfFire();

            m_IsFiring = true;
        }
    }

    protected WeaponComponent InitialiseWeaponComponent(EWeaponComponents component)
    {
        WeaponComponent weaponComponent;

        switch(component)
        {
            case None:
            {
                weaponComponent = new WeaponComponent_None();
                break;
            }
            case LaserPointer:
            {
                weaponComponent = new WeaponComponent_LaserPointer(m_Game, this);
                break;
            }
            default:
            {
                weaponComponent = new WeaponComponent_None();
                break;
            }
        }

        return weaponComponent;
    }

    public void ActivateComponent()
    {
        m_WeaponComponent.Activate();
    }

    public void DeactivateComponent()
    {
        m_WeaponComponent.Deactivate();
    }

    public Vector3 CalculateProjectileHeading()
    {
        return new Vector3(m_Anchor.GetForward());
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
        return m_Anchor.GetColour();
    }

	public Vector3 GetVelocity()
	{
		return m_Anchor.GetVelocity();
	}
	
	public Vector3 GetForward()
	{
		return m_Target;
	}

	public Vector3 GetPosition()
	{
		return m_Anchor.GetPosition();
	}

	public AffiliationKey GetAffiliation()
	{
		return m_Anchor.GetAffiliation();
	}
	
	public int GetNumMuzzles()
	{
		return m_WeaponBarrels.size();
	}

    public ArrayList<WeaponBarrel> GetWeaponBarrels()
    {
        return m_WeaponBarrels;
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

    public double GetFiringSpeed()
    {
        return m_FiringSpeed;
    }

    public void CleanUp()
    {
        DeactivateComponent();
        CeaseFire();
        m_WeaponComponent.Destroy();
    }
}
