package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Quaternion;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Engine
{
    private final double ENGINE_OUTPUT_EXERTION_MULTIPLIER = 0.0;
    private final double AFTERBURNER_EXERTION_MULTIPLIER = 2.0;
    private final double DODGE_EXERTION_MULTIPLIER = 10.0;
    private final double EXERTION_DECAY_MULTIPLIER = 0.7;
    private final double ARRIVAL_ANGLE = Math.toRadians(5);

    private final double DODGE_DECAY_MULTIPLIER = 5.0;

    protected Vehicle m_Anchor;

    // Propulsion.
    private Quaternion m_Yaw;
    private Quaternion m_Roll;
    private double m_RollAmount;

    private double m_EngineOutput;		// The percentage of energy that is being output by the engine.
    private double m_MaxEngineOutput;	// The maximum energy output of the engines.

    private double m_AfterBurnerOutput; 	// The percentage of energy that is being output by the afterburners.
    private double m_MaxAfterBurnerOutput; 	// The maximum energy output of the afterburners.

    private double m_DodgeOutput;			// How much energy is output when dodging.
    private double m_MaxDodgeForce;
    private Vector3 m_DodgeDirection;

    // Turning.
    private double m_TargetTurnRate;
    private double m_TurnSpeed;
    private double m_RollSpeed;
    private double m_TurnRate;		// How forcefully the object is turning.
    private double m_MaxTurnRate;		// The maximum rate at which an object can turn.
    private double m_MaxRoll;
    private double m_Exertion;			// How hard the engine is pushing itself.

    private boolean m_Active;

    public Engine(GameLogic game, Vehicle anchor)
    {
        m_Anchor = anchor;

        m_Yaw = new Quaternion();
        m_Roll = new Quaternion();
        m_RollAmount = 0;

        m_EngineOutput = 1.0;
        m_MaxEngineOutput = 700.0;

        m_AfterBurnerOutput = 0.0;
        m_MaxAfterBurnerOutput = 2000;

        m_DodgeOutput = 0.0;
        m_MaxDodgeForce = 1200.0;

        m_DodgeDirection = new Vector3();

        m_TargetTurnRate = 0.0;
        m_TurnSpeed = 6.0;
        m_RollSpeed = 1.0;
        m_TurnRate = 0.0f;
        m_MaxTurnRate = 1.0f;
        m_MaxRoll = Math.toRadians(25);

        m_Exertion = 0.0;

        m_Active = true;
    }

    public void Update(double deltaTime)
    {
        if(m_Active)
        {
            if(m_Anchor.GetSteeringState() != SteeringState.Locked)
            {
                UpdateTurnRate(deltaTime);
                UpdateOrientation(deltaTime);

                m_Anchor.ApplyForce(m_Anchor.GetForward(), GetEngineOutput(), deltaTime);
                m_Anchor.ApplyForce(m_DodgeDirection, GetDodgeOutput(), deltaTime);

                // Decay the output of the dodge overtime.
                m_DodgeOutput -= deltaTime * DODGE_DECAY_MULTIPLIER;
                m_DodgeOutput = MathsHelper.Clamp(m_DodgeOutput, 0, 1);
            }

            UpdateExertion(deltaTime);
        }
    }

    public void DodgeLeft()
    {
        m_DodgeDirection.SetVectorAsCrossProduct(Vector3.UP, m_Anchor.GetForward());
        m_DodgeOutput = 1.0;
    }

    public void DodgeRight()
    {
        m_DodgeDirection.SetVectorAsCrossProduct(m_Anchor.GetForward(), Vector3.UP);
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
        double deltaYaw = -m_TurnRate * m_MaxTurnRate * deltaTime;
        m_Yaw.SetQuaternion(Vector3.UP, deltaYaw);

        double rollTarget = m_TurnRate * m_MaxRoll;
        double rollDifference = rollTarget - m_RollAmount;

        double normalisedTurnRate = MathsHelper.SignedNormalise(rollDifference, -ARRIVAL_ANGLE, ARRIVAL_ANGLE);
        double deltaRoll = m_RollSpeed * normalisedTurnRate * deltaTime;
        m_RollAmount += deltaRoll;

        m_Roll.SetQuaternion(m_Anchor.GetForward(), deltaRoll);
        m_Yaw.Multiply(m_Roll);

        m_Anchor.Rotate(m_Yaw);
    }

    private void UpdateExertion(double deltaTime)
    {
        m_Exertion += deltaTime * ((m_EngineOutput * ENGINE_OUTPUT_EXERTION_MULTIPLIER)
                +  (m_AfterBurnerOutput * AFTERBURNER_EXERTION_MULTIPLIER)
                + 	m_DodgeOutput * DODGE_EXERTION_MULTIPLIER);

        m_Exertion -= deltaTime * EXERTION_DECAY_MULTIPLIER;
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

    public boolean AfterburnersEngaged() { return m_AfterBurnerOutput > 0.9; }

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

    public void SetTurnRate(double turnRate)
    {
        m_TargetTurnRate = MathsHelper.Clamp(turnRate, -1.0, 1.0);
    }

    public double GetExertion()
    {
        return m_Exertion;
    }

    public Vector3 GetPosition()
    {
        return m_Anchor.GetPosition();
    }

    public void CleanUp() {}

    public void TurnOff() { m_Active = false; }

    public void TurnOn() { m_Active = true; }
}