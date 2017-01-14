package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_HyperLight;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Engine
{
    private final double ENGINE_OUTPUT_EXERTION_MULTIPLIER 	= 0.0;
    private final double AFTERBURNER_EXERTION_MULTIPLIER 		= 2.0;
    private final double DODGE_EXERTION_MULTIPLIER	  		= 10.0;
    private final double EXERTION_DECAY_MULTIPLIER 			= 0.7;

    private final double DODGE_DECAY_MULTIPLIER 		= 5.0;

    protected GameObject m_Anchor;

    // Propulsion.
    private Vector3 m_Direction;		// The direction the engine is applying force.

    private double m_EngineOutput;		// The percentage of energy that is being output by the engine.
    private double m_MaxEngineOutput;	// The maximum energy output of the engines.

    private double m_AfterBurnerOutput; 	// The percentage of energy that is being output by the afterburners.
    private double m_MaxAfterBurnerOutput; 	// The maximum energy output of the afterburners.

    private double m_DodgeOutput;			// How much energy is output when dodging.
    private double m_MaxDodgeForce;
    private Vector3 m_DodgeDirection;

    // Turning.
    protected double m_TargetTurnRate;
    protected double m_TurnSpeed;
    protected double m_TurnRate;		// How forcefully the object is turning.
    protected double m_MaxTurnRate;		// The maximum rate at which an object can turn.
    protected double m_MaxRoll;

    private double m_Exertion;			// How hard the engine is pushing itself.

    private boolean m_RollEnabled;

    protected ParticleEmitter_HyperLight m_HyperLight;

    public Engine(GameLogic game, GameObject anchor)
    {
        m_Anchor = anchor;

        m_Direction = m_Anchor.GetForward();

        m_EngineOutput = 1.0;
        m_MaxEngineOutput = 700.0;

        m_AfterBurnerOutput = 0.0;
        m_MaxAfterBurnerOutput = 2000;

        m_DodgeOutput = 0.0;
        m_MaxDodgeForce = 120000.0;

        m_DodgeDirection = new Vector3();

        m_TargetTurnRate = 0.0;
        m_TurnSpeed = 6.0;
        m_TurnRate = 0.0f;
        m_MaxTurnRate = 1.0f;
        m_MaxRoll = Math.toRadians(25);

        m_Exertion = 0.0;

        m_HyperLight = new ParticleEmitter_HyperLight(game, m_Anchor.GetBaseColour(), m_Anchor.GetAltColour(), 30, 1);

        m_RollEnabled = true;
    }

    public void Update(double deltaTime)
    {
        UpdateTurnRate(deltaTime);
        UpdateOrientation(deltaTime);

        m_Anchor.ApplyForce(m_Direction, GetEngineOutput());
        m_Anchor.ApplyForce(m_DodgeDirection, GetDodgeOutput());

        // Decay the output of the dodge overtime.
        m_DodgeOutput -= deltaTime * DODGE_DECAY_MULTIPLIER;
        m_DodgeOutput = MathsHelper.Clamp(m_DodgeOutput, 0, 1);

        UpdateExertion(deltaTime);

        UpdateHyperlightEmitter(deltaTime);
    }

    private void UpdateHyperlightEmitter(double deltaTime)
    {
        Vector3 velocity = m_Anchor.GetVelocity();

        Vector3 emitterForward = new Vector3(velocity);
        emitterForward.Normalise();
        m_HyperLight.SetForward(emitterForward);

        emitterForward.Scale(3.0);
        emitterForward.Add(m_Anchor.GetPosition());
        m_HyperLight.SetPosition(emitterForward);
        m_HyperLight.SetVelocity(velocity);

        m_HyperLight.Update(deltaTime);
    }

    public void Dodge(Vector3 direction)
    {
        m_DodgeDirection.SetVector(direction);
        m_DodgeOutput = 1.0;
    }

    private void UpdateTurnRate(double deltaTime)
    {
        double deltaTurn = m_TargetTurnRate - m_TurnRate;
        double turnMultiplier = MathsHelper.SignedNormalise(deltaTurn, -0.1, 0.1);
        m_TurnRate += m_TurnSpeed * turnMultiplier * deltaTime;
    }

    private void UpdateOrientation(double deltaTime)
    {
        double yaw = m_Anchor.GetYaw() + (m_TurnRate * m_MaxTurnRate * deltaTime);
        m_Anchor.SetYaw(yaw);

        double deltaRoll;

        if(m_RollEnabled)
        {
            deltaRoll =  -(m_TargetTurnRate * m_MaxRoll) - m_Anchor.GetRoll();
        }
        else
        {
            deltaRoll = -m_Anchor.GetRoll();
        }

        double rollMultiplier = MathsHelper.SignedNormalise(deltaRoll, -0.1, 0.1);
        m_Anchor.SetRoll(MathsHelper.Clamp(m_Anchor.GetRoll() + (rollMultiplier * deltaTime), -m_MaxRoll, m_MaxRoll));
    }

    private void UpdateExertion(double deltaTime)
    {
        m_Exertion += deltaTime * ((m_EngineOutput * ENGINE_OUTPUT_EXERTION_MULTIPLIER)
                +  (m_AfterBurnerOutput * AFTERBURNER_EXERTION_MULTIPLIER)
                + 	m_DodgeOutput 		* DODGE_EXERTION_MULTIPLIER);


        m_Exertion -= deltaTime * EXERTION_DECAY_MULTIPLIER;

        m_Exertion = MathsHelper.Clamp(m_Exertion, 0, 1);
    }

    public void EngageAfterBurners()
    {
        m_HyperLight.On();
        m_AfterBurnerOutput = 1.0;
    }

    public void DisengageAfterBurners()
    {
        m_HyperLight.Off();
        m_AfterBurnerOutput = 0.0;
    }

    ///// Getters/Setters
    public double GetEngineOutput()
    {
        return (m_EngineOutput * m_MaxEngineOutput) + (m_AfterBurnerOutput * m_MaxAfterBurnerOutput);
    }

    public double GetDodgeOutput()
    {
        return (m_DodgeOutput * m_MaxDodgeForce);
    }

    public void SetDodgeOutput(double max)
    {
        m_MaxDodgeForce = max;
    }

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
        m_EngineOutput = MathsHelper.Clamp(Output, -1.0, 1.0);
    }

    public void SetAfterBurnerOutput(double output)
    {
        m_MaxAfterBurnerOutput = output;
    }

    public void SetDirection(Vector3 Direction)
    {
        m_Direction = Direction;
    }

    public void SetTurnRate(double turnRate)
    {
        m_TargetTurnRate = MathsHelper.Clamp(turnRate, -1.0, 1.0);
    }

    public double GetTurnRate()
    {
        return m_TurnRate;
    }

    public double GetExertion()
    {
        return m_Exertion;
    }

    public Vector3 GetPosition()
    {
        return m_Anchor.GetPosition();
    }

    public void EnableRoll()
    {
        m_RollEnabled = true;
    }

    public void DisableRoll()
    {
        m_RollEnabled = false;
    }
}