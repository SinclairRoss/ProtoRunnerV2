package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.ai.BehaviourControl.BehaviourStates;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;

public class AIController 
{
	///// AI Subsystems.
	private SituationalAwareness m_SituationalAwareness;
	private NavigationControl m_NavigationControl;
	private FireControl m_FireControl;
	private BehaviourControl m_BehaviourControl;

	private AffiliationKey m_EnemyFaction;
	private Vehicle m_Anchor;
	private Vehicle m_Leader;
	
	public AIController(Vehicle anchor, VehicleManager vManager, BulletManager bManager)
	{
		m_Anchor = anchor;
		
		m_Leader = null;
		
		switch(m_Anchor.GetAffiliation())
		{
			case RedTeam:
				m_EnemyFaction = AffiliationKey.BlueTeam;
				break;
			case BlueTeam:
				m_EnemyFaction = AffiliationKey.RedTeam;
				break;
			default:
				break;
		}

		///// AI Subsystems.
		m_SituationalAwareness 	= new SituationalAwareness(this, vManager, bManager);
		m_NavigationControl 	= new NavigationControl(this);
		m_FireControl 			= new FireControl(this);
		m_BehaviourControl		= new BehaviourControl(this);
	}

	public void Update(double DeltaTime)
	{
		m_SituationalAwareness.Update();
		m_BehaviourControl.Update(DeltaTime);
		m_NavigationControl.Update(DeltaTime);
		m_FireControl.Update(DeltaTime);
	}

	public Vehicle GetAnchor()
	{
		return m_Anchor;
	}
	
	public void SetLeader(Vehicle Leader)
	{
		m_Leader = Leader;
		m_BehaviourControl.SetState(BehaviourStates.Submissive);
	}
	
	public void RemoveLeader()
	{
		m_Leader = null;
		m_BehaviourControl.SetState(BehaviourStates.Independent);
	}
	
	public Vehicle GetLeader()
	{
		return m_Leader;
	}
	
	public AffiliationKey GetEnemyAffiliation()
	{
		return m_EnemyFaction;
	}
	
	public NavigationControl GetNavigationControl()
	{
		return m_NavigationControl;
	}
	
	public SituationalAwareness GetSituationalAwareness()
	{
		return m_SituationalAwareness;
	}
}
