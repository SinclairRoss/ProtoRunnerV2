package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.Projectile_HomingLaser;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Laser;
import com.raggamuffin.protorunnerv2.weapons.Projectile_PlasmaShot;
import com.raggamuffin.protorunnerv2.weapons.Projectile_TelegraphedPlasmaShot;
import com.raggamuffin.protorunnerv2.weapons.Weapon;

import java.util.ArrayList;

public class BulletManager
{
	private ArrayList<Projectile> m_ActiveBullets;
	
	private GameLogic m_Game;
	
	public BulletManager(GameLogic Game)
	{
		m_Game = Game;
		
		m_ActiveBullets = new ArrayList<>();
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
                --i;
            }
        }
	}

    public Projectile CreateProjectile(Weapon origin)
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
            case Laser:
                newProjectile = new Projectile_Laser(m_Game, origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetAffiliation(), origin);
                break;
            case HomingLaser:
                newProjectile = new Projectile_HomingLaser(origin.GetFirePosition(), origin.GetVelocity(), origin.CalculateProjectileHeading(), origin.GetBaseColour(), origin.GetBaseDamage(), origin.GetAffiliation());
                break;
        }

        AddProjectile(newProjectile);

        return newProjectile;
    }

    private void AddProjectile(Projectile projectile)
    {
        m_ActiveBullets.add(projectile);
    }

	public void Wipe()
	{
        for(int i = 0; i < m_ActiveBullets.size(); ++i)
        {
            Projectile bullet = m_ActiveBullets.get(i);

            bullet.CleanUp();
            m_ActiveBullets.remove(bullet);
            --i;
        }
	}
	
	public ArrayList<Projectile> GetActiveBullets()
	{
		return m_ActiveBullets;
	}
}
