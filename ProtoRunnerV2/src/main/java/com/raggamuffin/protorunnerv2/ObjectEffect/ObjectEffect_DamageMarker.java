package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   22/05/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Quaternion;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ObjectEffect_DamageMarker extends ObjectEffect
{
    private final double MAX_ALPHA = 0.6;

    private final GameLogic m_GameLogic;
    private Vector3 m_LookPosition;

    private Timer m_LifeTimer;

    // Roll
    private Quaternion m_RollQuaternion;
    private final double ROLL_RATE = Math.toRadians(180);
    private double m_RollAmount;

    // Scale
    private final double MAX_SCALE = 10.0;

    public ObjectEffect_DamageMarker(GameLogic game)
    {
        super(ModelType.DamageTri);

        SetScale(5);

        m_LifeTimer = new Timer(0.5);

        m_GameLogic = game;
        m_LookPosition = new Vector3();
        m_RollQuaternion = new Quaternion();
    }

    @Override
    public void Initialise(Vehicle anchor)
    {
        super.Initialise(anchor);

        SetColour(GetAnchor().GetColour());
        GetColour().Brighten(0.4f);
        SetAlpha(0);

        SetPosition(GetAnchor().GetPosition());

        m_LookPosition.SetVector(m_GameLogic.GetCamera().GetPosition());
        LookAt(m_LookPosition);

        m_RollAmount = 0;
        SetScale(0);

        m_LifeTimer.Start();
    }

    @Override
    public void Update(double deltaTime)
    {
        SetScale(MAX_SCALE * m_LifeTimer.GetProgress());

        double alpha = m_LifeTimer.GetInverseProgress() * MAX_ALPHA;
        SetAlpha(alpha);

        SetPosition(GetAnchor().GetPosition());

        m_LookPosition.SetVector(m_GameLogic.GetCamera().GetPosition());
        LookAt(m_LookPosition);

        m_RollAmount += ROLL_RATE * deltaTime;

        m_RollQuaternion.SetQuaternion(GetForward(), m_RollAmount);
        Rotate(m_RollQuaternion);
    }

    @Override
    public boolean IsValid()
    {
        return !m_LifeTimer.HasElapsed();
    }
}