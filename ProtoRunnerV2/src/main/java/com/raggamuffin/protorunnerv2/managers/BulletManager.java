package com.raggamuffin.protorunnerv2.managers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Flare;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Laser;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Laser_Experimental;
import com.raggamuffin.protorunnerv2.weapons.Projectile_LaserVampire;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Missile;
import com.raggamuffin.protorunnerv2.weapons.Projectile_PlasmaShot;
import com.raggamuffin.protorunnerv2.weapons.Weapon;

public class BulletManager
{
	private ArrayList<Projectile> m_ActiveBullets;
    private ArrayList<Projectile_Flare> m_ActiveFlares;
	
	private GameLogic m_Game;
	
	public BulletManager(GameLogic Game)
	{
		m_Game = Game;
		
		m_ActiveBullets = new ArrayList<>();
        m_ActiveFlares = new ArrayList<>();
	}

	public void Update(double deltaTime)
	{
        for(Iterator<Projectile> iter = m_ActiveBullets.iterator(); iter.hasNext();)
        {
            Projectile temp = iter.next();

            if(temp.IsValid())
            {
                temp.Update(deltaTime);
            }
            else
            {
                RemoveObjectFromRenderer(temp);
                iter.remove();
            }
        }

        for(Iterator<Projectile_Flare> iter = m_ActiveFlares.iterator(); iter.hasNext();)
        {
            Projectile_Flare temp = iter.next();

            if(temp.IsValid())
            {
                temp.Update(deltaTime);
            }
            else
            {
                RemoveObjectFromRenderer(temp);
                iter.remove();
            }
        }
	}

    public void CreateProjectile(Weapon origin)
    {
        Projectile newProjectile = null;

        switch(origin.GetProjectileType())
        {
            case PlasmaShot:
                newProjectile = new Projectile_PlasmaShot(origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetFiringSpeed(), origin.GetAffiliation());
                break;
            case Missile:
                newProjectile = new Projectile_Missile(origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetMuzzleIndex(), origin.GetAffiliation(), origin, m_Game);
                break;
            case Laser:
                newProjectile = new Projectile_Laser(origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetAffiliation(), origin, m_Game);
                break;
            case LaserVampire:
                newProjectile = new Projectile_LaserVampire(origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetAffiliation(), origin.GetAnchor(), origin, m_Game);
                break;
            case Flare:
                Projectile_Flare flare = new Projectile_Flare(origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetAltColour(), origin.GetBaseDamage(), origin.GetAffiliation(), m_Game);
                m_ActiveFlares.add(flare);
                newProjectile = flare;
                break;
            default:
                Log.e("BulletManager", "Bullet type not found: " + origin.GetProjectileType().toString());
                break;
        }

        AddProjectile(newProjectile);
    }

    public void AddProjectile(Projectile projectile)
    {
        m_ActiveBullets.add(projectile);
        AddObjectToRenderer(projectile);
    }

	public void Wipe()
	{
		for(Iterator<Projectile> iter = m_ActiveBullets.iterator(); iter.hasNext();)
		{
			iter.next().ForceInvalidation();
		}
	}

    private void AddObjectToRenderer(Projectile proj)
    {
        m_Game.AddObjectToRenderer(proj);
    }

    private void RemoveObjectFromRenderer(Projectile proj)
    {
        m_Game.RemoveGameObjectFromRenderer(proj);
    }
	
	public ArrayList<Projectile> GetActiveBullets()
	{
		return m_ActiveBullets;
	}

    public ArrayList<Projectile_Flare> GetActiveFlares()
    {
        return m_ActiveFlares;
    }
}
