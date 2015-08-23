package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;

public class IndependentBehaviour extends BehaviourState
{
    private ArrayList<Goal> m_Goals;
    private Goal m_Goal;

	public IndependentBehaviour(AIController controller, AIGoalSet goalSet)
	{
		super(controller);

        m_Goals = goalSet.GetGoals(controller);
	}
	
	@Override
	public void Update()
	{
        EvaluateGoals();

        m_NavController.SetGoal(m_Goal.GetGoalCoords());
	}

    private void EvaluateGoals()
    {
        double maxUtility = -Double.MAX_VALUE;

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
