package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.HexPlosiveRing;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

import java.util.ArrayList;

public class HexPlosive extends GameObject
{
    private final double TIME_SCALAR = 1.0;
    private final double EXPLOSION_DURATION = 1.0 * TIME_SCALAR;
    private final double DELTA_RING_LIGHTING = 0.3 * TIME_SCALAR;
    private final double PAUSE_BEFORE_COUNTDOWN = 1.0 * TIME_SCALAR;
    private final double PAUSE_BEFORE_EXPLOSION = 0.5 * TIME_SCALAR;

    private final double MAX_FLOORGRID_ATTENUATION = 15;
    private final double MIN_FLOORGRID_ATTENUATION = 1;

    private final int NUM_RINGS = 3;
    private final double RADIUS = 3.0;
    private final double FINAL_HEIGHT = 150;
    private final double RING_HEIGHT = 5;

    private final double INNERSCALE = 0.5;

    private enum State
    {
        ReleasingRings,
        PauseBeforeCountdown,
        CountDownToExplosion,
        PauseBeforeExploding,
        Exploding,
        WindDown
    }

    private GameLogic m_Game;

    private State m_State;

    private FloorGrid m_FloorGrid;

    private Timer m_PauseBeforeCountdownTimer;
    private Timer m_LightNextRingTimer;
    private Timer m_PauseBeforeExplosionTimer;
    private Timer m_ExplosionHeightTimer;

    private GameObject m_OuterTube;
    private GameObject m_InnerTube;

    private int m_ActiveRingIndex;
    private ArrayList<HexPlosiveRing> m_Rings;

    public HexPlosive(GameLogic game)
    {
        super(ModelType.Nothing, 3.0);

        m_Game = game;

        SetColour(Colours.HannahExperimentalAB);

        m_State = State.ReleasingRings;

        m_PauseBeforeCountdownTimer = new Timer(PAUSE_BEFORE_COUNTDOWN);
        m_LightNextRingTimer = new Timer(DELTA_RING_LIGHTING);
        m_PauseBeforeExplosionTimer = new Timer(PAUSE_BEFORE_EXPLOSION);
        m_ExplosionHeightTimer = new Timer(EXPLOSION_DURATION);

        m_ExplosionHeightTimer.Start();

        m_OuterTube = CreateTube(Colours.HannahExperimentalAB, 0.6, 1.0);
        m_Game.AddObjectToRenderer(m_OuterTube);

        m_InnerTube = CreateTube(Colours.HannahExperimentalAB, 1.0, INNERSCALE);
        m_Game.AddObjectToRenderer(m_InnerTube);

        m_ActiveRingIndex = 0;
        CreateRings();

        m_FloorGrid = new FloorGrid(GetPosition(), GetColour(), 10.0);
        game.AddObjectToRenderer(m_FloorGrid);
    }

    private GameObject CreateTube(double[] colour, double alpha, double scale)
    {
        GameObject tube = new GameObject(ModelType.HeagonTube, RADIUS);
        tube.SetScale(RADIUS * scale, 0, RADIUS * scale);
        tube.SetColour(colour);
        tube.SetAlpha(alpha);
        tube.SetPosition(GetPosition());
        tube.GetPosition().Y = -1;

        return tube;
    }

    private void CreateRings()
    {
        m_Rings = new ArrayList<>(NUM_RINGS);

        HexPlosiveRing ring = new HexPlosiveRing(this, RADIUS, RING_HEIGHT, Colours.HannahExperimentalAB, Colours.HannahExperimentalAA);
        m_Game.GetGameObjectManager().AddObject(ring);
        m_Rings.add(ring);

        ring = new HexPlosiveRing(this, RADIUS * 0.75, RING_HEIGHT * 0.75, Colours.HannahExperimentalAB, Colours.HannahExperimentalAA);
        m_Game.GetGameObjectManager().AddObject(ring);
        m_Rings.add(ring);

        ring = new HexPlosiveRing(this, RADIUS * 0.5, RING_HEIGHT * 0.5, Colours.HannahExperimentalBA, Colours.HannahExperimentalAA);
        m_Game.GetGameObjectManager().AddObject(ring);
        m_Rings.add(ring);
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case ReleasingRings:
            {
                for(int i = 0; i < NUM_RINGS; ++i)
                {
                    m_Rings.get(i).Rise();
                }

                m_PauseBeforeCountdownTimer.Start();
                m_State = State.PauseBeforeCountdown;

                break;
            }
            case PauseBeforeCountdown:
            {
                if(m_PauseBeforeCountdownTimer.HasElapsed())
                {
                    m_LightNextRingTimer.Start();
                    m_State = State.CountDownToExplosion;
                }

                break;
            }
            case CountDownToExplosion:
            {
                if(m_LightNextRingTimer.HasElapsed())
                {
                    m_LightNextRingTimer.Start();

                    m_Rings.get(m_ActiveRingIndex).TurnOn();
                    ++m_ActiveRingIndex;

                    if(m_ActiveRingIndex >= NUM_RINGS)
                    {
                        m_PauseBeforeExplosionTimer.Start();
                        m_State = State.PauseBeforeExploding;
                    }
                }
            }
            case PauseBeforeExploding:
            {
                if(m_PauseBeforeExplosionTimer.HasElapsed())
                {
                    for(int i = 0; i < NUM_RINGS; ++i)
                    {
                        m_Rings.get(i).Invalidate();
                    }

                    m_FloorGrid.SetAttenuation(MAX_FLOORGRID_ATTENUATION);

                    m_ExplosionHeightTimer.Start();
                    m_State = State.Exploding;
                }
                break;
            }
            case Exploding:
            {
                double progress = m_ExplosionHeightTimer.GetProgress();
                double inverseProgress = 1.0 - progress;

                double height = progress * FINAL_HEIGHT;
                double width = inverseProgress * RADIUS;

                m_OuterTube.SetScale(width , height, width);
                m_InnerTube.SetScale(width * INNERSCALE, height, width * INNERSCALE);
                m_FloorGrid.SetAttenuation(MathsHelper.Lerp(progress, MIN_FLOORGRID_ATTENUATION, MAX_FLOORGRID_ATTENUATION));

                if(m_ExplosionHeightTimer.HasElapsed())
                {
                    m_State = State.WindDown;
                }

               break;
            }
            case WindDown:
            {
                break;
            }
        }
    }

    @Override
    public boolean IsValid() { return m_State != State.WindDown; }

    @Override
    public void CleanUp()
    {
        m_Game.RemoveObjectFromRenderer(m_InnerTube);
        m_Game.RemoveObjectFromRenderer(m_OuterTube);

        m_Game.RemoveObjectFromRenderer(m_FloorGrid);
    }
}
