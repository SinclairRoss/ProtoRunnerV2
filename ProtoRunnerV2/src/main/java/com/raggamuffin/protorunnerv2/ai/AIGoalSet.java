package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;

public class AIGoalSet
{
    private GoalState[] m_Goals;

    public AIGoalSet(GoalState... goals)
    {
        m_Goals = goals;
    }

    public ArrayList<Goal> GetGoals(AIController controller)
    {
        ArrayList<Goal> goals = new ArrayList<>();

        for(GoalState goalState : m_Goals)
        {
            switch(goalState)
            {
                case EngageTarget:
                    goals.add(new Goal_EngageTarget(controller));
                    break;
                case Flee:
                    goals.add(new Goal_Flee(controller));
                    break;
                case Encircle:
                    goals.add(new Goal_Encircle(controller));
                    break;
            }
        }

        return goals;
    }
}
