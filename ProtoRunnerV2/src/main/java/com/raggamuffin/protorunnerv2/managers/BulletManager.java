package com.raggamuffin.protorunnerv2.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Explosion;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.ProjectileTemplate;

public class BulletManager
{
	private ArrayList<Projectile> m_ActiveBullets;
	private ArrayList<Projectile> m_InvalidBullets;
	
	private ArrayList<Explosion> m_Explosions;
	
	private GameLogic m_Game;
	
	public BulletManager(GameLogic Game)
	{
		m_Game = Game;
		
		m_ActiveBullets 	= new ArrayList<>();
		m_InvalidBullets 	= new ArrayList<>();
		
		m_Explosions = new ArrayList<>();
	}

	public void Update(double deltaTime)
	{
		for(Iterator<Projectile> Iter = m_ActiveBullets.iterator(); Iter.hasNext();)
		{
			Projectile Temp = Iter.next();
			
			if(Temp.IsValid())
			{
				Temp.Update(deltaTime);
			}
			else
			{
				m_InvalidBullets.add(Temp);
				m_Game.RemoveTrailFromRenderer(Temp);
				Iter.remove();
			}
		}
		
		for(Iterator<Explosion> Iter = m_Explosions.iterator(); Iter.hasNext();)
		{
			Explosion Temp = Iter.next();
			
			if(Temp.IsValid())
			{
				Temp.Update(deltaTime);
			}
			else
			{
				m_Game.RemoveTrailFromRenderer(Temp);
				Iter.remove();
			}
		}
	}
	
	// Creates or recycles a projectile and activates it.
	public void CreateBullet(ProjectileTemplate template)
	{
		Projectile NewBullet = null;

		if(m_InvalidBullets.size() > 0)
		{
			for(Iterator<Projectile> Iter = m_InvalidBullets.iterator(); Iter.hasNext();)
			{
				Projectile Invalid 	= Iter.next();
				NewBullet 			= Invalid;
				Iter.remove();
			}
		}

		if(NewBullet == null)
		{
			NewBullet = new Projectile();
		}
		
		NewBullet.Activate(template);
		m_ActiveBullets.add(NewBullet);
		m_Game.AddObjectToRenderer(NewBullet);
	}
	
	public void CreateExplosion(Vector3 position, AffiliationKey affilitation, Colour colour, double maxSize, double rateOfExpansion)
	{
		Explosion exp = new Explosion(m_Game.GetPubSubHub(), m_Game.GetGameAudioManager(), colour, position, affilitation, maxSize, rateOfExpansion);
		m_Explosions.add(exp);
		m_Game.AddObjectToRenderer(exp);
	}
	
	public void Wipe()
	{
		for(Iterator<Projectile> Iter = m_ActiveBullets.iterator(); Iter.hasNext();)
		{
			Iter.next().ForceInvalidation();
		}
		
		for(Iterator<Explosion> Iter = m_Explosions.iterator(); Iter.hasNext();)
		{
			Iter.next().ForceInvalidation();
		}
	}
	
	public ArrayList<Projectile> GetActiveBullets()
	{
		return m_ActiveBullets;
	}
	
	public ArrayList<Explosion> GetExplosions()
	{
		return m_Explosions;
	}
}
