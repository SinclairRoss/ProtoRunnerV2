package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Engine
{
    private double ENGINE_OUTPUT_EXERTION_MULTIPLIER 	= 0.0;
    private double AFTERBURNER_EXERTION_MULTIPLIER 		= 2.0;
    private double DODGE_EXERTION_MULTIPLIER	  		= 10.0;
    private double EXERTION_DECAY_MULTIPLIER 			= 0.7;

    private final double DODGE_DECAY_MULTIPLIER 		= 5.0;

    private GameObject m_Anchor;

    // Propulsion.
    private Vector3 m_Direction;		// The direction the engine is applying force.

    private double m_EngineOutput;		// The percentage of energy that is being output by the engine.
    private double m_MaxEngineOutput;	// The maximum energy output of the engines.

    private double m_AfterBurnerOutput; 	// The percentage of energy that is being output by the afterburners.
    private double m_MaxAfterBurnerOutput; 	// The maximum energy output of the afterburners.

    private double m_DodgeOutput;			// How much energy is output by dodging.
    private double m_MaxDodgeForce;
    private Vector3 m_DodgeDirection;

    // Turning.
    protected double m_TurnRate;		// How forcefully the object is turning.
    protected double m_MaxTurnRate;		// The maximum rate at which an object can turn.

    private double m_Exertion;			// How hard the engine is pushing itself.

    private EngineUseBehaviour m_UseBehaviour;

    private TrailEmitter m_TrailEmitter;

    public Engine(GameObject anchor, ParticleManager pManager, EngineUseBehaviour behaviour)
    {
        m_Anchor = anchor;
        m_UseBehaviour = behaviour;

        m_Direction = m_Anchor.GetForward();

        m_EngineOutput = 1.0;
        m_MaxEngineOutput = 700.0;

        m_AfterBurnerOutput = 0.0;
        m_MaxAfterBurnerOutput= 2000;

        m_DodgeOutput = 0.0;
        m_MaxDodgeForce = 18000.0;

        m_DodgeDirection = new Vector3();

        m_TurnRate = 0.0f;
        m_MaxTurnRate = 1.0f;

        m_Exertion = 0.0;

        m_TrailEmitter = new TrailEmitter(m_Anchor, pManager);
        m_Anchor.AddChild(m_TrailEmitter);
    }

    public void Update(double deltaTime)
    {
        UpdateOrientation(deltaTime);

        m_Anchor.UpdateVectors();
        m_Anchor.ApplyForce(m_Direction, GetEngineOutput());

        m_Anchor.ApplyForce(m_DodgeDirection, GetDodgeOutput());

        // Decay the output of the dodge overtime.
        m_DodgeOutput -= deltaTime * DODGE_DECAY_MULTIPLIER;
        m_DodgeOutput = MathsHelper.Clamp(m_DodgeOutput, 0, 1);

        UpdateExertion(deltaTime);

        m_UseBehaviour.Update(deltaTime, GetEngineOutput(), GetDodgeOutput());
    }

    public void Dodge(Vector3 Direction)
    {
        m_DodgeDirection.SetVector(Direction);
        m_DodgeOutput = 1.0;
    }

    private void UpdateOrientation(double DeltaTime)
    {
        double yaw = m_Anchor.GetYaw() + (m_TurnRate * m_MaxTurnRate * DeltaTime);
        m_Anchor.SetYaw(yaw);

        double roll = Math.toRadians(m_TurnRate * 25);
        m_Anchor.SetRoll(roll);
    }

    private void UpdateExertion(double DeltaTime)
    {
        m_Exertion += DeltaTime * ((m_EngineOutput 		* ENGINE_OUTPUT_EXERTION_MULTIPLIER)
                +  (m_AfterBurnerOutput * AFTERBURNER_EXERTION_MULTIPLIER)
                + 	m_DodgeOutput 		* DODGE_EXERTION_MULTIPLIER);


        m_Exertion -= DeltaTime * EXERTION_DECAY_MULTIPLIER;

        m_Exertion = MathsHelper.Clamp(m_Exertion, 0, 1);
    }

    public void EngageAfterBurners()
    {
        m_AfterBurnerOutput = 1.0;
    }

    public void DisengageAfterBurners()
    {
        m_AfterBurnerOutput = 0.0;
    }

    ///// Getters.
    public double GetEngineOutput()
    {
        return (m_EngineOutput * m_MaxEngineOutput) + (m_AfterBurnerOutput * m_MaxAfterBurnerOutput);
    }

    public double GetDodgeOutput()
    {
        return (m_DodgeOutput * m_MaxDodgeForce);
    }

    ///// Setters.
    public void SetMaxTurnRate(double TurnRate)
    {
        m_MaxTurnRate = TurnRate;
    }

    public void SetMaxEngineOutput(double Output)
    {
        m_MaxEngineOutput = Output;
    }

    public void SetEngineOutput(double Output)
    {
        m_EngineOutput = MathsHelper.Clamp(Output, 0.0, 1.0);
    }

    public void SetAfterBurnerOutput(double output)
    {
        m_MaxAfterBurnerOutput = output;
    }

    public void SetDirection(Vector3 Direction)
    {
        m_Direction = Direction;
    }

    public void SetTurnRate(double TurnRate)
    {
        m_TurnRate = TurnRate;
        m_TurnRate = MathsHelper.Clamp(m_TurnRate, -1.0, 1.0);
    }

    public double GetTurnRate()
    {
        return m_TurnRate;
    }

    public double GetExertion()
    {
        return m_Exertion;
    }

    public void UpdateParticleColours(Colour start, Colour end)
    {
        m_TrailEmitter.SetStartColour(start);
        m_TrailEmitter.SetFinalColour(end);
    }
}