// Author:	Sinclair Ross.
// Notes:	This class controls the movement of an AI controlled ship.
//			The responsibilites of this class are moving to the target position dictated by the navigation state.

package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;
import java.util.Vector;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

public class NavigationControl 
{
	public enum NavigationStates
	{
		Intercept,
		Flee,
		Follow,
		Strafe
	}
	
	private final double ARRIVAL_ANGLE = MathsHelper.DegToRad(5.0);
	private final double EVASION_CHANCE = 0.6;
	private final double DELTA_DODGE_TIME = 2.0;

	private Vehicle m_Anchor;
	private AIController m_Controller;
	private SituationalAwareness m_SituationalAwareness;
	
	private Vector3 m_AnchorPosition;
	private Vector3 m_AnchorForward;
	
	private Timer m_DodgeTimer;
	
	///// Steering Attributes.
	private Vector3 m_SteeringVector;
	private Vector3 m_ToGoal;
	private Vector3 m_Seperation;
	private Vector3 m_Alignment;
	private Vector3 m_Cohesion;
	private Vector3 m_CenterOfMass;
	private double m_GoalWeight;
	private double m_SeperationWeight;
	private double m_AlignmentWeight;
	private double m_CohesionWeight;
	
	///// Navigation States.
	private ArrayList<NavigationState> m_NavigationStates;
	private NavigationState m_NavigationState;
	private Intercept m_Intercept;
	private Flee m_Flee;
	private Follow m_Follow;
	private Strafe m_Strafe;
	
	public NavigationControl(AIController Controller)
	{
		m_Controller =			 Controller;
		m_Anchor 				= m_Controller.GetAnchor();
		m_SituationalAwareness 	= m_Controller.GetSituationalAwareness();
		
		m_AnchorPosition 		= m_Anchor.GetPosition();
		m_AnchorForward 		= m_Anchor.GetForward();
		
		m_DodgeTimer = new Timer(DELTA_DODGE_TIME);
		
		m_SteeringVector = new Vector3();
		m_ToGoal 		 = new Vector3();
		m_Seperation 	 = new Vector3();
		m_Alignment 	 = new Vector3();
		m_Cohesion 		 = new Vector3();
		m_CenterOfMass 	 = new Vector3();
		m_GoalWeight		= 0.4;
		m_SeperationWeight	= 1.0;
		m_AlignmentWeight	= 0.7;
		m_CohesionWeight	= 0.6;
		
		m_Intercept = new Intercept(m_Controller);
		m_Flee		= new Flee(m_Controller);
		m_Follow	= new Follow(m_Controller);
		m_Strafe	= new Strafe(m_Controller);
		
		m_NavigationStates = new ArrayList<NavigationState>();
		
		m_NavigationStates.add(m_Intercept);
		m_NavigationStates.add(m_Flee);
		m_NavigationStates.add(m_Follow);
		m_NavigationStates.add(m_Strafe);
		
		m_NavigationState = m_Intercept;
		SetNavigationState(NavigationStates.Intercept);
	}
	
	public void Update(double deltaTime)
	{
		CalculateToGoal();
		CalculateSeparation();
		CalculateAlignment();
		CalculateCohesion(); 
		CalculateSteeringVector();
	
		m_DodgeTimer.Update(deltaTime);
		HandleEvasion();
		
		m_Anchor.SetEngineOutput(CalculateEngineOutput());					 // Set engine output.
		m_Anchor.SetTurnRate(CalculateTurnRate());							 // Set vehicle turn rate.
	}
	
	private void HandleEvasion()
	{
		if(!m_DodgeTimer.TimedOut())
			return;
		
		Vector<Projectile> projectiles = m_SituationalAwareness.GetIncomingProjectiles();

		if(projectiles.size() == 0)
			return;

		m_DodgeTimer.ResetTimer();
		
		double outcome = MathsHelper.RandomDouble(0, 1);
		
		if(outcome > EVASION_CHANCE)
			return;
		
		outcome = MathsHelper.RandomDouble(0, 1);
		
		if(outcome >= 0.5)
			m_Anchor.DodgeLeft();
		else
			m_Anchor.DodgeRight();
	}
	
	private void CalculateToGoal()
	{
		Vector3 Destination = m_NavigationState.CalculateDestination();	// Calculate the desination of the agent.
		m_ToGoal.SetVectorDifference(m_AnchorPosition, Destination); 	// Calculate vector pointing to destination.
		m_ToGoal.Normalise();
		m_ToGoal.Scale(m_GoalWeight);
	}
	
	private void CalculateSeparation()
	{
		m_Seperation.SetVector(0.0);

		// Get surrounding vehicles.
		Vector<Vehicle> SurroundingVehicles = m_SituationalAwareness.GetSurroundingVehicles();
		
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
		Vector<Vehicle> SurroundingVehicles = m_SituationalAwareness.GetSurroundingVehicles();
				
		// Loop through each surrounding vehicle and create seperation vector.
		for(Vehicle Squaddie : SurroundingVehicles)
		{
			if(Squaddie.GetAffiliation() == m_Anchor.GetAffiliation())
				m_Alignment.Add(Squaddie.GetForward());
		}
		
		m_Alignment.Normalise();
		m_Alignment.Scale(m_AlignmentWeight);
	}
	
	private void CalculateCohesion()
	{
		m_Cohesion.SetVector(0.0);
		m_CenterOfMass.SetVector(0.0);

		// Get surrounding vehicles.
		Vector<Vehicle> SurroundingVehicles = m_SituationalAwareness.GetSurroundingVehicles();
		
		// If there is no surrounding vehicles break from function.
		// Not doing this could result in cohesion vector pointing towards origin instead of being 0.
		if(SurroundingVehicles.size() == 0)
			return;
				
		// Loop through each surrounding vehicle and create seperation vector.
		for(Vehicle Squaddie : SurroundingVehicles)
		{
			if(Squaddie.GetAffiliation() == m_Anchor.GetAffiliation())
			{
				m_CenterOfMass.Add(Squaddie.GetPosition());
			}
		}
		
		// Get average position of squaddies as center of mass.
		double Scale = 1.0 / SurroundingVehicles.size();
		m_CenterOfMass.Scale(Scale);
		
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
	
	public void SetNavigationState(NavigationStates state)
	{
		m_NavigationState.EndBehaviour();
		
		switch(state)
		{
			case Flee:
				m_NavigationState = m_Flee;
				break;
			case Follow:
				m_NavigationState = m_Follow;
				break;
			case Intercept:
				m_NavigationState = m_Intercept;
				break;
			case Strafe:
				m_NavigationState = m_Strafe;
				break;		
		}
		
		m_NavigationState.StartBehaviour();
	}
	
	public void SetHighestUtilityState() 
	{
		double maxUtility = 0.0f;
		
		for(NavigationState state : m_NavigationStates)
		{
			double currentUtility = state.CalculateUtility();
			
			if(currentUtility > maxUtility)
			{
				m_NavigationState 	= state;
				maxUtility 			= currentUtility;
			}
		}
	}
}
