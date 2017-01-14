package com.raggamuffin.protorunnerv2.managers;

import android.util.Log;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Flare;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Laser;
import com.raggamuffin.protorunnerv2.weapons.Projectile_LaserVampire;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Missile;
import com.raggamuffin.protorunnerv2.weapons.Projectile_PlasmaShot;
import com.raggamuffin.protorunnerv2.weapons.Projectile_TelegraphedPlasmaShot;
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
        for(int i = 0; i < m_ActiveBullets.size(); ++i)
        {
            Projectile bullet = m_ActiveBullets.get(i);
            if(bullet.IsValid())
            {
                bullet.Update(deltaTime);
            }
            else
            {
                bullet.CleanUp();
                m_ActiveBullets.remove(bullet);
                m_Game.RemoveGameObjectFromRenderer(bullet);
                --i;
            }
        }

        for(int i = 0; i < m_ActiveFlares.size(); ++i)
        {
            Projectile_Flare flare = m_ActiveFlares.get(i);

            if(flare.IsValid())
            {
                flare.Update(deltaTime);
            }
            else
            {
                flare.CleanUp();
                m_ActiveFlares.remove(flare);
                m_Game.RemoveGameObjectFromRenderer(flare);
                --i;
            }
        }
	}

    public void CreateProjectile(Weapon origin)
    {
        Projectile newProjectile = null;

        switch(origin.GetProjectileType())
        {
            case PlasmaShot:
                newProjectile = new Projectile_PlasmaShot(m_Game, origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetFiringSpeed(), origin.GetAffiliation());
                break;
            case PlasmaShot_Telegraphed:
                newProjectile = new Projectile_TelegraphedPlasmaShot(m_Game, origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetFiringSpeed(), origin.GetAffiliation());
                break;
            case Missile:
                newProjectile = new Projectile_Missile(m_Game, origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetMuzzleIndex(), origin.GetAffiliation(), origin);
                break;
            case Laser:
                newProjectile = new Projectile_Laser(m_Game, origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetAffiliation(), origin);
                break;
            case LaserVampire:
                newProjectile = new Projectile_LaserVampire(m_Game, origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetAffiliation(), origin.GetAnchor(), origin);
                break;
            case Flare:
                Projectile_Flare flare = new Projectile_Flare(m_Game, origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetAffiliation());
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
        for (Projectile proj : m_ActiveBullets)
        {
            proj.ForceInvalidation();
        }

        for(Projectile proj : m_ActiveFlares)
        {
            proj.ForceInvalidation();
        }
	}

    private void AddObjectToRenderer(Projectile proj)
    {
        m_Game.AddObjectToRenderer(proj);
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
