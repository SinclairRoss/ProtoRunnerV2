package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class NavigationControl 
{
	private final double ARRIVAL_ANGLE = Math.toRadians(5.0);

	private Vehicle m_Anchor;
	private SituationalAwareness m_SituationalAwareness;

	private Vector3 m_AnchorPosition;
	private Vector3 m_AnchorForward;

	private Boolean m_NavigationActive;

	///// Steering Attributes.
	private Vector3 m_SteeringVector;
	private Vector3 m_ToGoal;
    private Vector3 m_Goal;
	private Vector3 m_Separation;
	private Vector3 m_Alignment;
	private Vector3 m_Cohesion;
	private Vector3 m_CenterOfMass;
	private double m_GoalWeight;
	private double m_SeparationWeight;
	private double m_AlignmentWeight;
	private double m_CohesionWeight;
	
	public NavigationControl(AIController Controller, NavigationalBehaviourInfo navInfo)
	{
		m_Anchor = Controller.GetAnchor();
		m_SituationalAwareness = Controller.GetSituationalAwareness();
		
		m_AnchorPosition = m_Anchor.GetPosition();
		m_AnchorForward = m_Anchor.GetForward();

        m_NavigationActive = true;

		m_SteeringVector = new Vector3();
		m_ToGoal = new Vector3();
        m_Goal = new Vector3();
        m_Separation = new Vector3();
		m_Alignment = new Vector3();
		m_Cohesion = new Vector3();
		m_CenterOfMass = new Vector3();
		m_GoalWeight = navInfo.GetGoalWeight(); // 0.4
		m_SeparationWeight = navInfo.GetSeperationWeight(); // 1.0
		m_AlignmentWeight = navInfo.GetAlignmentWeight(); // 0.7
		m_CohesionWeight = navInfo.GetCohesionWeight(); // 0.6
	}
	
	public void Update()
	{
        if(m_NavigationActive)
        {
            CalculateToGoal();
            CalculateSeparation();
            CalculateAlignment();
            CalculateCohesion();
            CalculateSteeringVector();

			double turnRate = CalculateTurnRate();
            m_Anchor.SetTurnRate(turnRate);
        }
	}

	private void CalculateToGoal()
	{
		m_ToGoal.SetAsDifference(m_AnchorPosition, m_Goal); 	// Calculate vector pointing to destination.
		m_ToGoal.Normalise();
		m_ToGoal.Scale(m_GoalWeight);
	}
	
	private void CalculateSeparation()
	{
		m_Separation.SetVector(0.0);

		ArrayList<Vehicle> surroundingVehicles = m_SituationalAwareness.GetSurroundingAwarenessSensor().GetVehiclesInNeighbourhood();

		int numSurroundingVehicles = surroundingVehicles.size();
		for(int i = 0; i < numSurroundingVehicles; ++i)
		{
			Vehicle obstacle = surroundingVehicles.get(i);
            Vector3 obstaclePosition = obstacle.GetPosition();

            double x = m_AnchorPosition.X - obstaclePosition.X;
            double y = m_AnchorPosition.Y - obstaclePosition.Y;
            double z = m_AnchorPosition.Z - obstaclePosition.Z;
			
			m_Separation.Add(x, y, z);
		}
		
		m_Separation.Normalise();
		m_Separation.Scale(m_SeparationWeight);
	}
	
	private void CalculateAlignment()
	{
		m_Alignment.SetVector(0.0);

		// Get surrounding vehicles.
        ArrayList<Vehicle> surroundingVehicles = m_SituationalAwareness.GetSurroundingAwarenessSensor().GetFriendliesInNeighbourhood();

        // Loop through each surrounding vehicle and create seperation vector.
        int numSquaddies = surroundingVehicles.size();
        for(int i = 0; i < numSquaddies; ++i)
        {
            Vehicle squaddie = surroundingVehicles.get(i);
			m_Alignment.Add(squaddie.GetForward());
		}

		m_Alignment.Normalise();
		m_Alignment.Scale(m_AlignmentWeight);
	}
	
	private void CalculateCohesion()
	{
		m_Cohesion.SetVector(0.0);
		m_CenterOfMass.SetVector(0.0);

		// Get surrounding vehicles.
        ArrayList<Vehicle> surroundingVehicles = m_SituationalAwareness.GetSurroundingAwarenessSensor().GetFriendliesInNeighbourhood();
		
		// If there is no surrounding vehicles break from function.
		// Not doing this could result in cohesion vector pointing towards origin instead of being 0.
		if(surroundingVehicles.size() > 0)
		{
			// Loop through each surrounding vehicle and create seperation vector.
            int numSquaddies = surroundingVehicles.size();
            for(int i = 0; i < numSquaddies; ++i)
			{
                Vehicle squaddie = surroundingVehicles.get(i);
				m_CenterOfMass.Add(squaddie.GetPosition());
			}

			// Get average position of squaddies as center of mass.
			double scale = 1.0 / surroundingVehicles.size();
			m_CenterOfMass.Scale(scale);

			// Set cohesion vector.
			m_Cohesion.SetAsDifference(m_AnchorPosition, m_CenterOfMass);
			m_Cohesion.Normalise();
			m_Cohesion.Scale(m_CohesionWeight);
		}
	}
	
	private void CalculateSteeringVector()
	{
		m_SteeringVector.SetVector(0.0);
		m_SteeringVector.Add(m_ToGoal);
		m_SteeringVector.Add(m_Separation);
		m_SteeringVector.Add(m_Alignment);
		m_SteeringVector.Add(m_Cohesion);
		m_SteeringVector.Normalise();
	}
	
	private double CalculateTurnRate()
	{
		// Calculate the difference in bearing to point towards the target.
		double deltaRadians = Vector3.RadiansBetween(m_AnchorForward, m_SteeringVector);

		// Calculate Turn Rate	
		double normalisedTurnRate = MathsHelper.Normalise(deltaRadians, 0.0, ARRIVAL_ANGLE);

		// Calculate Turn Direction
		if(Vector3.Determinant(m_AnchorForward,  m_SteeringVector) < 0.0)
        {
            normalisedTurnRate *= -1;
        }

		return normalisedTurnRate;
	}

    public void SetGoal(Vector3 goal)
    {
        m_Goal.SetVector(goal);
    }

    public void Activate()
    {
        m_NavigationActive = true;
    }

    public void Deactivate()
	{
        m_NavigationActive = false;
    }
}
