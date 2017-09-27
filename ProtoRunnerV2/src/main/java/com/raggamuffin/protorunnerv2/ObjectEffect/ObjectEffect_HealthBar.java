package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   20/07/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ObjectEffect_HealthBar extends ObjectEffect
{
    private static final double FADEIN_DURATION = 0.2;
    private static final double FADEOUT_DURATION = 1.0;

    private static final double HEIGHT_OFFSET = 10.0;
    private static final double BASE_SCALE = 2.0;

    private static final double ARRIVAL_DISTANCE = 0.1;
    private static final double FILL_RATE = 2.0;

    private ChaseCamera m_Camera;
    private Vector3 m_ToCamera;

    private Timer m_Timer_Grow;
    private boolean m_FadingOff;

    private double m_Progress;

    public ObjectEffect_HealthBar(GameLogic gameLogic)
    {
        super(ModelType.Radial);

        m_Camera = gameLogic.GetCamera();
        m_ToCamera = new Vector3();

        m_Timer_Grow = new Timer(1);
    }

    @Override
    public void Initialise(Vehicle anchor)
    {
        super.Initialise(anchor);

        SetPosition(GetAnchor().GetPosition());
        GetPosition().Y += HEIGHT_OFFSET;

        SetColour(anchor.GetColour());
        SetScale(0);
        SetAlpha(0);

        m_Timer_Grow.Start(FADEIN_DURATION);
        m_FadingOff = false;

        m_Progress = 1.0;
    }

    @Override
    public void Update(double deltaTime)
    {
        SetPosition(GetAnchor().GetPosition());
        GetPosition().Y += HEIGHT_OFFSET;

        m_ToCamera.SetVectorAsDifference(GetPosition(), m_Camera.GetPosition());
        m_ToCamera.Normalise();
        SetForward(m_ToCamera);

        double timerProgress;

        if(!GetAnchor().IsValid())
        {
            if(!m_FadingOff)
            {
                m_Timer_Grow.Start(FADEOUT_DURATION);
                m_FadingOff = true;
            }

            timerProgress =  m_Timer_Grow.GetInverseProgress();
        }
        else
        {
            timerProgress = m_Timer_Grow.GetProgress();
        }

        SetScale(BASE_SCALE * timerProgress);
        SetAlpha(timerProgress);

        UpdateProgress(deltaTime);
    }

    private void UpdateProgress(double deltaTime)
    {
        double deltaProgress = GetAnchor().GetHullPointsAsPercentage() - m_Progress;
        deltaProgress = MathsHelper.SignedNormalise(deltaProgress, -ARRIVAL_DISTANCE, ARRIVAL_DISTANCE);

        m_Progress += (deltaProgress * FILL_RATE * deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        return !(m_FadingOff && m_Timer_Grow.HasElapsed());
    }

    public double Progress() { return m_Progress; } //GetAnchor().GetHullPointsAsPercentage(); }
    public double GetLineWidth() { return 25; }
}
