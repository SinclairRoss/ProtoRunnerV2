package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;

public class AIController 
{
	///// AI Subsystems.
    private AIBehaviour m_Behaviour;
	private SituationalAwareness m_SituationalAwareness;
	private NavigationControl m_NavigationControl;
    private EvasionControl m_EvasionControl;
	private FireControl m_FireControl;

	private Vehicle m_Anchor;
	private Vehicle m_Leader;

    private AffiliationKey m_TeamAffiliation;

	public AIController(Vehicle anchor, VehicleManager vManager, BulletManager bManager, NavigationalBehaviourInfo navInfo, AIBehaviours behaviour, FireControlBehaviour fireBehaviour, TargetingBehaviour targetingBehaviour)
	{
		m_Anchor = anchor;

        m_TeamAffiliation = m_Anchor.GetAffiliation();

		///// AI Subsystems.
		m_SituationalAwareness 	= new SituationalAwareness(this, vManager, bManager, targetingBehaviour);
        m_NavigationControl 	= new NavigationControl(this, navInfo);
        m_EvasionControl        = new EvasionControl(this);
		m_FireControl 			= GetFireControlBehaviour(fireBehaviour);
        m_Behaviour             = GetBehaviour(behaviour, vManager);

        AIUpdateScheduler.Instance().RegisterListener(m_SituationalAwareness);
    }

    private AIBehaviour GetBehaviour(AIBehaviours behaviour, VehicleManager vManager)
    {
        switch(behaviour)
        {
            case EngageTarget:
                return new AIBehaviour_EngageTarget(this);
            case Encircle:
                return new AIBehaviour_Encircle(this);
            case FollowTheLeader:
                return new AIBehaviour_FollowTheLeader(this);
            case StickWithThePack:
                return new AIBehaviour_StickWithThePack(this, vManager);
            case TentacleSnare:
                return new AIBehaviour_TentacleSnare(this);
            default:
                return null;
        }
    }

    private FireControl GetFireControlBehaviour(FireControlBehaviour behaviour)
    {
        switch(behaviour)
        {
            case Standard:
                return new FireControl_Standard(this);
            case Telegraphed:
                return new FireControl_Telegraphed(this);
            case LaserSpinner:
                return new FireControl_LaserSpinner(this);
            case MissileLauncher:
                return new FireControl_MissileLauncher(this);
            case None:
                return new FireControl_None(this);
            default:
                return null;
        }
    }

	public void Update(double deltaTime)
	{
        CheckLeader();

        if(AIUpdateScheduler.Instance().CanUpdate(m_SituationalAwareness))
        {
            m_SituationalAwareness.Update();
        }

	    m_NavigationControl.SetGoal(m_Behaviour.GetNavigationCoordinates());
        m_NavigationControl.Update();
        m_EvasionControl.Update();
		m_FireControl.Update(deltaTime);
	}

    public void CheckLeader()
    {
        if(m_Leader != null && !m_Leader.IsValid())
        {
            m_Leader = null;
        }
    }

    public AffiliationKey GetTeamAffiliationKey()
    {
        return m_TeamAffiliation;
    }

    public AffiliationKey GetEnemyAffiliationKey()
    {
        switch (m_TeamAffiliation)
        {
            case BlueTeam:
                return AffiliationKey.RedTeam;
            case RedTeam:
                return AffiliationKey.BlueTeam;
            case Neutral:
            default:
                return AffiliationKey.Neutral;

        }
    }

	public Vehicle GetAnchor()
	{
		return m_Anchor;
	}

	public NavigationControl GetNavigationControl()
	{
		return m_NavigationControl;
	}

    public EvasionControl GetEvasionControl()
    {
        return m_EvasionControl;
    }

	public SituationalAwareness GetSituationalAwareness()
	{
		return m_SituationalAwareness;
	}

    public Vehicle GetLeader()
    {
        return m_Leader;
    }

    public void SetLeader(Vehicle leader)
    {
        m_Leader = leader;
    }

    public void CleanUp()
    {
        AIUpdateScheduler.Instance().DeregisterComponent(m_SituationalAwareness);
    }
}