package com.raggamuffin.protorunnerv2.weapons;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_LerpTo;
import com.raggamuffin.protorunnerv2.gameobjects.Engine;
import com.raggamuffin.protorunnerv2.gameobjects.EngineUseBehaviour_Null;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ProjectileBehaviour_Missile extends ProjectileBehaviour
{
	private enum ProjectileState 
	{
		Docked,
		Releasing,
		Arming,
		Armed
	}
	
	private final double ARRIVAL_ANGLE = Math.toRadians(3);
	private final double RE_EVALUATION_TIME = 0.3;
	private Timer ReEvaluateTargetTimer;
	private Vector3 m_ToTarget;
	private Vector3 m_ScratchVector;
	private double m_UtilityArk;
	
	private final double DELTA_RELEASE = 0.15;
	private final double ARMING_TIME = 0.5;
	
	private GameAudioManager m_Audio;
	private ArrayList<Vehicle> m_Enemies;
	
	private ProjectileState m_State;
	private Weapon m_FiringWeapon;
	private Vector3 m_DockedPosition;
	private Vector3 m_Offset;
	
	private Engine m_Engine;
	
	private Timer m_ReleaseTimer;
	private Timer m_ArmingTimer;
	
	private ColourBehaviour_LerpTo m_StressBehaviour;
	
	public ProjectileBehaviour_Missile(Projectile anchor, GameAudioManager audio, VehicleManager vManager, ParticleManager pManager, Vector3 muzzlePos, int index) 
	{
		super(anchor);

		m_State = ProjectileState.Docked;
		
		ReEvaluateTargetTimer = new Timer(RE_EVALUATION_TIME);
		
		m_Audio = audio;
		m_Enemies = vManager.GetOpposingTeam(m_Anchor.GetAffiliation());
		m_ToTarget = new Vector3();
		m_ScratchVector = new Vector3();
		m_UtilityArk = Math.toRadians(90);
		
		m_FiringWeapon = m_Anchor.GetFiringWeapon();
		m_DockedPosition = new Vector3();
		m_Offset = new Vector3(muzzlePos);
		
		m_Anchor.SetDragCoefficient(0.2);
		
		m_Anchor.SetMass(100);
		
		Colour altColour = new Colour(m_Anchor.GetFiringWeapon().GetAltColour());
		
		m_Engine = new Engine(anchor, pManager, new EngineUseBehaviour_Null());
		m_Engine.SetMaxTurnRate(2.0);
		m_Engine.SetAfterBurnerOutput(50);
		m_Engine.UpdateParticleColours(altColour, m_Anchor.GetFiringWeapon().GetColour());
		
		m_ReleaseTimer = new Timer(DELTA_RELEASE * index);
		m_ArmingTimer = new Timer(ARMING_TIME);

		m_StressBehaviour = new ColourBehaviour_LerpTo(m_Anchor, ColourBehaviour.ActivationMode.Continuous);
		m_StressBehaviour.SetAltColour(altColour);
		m_Anchor.AddColourBehaviour(m_StressBehaviour);
		
		m_Anchor.AddChild(new FloorGrid(m_Anchor.GetColour()));
	}

	@Override
	public void Update(double deltaTime) 
	{
		switch(m_State)
		{
			case Docked:
			{
				LockProjectile();
				
				if(!m_FiringWeapon.GetAnchor().IsValid())
					m_State = ProjectileState.Releasing;
					
				if(!m_FiringWeapon.IsTriggerPulled())
					m_State = ProjectileState.Releasing;

				break;
			}
			case Releasing:
			{
				LockProjectile();
				
				m_ReleaseTimer.Update(deltaTime);
				
				if(m_ReleaseTimer.TimedOut())
				{
					m_State = ProjectileState.Arming;
					
					m_Anchor.SetForward(m_FiringWeapon.GetForward());
					m_Anchor.SetVelocity(m_FiringWeapon.GetVelocity());
					m_Anchor.GetVelocity().Scale(0.4);
				}
		
				break;
			}
			case Arming:	
			{
				m_ArmingTimer.Update(deltaTime);
				
				if(m_ArmingTimer.TimedOut())
				{
					m_State = ProjectileState.Armed;
					m_Engine.EngageAfterBurners();
					
					m_Audio.PlaySound(m_Anchor.GetPosition(), AudioClips.MissileEngaged);
					m_ToTarget.SetVector(m_FiringWeapon.GetForward());
				}
				
				break;	
			}	
			case Armed:
			{	
				m_Engine.Update(deltaTime);
				
				ReEvaluateTargetTimer.Update(deltaTime);
				
				if(ReEvaluateTargetTimer.TimedOut())
				{
					ReEvaluateTargetTimer.ResetTimer();
					CalculateToTarget();
				}
				
				m_Engine.SetTurnRate(CalculateTurnRate());

				break;
			}
		}
		
		m_StressBehaviour.SetIntensity(m_Engine.GetExertion());
	}
	
	private void CalculateToTarget()
	{
		Vehicle target = null;
		double highestUtility = Double.MIN_VALUE;
		
		for(Vehicle enemy : m_Enemies)
		{
			double utility = CalculateVehicleUtility(enemy.GetPosition());
			
			if(utility > highestUtility)
			{
				target = enemy;
				highestUtility = utility;
			}
		}
		
		if(target != null)
			m_ToTarget.SetVectorDifference(m_Anchor.GetPosition(), target.GetPosition());
		else
			m_ToTarget = m_Anchor.GetForward();
	}
	
	private double CalculateVehicleUtility(Vector3 pos)
	{
		m_ScratchVector.SetVectorDifference(m_Anchor.GetPosition(), pos);
		
		// Phase 1: Calculate utility based on distance from target.
		double distanceSqr = m_ScratchVector.GetLengthSqr();
		double utility = MathsHelper.Normalise(distanceSqr, 0.0, 100.0);
		
		// Phase 2: Calculate utility base on direction to target
		double deltaHeading = Vector3.RadiansBetween(m_Anchor.GetForward(), m_ScratchVector);
		utility += (1.0 - MathsHelper.Normalise(deltaHeading, 0.0, m_UtilityArk));
		
		return utility;
	}
	
	private double CalculateTurnRate()
	{
		// Calculate the difference in bearing to point towards the target.
		double deltaRadians = Vector3.RadiansBetween(m_Anchor.GetForward(), m_ToTarget);

		// Calculate Turn Rate	
		double normalisedTurnRate = MathsHelper.Normalise(deltaRadians, 0.0, ARRIVAL_ANGLE);

		// Calculate Turn Direction
		if(Vector3.Determinant(m_Anchor.GetForward(),  m_ToTarget) > 0.0)
			normalisedTurnRate *= -1;

		return normalisedTurnRate;
	}
	
	private void LockProjectile()
	{
		m_DockedPosition.SetVector(m_Offset);
		m_DockedPosition.RotateY(m_FiringWeapon.GetOrientation());
		m_DockedPosition.Add(m_FiringWeapon.GetPosition());
		
		m_Anchor.SetYaw(m_FiringWeapon.GetOrientation());
		m_Anchor.SetPosition(m_DockedPosition);
		
		m_Anchor.ResetLifeSpan();
	}

	@Override
	public void CleanUp() 
	{
		m_Anchor.RemoveColourBehaviour(m_StressBehaviour);
		m_Anchor.SetDragCoefficient(0.0);
		m_Anchor.ResetMass();
	}

    @Override
    public boolean UseSimpleCollisionDetection()
    {
        return false;
    }
}
