package com.raggamuffin.protorunnerv2.managers;

import java.util.ArrayList;
import java.util.Iterator;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Laser;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Missile;
import com.raggamuffin.protorunnerv2.weapons.Projectile_PlasmaShot;
import com.raggamuffin.protorunnerv2.weapons.Weapon;

public class BulletManager
{
	private ArrayList<Projectile> m_ActiveBullets;
	
	private GameLogic m_Game;
	
	public BulletManager(GameLogic Game)
	{
		m_Game = Game;
		
		m_ActiveBullets 	= new ArrayList<>();
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
	}

    public void CreateProjectile(Weapon origin)
    {
        Projectile newProjectile;

        switch(origin.GetProjectileType())
        {
            case PlasmaShot:
                newProjectile = new Projectile_PlasmaShot(origin);
                break;
            case Missile:
                newProjectile = new Projectile_Missile(origin);
                break;
            case Laser:
                newProjectile = new Projectile_Laser(origin, m_Game);
                break;
            case Flare:
                newProjectile = new Projectile_PlasmaShot(origin);
                break;
            default:
                newProjectile = new Projectile_PlasmaShot(origin);
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
}
