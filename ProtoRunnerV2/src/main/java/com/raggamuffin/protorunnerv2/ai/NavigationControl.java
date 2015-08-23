package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.vehicles.Vehicle;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class NavigationControl 
{
	private final double ARRIVAL_ANGLE = MathsHelper.DegToRad(5.0);

	private Vehicle m_Anchor;
	private SituationalAwareness m_SituationalAwareness;
	
	private Vector3 m_AnchorPosition;
	private Vector3 m_AnchorForward;

	///// Steering Attributes.
	private Vector3 m_SteeringVector;
	private Vector3 m_ToGoal;
    private Vector3 m_Goal;
	private Vector3 m_Seperation;
	private Vector3 m_Alignment;
	private Vector3 m_Cohesion;
	private Vector3 m_CenterOfMass;
	private double m_GoalWeight;
	private double m_SeperationWeight;
	private double m_AlignmentWeight;
	private double m_CohesionWeight;
	
	public NavigationControl(AIController Controller)
	{
		m_Anchor 				= Controller.GetAnchor();
		m_SituationalAwareness 	= Controller.GetSituationalAwareness();
		
		m_AnchorPosition 		= m_Anchor.GetPosition();
		m_AnchorForward 		= m_Anchor.GetForward();

		m_SteeringVector = new Vector3();
		m_ToGoal 		 = new Vector3();
        m_Goal           = new Vector3();
		m_Seperation 	 = new Vector3();
		m_Alignment 	 = new Vector3();
		m_Cohesion 		 = new Vector3();
		m_CenterOfMass 	 = new Vector3();
		m_GoalWeight		= 0.4;
		m_SeperationWeight	= 1.0;
		m_AlignmentWeight	= 0.7;
		m_CohesionWeight	= 0.6;
	}
	
	public void Update()
	{
		CalculateToGoal();
		CalculateSeparation();
		CalculateAlignment();
		CalculateCohesion(); 
		CalculateSteeringVector();
		
		m_Anchor.SetEngineOutput(CalculateEngineOutput());					 // Set engine output.
		m_Anchor.SetTurnRate(CalculateTurnRate());							 // Set vehicle turn rate.
	}
	
	private void CalculateToGoal()
	{
		m_ToGoal.SetVectorDifference(m_AnchorPosition, m_Goal); 	// Calculate vector pointing to destination.
		m_ToGoal.Normalise();
		m_ToGoal.Scale(m_GoalWeight);
	}
	
	private void CalculateSeparation()
	{
		m_Seperation.SetVector(0.0);

		// Get surrounding vehicles.
		ArrayList<Vehicle> SurroundingVehicles = m_SituationalAwareness.GetSurroundingAwarenessSensor().GetVehiclesInNeighbourhood();

		// Loop through each surrounding vehicle and create seperation vector.
		for(Vehicle Obstacle : SurroundingVehicles)
		{
            Vector3 obstaclePosition = Obstacle.GetPosition();

            double i = m_AnchorPosition.I - obstaclePosition.I;
            double j = m_AnchorPosition.J - obstaclePosition.J;
            double k = m_AnchorPosition.K - obstaclePosition.K;
			
			m_Seperation.Add(i, j, k);
		}
		
		m_Seperation.Normalise();
		m_Seperation.Scale(m_SeperationWeight);
	}
	
	private void CalculateAlignment()
	{
		m_Alignment.SetVector(0.0);

		// Get surrounding vehicles.
        ArrayList<Vehicle> SurroundingVehicles = m_SituationalAwareness.GetSurroundingAwarenessSensor().GetFriendliesInNeighbourhood();

        // Loop through each surrounding vehicle and create seperation vector.
		for(Vehicle Squaddie : SurroundingVehicles)
            m_Alignment.Add(Squaddie.GetForward());

		m_Alignment.Normalise();
		m_Alignment.Scale(m_AlignmentWeight);
	}
	
	private void CalculateCohesion()
	{
		m_Cohesion.SetVector(0.0);
		m_CenterOfMass.SetVector(0.0);

		// Get surrounding vehicles.
        ArrayList<Vehicle> SurroundingVehicles = m_SituationalAwareness.GetSurroundingAwarenessSensor().GetFriendliesInNeighbourhood();
		
		// If there is no surrounding vehicles break from function.
		// Not doing this could result in cohesion vector pointing towards origin instead of being 0.
		if(SurroundingVehicles.size() == 0)
			return;
				
		// Loop through each surrounding vehicle and create seperation vector.
		for(Vehicle Squaddie : SurroundingVehicles)
            m_CenterOfMass.Add(Squaddie.GetPosition());
		
		// Get average position of squaddies as center of mass.
		double scale = 1.0 / SurroundingVehicles.size();
		m_CenterOfMass.Scale(scale);
		
		// Set cohesion vector.
		m_Cohesion.SetVectorDifference(m_AnchorPosition, m_CenterOfMass);
		m_Cohesion.Normalise();
		m_Cohesion.Scale(m_CohesionWeight);
	}
	
	private void CalculateSteeringVector()
	{
		m_SteeringVector.SetVector(0.0);
		m_SteeringVector.Add(m_ToGoal);
		m_SteeringVector.Add(m_Seperation);
		m_SteeringVector.Add(m_Alignment);
		m_SteeringVector.Add(m_Cohesion);
	}
	
	private double CalculateEngineOutput()
	{		
		return 1.0;
	}
	
	private double CalculateTurnRate()
	{
		// Calculate the difference in bearing to point towards the target.
		double deltaRadians = Vector3.RadiansBetween(m_AnchorForward, m_SteeringVector);
		
		// Calculate Turn Rate	
		double normalisedTurnRate = MathsHelper.Normalise(deltaRadians, 0.0, ARRIVAL_ANGLE);
		
		// Calculate Turn Direction
		if(Vector3.Determinant(m_AnchorForward,  m_SteeringVector) > 0.0)
		{
            normalisedTurnRate *= -1;
		}

		return normalisedTurnRate;
	}

    public void SetGoal(Vector3 goal)
    {
        m_Goal.SetVector(goal);
    }
}
