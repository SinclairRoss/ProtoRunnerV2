package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TrailEmitter
{
    private Vector3 m_Position;

    private GameObject m_Anchor;
    private GameLogic m_Game;
    private ParticleManager m_ParticleManager;

    private double m_Offset;
    private double m_LifeSpan;
    private Timer m_EmissionTimer;

    private Colour m_HotColour;
    private Colour m_ColdColour;
    private double m_TrailFadeInLength;

    private Trail m_Trail;

    public TrailEmitter(GameLogic game, GameObject anchor)
    {
        m_Anchor = anchor;
        m_Game = game;
        m_ParticleManager = m_Game.GetParticleManager();

        m_Offset = -1.5;

        m_LifeSpan = 2.0;
        m_EmissionTimer = new Timer(0.1);
        m_EmissionTimer.Start();

        m_HotColour = anchor.GetColour();
        m_ColdColour = anchor.GetColour();
        m_TrailFadeInLength = 0.4;

        m_Position = new Vector3(m_Anchor.GetPosition());

        m_Trail = new Trail(this);
        m_Trail.AddNode(m_ParticleManager.CreateTrailPoint(this));

        m_Game.GetTrailManager().AddObject(m_Trail);
    }

    public void Update()
    {
        UpdatePosition();

        if(m_EmissionTimer.HasElapsed())
        {
            m_Trail.AddNode(m_ParticleManager.CreateTrailPoint(this));
            m_EmissionTimer.Start();
        }
    }

    private void UpdatePosition()
    {
        Vector3 anchorPos = m_Anchor.GetPosition();
        Vector3 anchorForward = m_Anchor.GetForward();

        m_Position.SetVector(anchorPos.X + (m_Offset * anchorForward.X),
                             anchorPos.Y + (m_Offset * anchorForward.Y),
                             anchorPos.Z + (m_Offset * anchorForward.Z));
    }

    public Vector3 GetPosition() { return m_Position; }
    public Trail GetTrail() { return m_Trail; }
    public double GetLifeSpan() { return m_LifeSpan; }
    public Colour GetHotColour() { return m_HotColour; }
    public Colour GetColdColour() { return m_ColdColour; }
    public double GetFadeInLength() { return m_TrailFadeInLength; }
}
