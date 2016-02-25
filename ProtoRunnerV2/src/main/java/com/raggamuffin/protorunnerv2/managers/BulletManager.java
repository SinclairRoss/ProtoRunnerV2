package com.raggamuffin.protorunnerv2.managers;

import java.util.ArrayList;
import java.util.Iterator;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.Projectile_EnergyBall;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Flare;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Laser;
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
        for(Iterator<Projectile> Iter = m_ActiveBullets.iterator(); Iter.hasNext();)
        {
            Projectile temp = Iter.next();

            if(temp.IsValid())
            {
                temp.Update(deltaTime);
            }
            else
            {
                RemoveObjectFromRenderer(temp);
                Iter.remove();
            }
        }

        for(Iterator<Projectile_Flare> Iter = m_ActiveFlares.iterator(); Iter.hasNext();)
        {
            Projectile_Flare temp = Iter.next();

            if(temp.IsValid())
            {
                temp.Update(deltaTime);
            }
            else
            {
                RemoveObjectFromRenderer(temp);
                Iter.remove();
            }
        }
	}

    public void CreateProjectile(Weapon origin)
    {
        Projectile newProjectile;

        switch(origin.GetProjectileType())
        {
            case PlasmaShot:
                newProjectile = new Projectile_PlasmaShot(origin, m_Game);
                break;
            case EnergyBall:
                newProjectile = new Projectile_EnergyBall(origin);
                break;
            case Missile:
                newProjectile = new Projectile_Missile(origin, m_Game, origin.GetMuzzleIndex());
                break;
            case Laser:
                newProjectile = new Projectile_Laser(origin, m_Game);
                break;
            case LaserVampire:
                newProjectile = new Projectile_LaserVampire(origin, m_Game);
                break;
            case Flare:
                Projectile_Flare flare = new Projectile_Flare(origin, m_Game);
                m_ActiveFlares.add(flare);
                newProjectile = flare;
                break;
            default:
                newProjectile = new Projectile_PlasmaShot(origin, m_Game);
                m_ActiveBullets.add(newProjectile);
                break;
        }

        m_ActiveBullets.add(newProjectile);
        AddObjectToRenderer(newProjectile);
    }

	public void Wipe()
	{
		for(Iterator<Projectile> Iter = m_ActiveBullets.iterator(); Iter.hasNext();)
		{
			Iter.next().ForceInvalidation();
		}
	}

    private void AddObjectToRenderer(Projectile proj)
    {
        switch (proj.GetModel())
        {
            case PlasmaShot:
                m_Game.AddBulletToRenderer(proj);
            default:
                m_Game.AddObjectToRenderer(proj);
        }
    }

    private void RemoveObjectFromRenderer(Projectile proj)
    {
        switch (proj.GetModel())
        {
            case PlasmaShot:
                m_Game.RemoveBulletFromRenderer(proj);
            default:
                m_Game.RemoveGameObjectFromRenderer(proj);
        }
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
