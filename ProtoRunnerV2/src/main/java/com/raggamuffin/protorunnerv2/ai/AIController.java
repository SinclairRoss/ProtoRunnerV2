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

	private AffiliationKey m_EnemyFaction;
	private Vehicle m_Anchor;
	private Vehicle m_Leader;

	public AIController(Vehicle anchor, VehicleManager vManager, BulletManager bManager, AIBehaviours behaviour, FireControlBehaviour fireBehaviour)
	{
		m_Anchor = anchor;

        m_EnemyFaction = (m_Anchor.GetAffiliation() == AffiliationKey.BlueTeam) ?
                            AffiliationKey.RedTeam :
                            AffiliationKey.BlueTeam;

		///// AI Subsystems.
		m_SituationalAwareness 	= new SituationalAwareness(this, vManager, bManager);
        m_NavigationControl 	= new NavigationControl(this);
        m_EvasionControl        = new EvasionControl(this);
		m_FireControl 			= GetFireControlBehaviour(fireBehaviour);
        m_Behaviour             = GetBehaviour(behaviour);
    }

    private AIBehaviour GetBehaviour(AIBehaviours behaviour)
    {
        switch(behaviour)
        {
            case EngageTarget:
                return new AIBehaviour_EngageTarget(this);
            case Encircle:
                return new AIBehaviour_Encircle(this);
            case FollowTheLeader:
                return new AIBehaviour_FollowTheLeader(this);
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
            case BeamSweep:
                return new FireControl_BeamSweep(this);
            case LaserSpinner:
                return new FireControl_LaserSpinner(this);
            case None:
                return new FireControl_None(this);
            default:
                return null;
        }
    }

	public void Update(double deltaTime)
	{
        CheckLeader();

        m_SituationalAwareness.Update();
	    m_NavigationControl.SetGoal(m_Behaviour.GetNavigationCoordinates());
        m_NavigationControl.Update();
        m_EvasionControl.Update(deltaTime);
		m_FireControl.Update(deltaTime);
	}

    public void CheckLeader()
    {
        if(m_Leader == null)
            return;

        if(m_Leader.IsValid())
            return;

        m_Leader = null;
    }

	public Vehicle GetAnchor()
	{
		return m_Anchor;
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

    public Vehicle GetLeader()
    {
        return m_Leader;
    }

    public void SetLeader(Vehicle leader)
    {
        m_Leader = leader;
    }
}