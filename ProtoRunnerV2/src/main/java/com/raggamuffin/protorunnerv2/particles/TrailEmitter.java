package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.RenderObjectType;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class TrailEmitter extends GameObject
{
    private GameObject m_Anchor;
    private GameLogic m_Game;
    private ParticleManager m_ParticleManager;

    private double m_Offset;

    private TrailNode m_HeadNode;

    private double m_LifeSpan;
    private double m_EmissionRate;
    private double m_EmissionCounter;

    private Colour m_HotColour;
    private Colour m_ColdColour;
    private double m_TrailFadeInLength;

    public TrailEmitter(GameLogic game, GameObject anchor, double fadeInLength)
    {
        super(game, ModelType.Nothing);

        Initialise(game, anchor, fadeInLength);
    }

    public TrailEmitter(GameLogic game, GameObject anchor)
    {
        super(game, ModelType.Nothing);

        Initialise(game, anchor, 0.4);
    }

    private void Initialise(GameLogic game, GameObject anchor, double fadeInLength)
    {
        m_Anchor = anchor;
        m_Game = game;
        m_ParticleManager = m_Game.GetParticleManager();

        m_Offset = -1.5;

        m_LifeSpan = 2.0;
        m_EmissionRate = 0.1;
        m_EmissionCounter = m_EmissionRate;

        m_HotColour = anchor.GetColour();
        m_ColdColour = anchor.GetColour();
        m_TrailFadeInLength = fadeInLength;

        m_HotColour.Alpha = 0.0;
        m_ColdColour.Alpha = 0.0;

        m_Position.SetVector(m_Anchor.GetPosition());
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Position.SetVector(m_Anchor.GetPosition());
        m_Position.I += m_Offset * m_Anchor.GetForward().I;
        m_Position.J += m_Offset * m_Anchor.GetForward().J;
        m_Position.K += m_Offset * m_Anchor.GetForward().K;

        m_EmissionCounter += deltaTime;

        if(m_EmissionCounter >= m_EmissionRate)
        {
            m_EmissionCounter = 0;
            m_HeadNode = m_ParticleManager.CreateTrailPoint(this);
        }

        m_HeadNode.SetPosition(m_Position);
    }

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid();
    }

    @Override
    public void CleanUp()
    {
        m_HeadNode = null;
    }

    public TrailNode GetHeadNode()
    {
        return m_HeadNode;
    }

    public double GetLifeSpan()
    {
        return m_LifeSpan;
    }

    public Colour GetHotColour()
    {
        return m_HotColour;
    }

    public Colour GetColdColour()
    {
        return m_ColdColour;
    }

    public double GetFadeInLength()
    {
        return m_TrailFadeInLength;
    }

    public void LowResolutionMode()
    {
        m_EmissionRate = 0.05;
        m_LifeSpan = 0.1;
    }
}
