package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;

public class IndependentBehaviour extends BehaviourState
{
    private ArrayList<Goal> m_Goals;
    private Goal m_Goal;

	public IndependentBehaviour(AIController controller)
	{
		super(controller);

        m_Goals = new ArrayList<Goal>();
        m_Goals.add(new Goal_EngageTarget(m_Controller));
        m_Goals.add(new Goal_Flee(m_Controller));
	}
	
	@Override
	public void Update()
	{
        EvaluateGoals();
        m_NavController.SetGoal(m_Goal.GetGoalCoords());
	}

    private void EvaluateGoals()
    {
        double maxUtility = Double.MIN_VALUE;

        for(Goal goal : m_Goals)
        {
            double utility = goal.CalculateUtility();

            if(utility <= maxUtility)
                continue;

            m_Goal = goal;
            maxUtility = utility;
        }
    }

	@Override
	public void InitialiseState() 
	{

	}
}
