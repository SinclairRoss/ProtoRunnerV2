package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Spring3;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RadarFragment extends GameObject
{
    private final double MAX_IMPULSE_PERIOD = 2.0;
    private final double MAX_IMPULSE_FORCE = 0.5;
    private final double MAX_ALPHA = 0.4;

    private final double MIN_DEPTH;
    private final double MAX_DEPTH;

    private Vector3 m_RestedPosition;
    private Vector3 m_Offset;
    private double m_RestingHeight;

    private Vector3 m_NormalisedRadarPos;
    private Spring3 m_Spring;

    private final Colour m_FriendlyColour;
    private final Colour m_EnemyColour;
    private final Colour m_NeutralColour;

    private boolean m_ForciblyInvalidated;

    private RadarSignatureType m_SignatureType;
    private Timer m_RandomImpulseTimer;

    public RadarFragment(GameLogic game, double min, double max, double x, double y, double radarRadius)
    {
        super(ModelType.RadarFragment, 0);

        MIN_DEPTH = min;
        MAX_DEPTH = max;

        m_ForciblyInvalidated = false;

        m_Offset = new Vector3(x, 0, y);
        m_RestedPosition = new Vector3();
        m_Spring = new Spring3(5, 0);

        m_NormalisedRadarPos = new Vector3(m_Offset);
        m_NormalisedRadarPos.Scale(1 / radarRadius);

        m_RestingHeight = 0.25;
        m_SignatureType = RadarSignatureType.None;

        m_FriendlyColour = game.GetColourManager().GetSafeColour();
        m_EnemyColour = game.GetColourManager().GetDangerColour();

        m_NeutralColour = new Colour(Colours.PastelGrey);
        SetColour(m_NeutralColour);

        m_RandomImpulseTimer = new Timer(1.0);
        m_RandomImpulseTimer.Start();
    }

    @Override
    public void Update(double deltaTime)
    {
        if(m_RandomImpulseTimer.HasElapsed())
        {
            ApplyForce(Vector3.UP, MathsHelper.RandomDouble(-MAX_IMPULSE_FORCE, MAX_IMPULSE_FORCE));

            m_RandomImpulseTimer.SetDuration(MathsHelper.RandomDouble(0, MAX_IMPULSE_PERIOD));
            m_RandomImpulseTimer.Start();
        }

        double targetDepth = MathsHelper.Lerp(m_RestingHeight, MIN_DEPTH, MAX_DEPTH);

        Translate(m_Offset);
        m_RestedPosition.SetVector(GetPosition());
        m_RestedPosition.Y = targetDepth;
        ApplyForce(m_Spring.CalculateSpringForce(GetPosition(), m_RestedPosition), deltaTime);

        UpdateColour();

        super.Update(deltaTime);
    }

    private void UpdateColour()
    {
        double alpha = MathsHelper.Normalise(GetPosition().Y, MIN_DEPTH, MAX_DEPTH) * MAX_ALPHA;

        SetAlpha(alpha);
    }

    public void SetSignatureType(RadarSignatureType type)
    {
        m_SignatureType = type;

        switch (m_SignatureType)
        {
            case Friendly:
            {
                SetColour(m_FriendlyColour);
                m_RestingHeight = 1.0;
                break;
            }
            case Foe:
            {
                SetColour(m_EnemyColour);
                m_RestingHeight = 1.0;
                break;
            }
            case None:
            {
                SetColour(m_NeutralColour);
                m_RestingHeight = 0.1;
                break;
            }
        }
    }

    public void ForceInvalidation()
    {
        m_ForciblyInvalidated = true;
    }

    @Override
    public boolean IsValid()
    {
        return !m_ForciblyInvalidated;
    }

    @Override
    public void CleanUp()
    {}

    public void Reset()
    {
        SetSignatureType(RadarSignatureType.None);
    }

    public Vector3 GetNormalisedRadarPosition()
	{
		return m_NormalisedRadarPos;
	}

    public RadarSignatureType GetRadarSignature()
    {
        return m_SignatureType;
    }
}