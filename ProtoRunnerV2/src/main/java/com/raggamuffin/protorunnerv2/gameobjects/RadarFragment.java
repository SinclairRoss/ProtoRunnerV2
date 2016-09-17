package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.colours.ColourBehaviour;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_AlphaController;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_LerpTo;
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
    private final double LIT_ALPHA = 0.4;
    private final double IDLE_ALPHA = 0.1;
    private final double MIN_DEPTH;
    private final double MAX_DEPTH;

    private Vector3 m_RestedPosition;
    private Vector3 m_Offset;
    private double m_Heat;

    private Vector3 m_NormalisedRadarPos;
    private Spring3 m_Spring;

    private final ColourBehaviour_LerpTo m_ColourBehaviour;
    private final ColourBehaviour_AlphaController m_AlphaController;

    private final Colour m_FriendlyColour;
    private final Colour m_EnemyColour;
    private final Colour m_NeutralColour;

    private RadarSignatureType m_SignatureType;

    public RadarFragment(GameLogic game, double min, double max, double x, double y, double radarRadius)
    {
        super(game, ModelType.RadarFragment);

        MIN_DEPTH = min;
        MAX_DEPTH = max;

        m_Mass = 1.0;
        m_DragCoefficient = 0.95;

        m_Offset = new Vector3(x, 0, y);
        m_RestedPosition = new Vector3();
        m_Spring = new Spring3(5, 0);

        m_NormalisedRadarPos = new Vector3(m_Offset);
        m_NormalisedRadarPos.Scale(1 / radarRadius);

        m_Heat = 0.0;
        m_SignatureType = RadarSignatureType.None;

        m_FriendlyColour = game.GetColourManager().GetPrimaryColour();
        m_EnemyColour = game.GetColourManager().GetSecondaryColour();
        m_NeutralColour = new Colour(Colours.PastelGrey);
        m_BaseColour = m_NeutralColour;
        m_AltColour = m_NeutralColour;

        m_ColourBehaviour = new ColourBehaviour_LerpTo(this, ColourBehaviour.ActivationMode.Continuous);
        AddColourBehaviour(m_ColourBehaviour);

        m_AlphaController = new ColourBehaviour_AlphaController(this, ColourBehaviour.ActivationMode.Continuous);
        AddColourBehaviour(m_AlphaController);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Heat += MathsHelper.RandomDouble(0, 0.75);
        double targetDepth = MathsHelper.Lerp(m_Heat, MIN_DEPTH, MAX_DEPTH);

        m_Position.Add(m_Offset);
        m_RestedPosition.SetVector(m_Position);
        m_RestedPosition.J = targetDepth;
        ApplyForce(m_Spring.CalculateSpringForce(m_Position, m_RestedPosition));

        UpdateColour();

        super.Update(deltaTime);
    }

    private void UpdateColour()
    {
        double normalisedDepth = MathsHelper.Normalise(m_Position.J, MIN_DEPTH, MAX_DEPTH);
        m_ColourBehaviour.SetIntensity(normalisedDepth);

        double maxAlpha = 0;

        switch(m_SignatureType)
        {
            case Friendly:
            case Foe:
            {
                maxAlpha = LIT_ALPHA;
                break;
            }
            case None:
            {
                maxAlpha = IDLE_ALPHA;
                break;
            }
        }

        m_AlphaController.SetAlpha(normalisedDepth * maxAlpha);
    }

    public void SetSignatureType(RadarSignatureType type)
    {
        m_SignatureType = type;

        switch (m_SignatureType)
        {
            case Friendly:
            {
                m_AltColour = m_FriendlyColour;
                break;
            }
            case Foe:
            {
                m_AltColour = m_EnemyColour;
                break;
            }
            case None:
            {
                m_AltColour = m_NeutralColour;
                break;
            }
        }
    }

    public void HeatUp()
    {
        m_Heat = 1.0;
    }

    @Override
    public boolean IsValid()
    {
        return true;
    }

    public void Reset()
    {
        SetSignatureType(RadarSignatureType.None);
        m_Heat = 0.0;
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