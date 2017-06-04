package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Ray;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Ripple;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class HexPlosive extends GameObject
{
    private final double TIME_SCALAR = 1.0;
    private final double EXPLOSION_DURATION = 1.0 * TIME_SCALAR;
    private final double PAUSE_BEFORE_EXPLOSION = 3.0 * TIME_SCALAR;
    private final double WIND_DOWN_DURATION = 0.5 * TIME_SCALAR;

    private final double MAX_FLOORGRID_ATTENUATION = 15;
    private final double MIN_FLOORGRID_ATTENUATION = 1;

    public static final double RADIUS = 7.0;
    private final double FINAL_HEIGHT = 150;

    private final double INNERSCALE = 0.5;

    private enum State
    {
        PauseBeforeExploding,
        Exploding,
        WindDown,
        Complete
    }

    private GameLogic m_Game;

    private State m_State;

    private FloorGrid m_FloorGrid;

    private Timer m_PauseBeforeExplosionTimer;
    private Timer m_ExplosionHeightTimer;
    private Timer m_WindDownTimer;

    private GameObject m_OuterTube;
    private GameObject m_InnerTube;

    private ParticleEmitter_Ray m_RayEmitter;
    private ParticleEmitter_Ripple m_RadialEmitter;

    public HexPlosive(GameLogic game)
    {
        super(ModelType.Nothing, 3.0);

        m_Game = game;

        SetColour(Colours.HannahExperimentalAB);

        m_PauseBeforeExplosionTimer = new Timer(PAUSE_BEFORE_EXPLOSION);
        m_ExplosionHeightTimer = new Timer(EXPLOSION_DURATION);
        m_WindDownTimer = new Timer(WIND_DOWN_DURATION);

        m_ExplosionHeightTimer.Start();

        m_OuterTube = CreateTube(GetColour(), 0.6, 1.0);
        m_Game.AddObjectToRenderer(m_OuterTube);

        m_InnerTube = CreateTube(GetColour(), 1.0, INNERSCALE);
        m_Game.AddObjectToRenderer(m_InnerTube);

        m_FloorGrid = new FloorGrid(GetPosition(), GetColour(), 10.0);
        game.AddObjectToRenderer(m_FloorGrid);

        m_RayEmitter = new ParticleEmitter_Ray(game, GetColour(), GetColour(), 0, 0.1, 1);
        m_RayEmitter.SetPosition(GetPosition());
        m_RayEmitter.SetForward(Vector3.UP);
        m_RayEmitter.SetLength(FINAL_HEIGHT * 0.2);

        m_RadialEmitter = new ParticleEmitter_Ripple(game, GetColour(), GetColour(), 80, 0.8);
        m_RadialEmitter.SetPosition(GetPosition());

        m_PauseBeforeExplosionTimer.Start();
        m_State = State.PauseBeforeExploding;
    }

    private GameObject CreateTube(Colour colour, double alpha, double scale)
    {
        GameObject tube = new GameObject(ModelType.HeagonTube, RADIUS);
        tube.SetScale(RADIUS * scale, 0, RADIUS * scale);
        tube.SetColour(colour);
        tube.SetAlpha(alpha);
        tube.SetPosition(GetPosition());
        tube.GetPosition().Y = -1;

        return tube;
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case PauseBeforeExploding:
            {
                m_RadialEmitter.Update(deltaTime);

                if(m_PauseBeforeExplosionTimer.HasElapsed())
                {
                    m_RadialEmitter.Burst();
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

                m_RayEmitter.Update(deltaTime);

                if(m_ExplosionHeightTimer.HasElapsed())
                {
                    m_State = State.WindDown;
                    m_WindDownTimer.Start();
                }

               break;
            }
            case WindDown:
            {
                m_RayEmitter.Update(deltaTime);
                SetAlpha(m_WindDownTimer.GetInverseProgress());

                if(m_WindDownTimer.HasElapsed())
                {
                    m_State = State.Complete;
                }

                break;
            }
            case Complete:
            {
                break;
            }
        }
    }

    @Override
    public boolean IsValid() { return m_State != State.Complete; }

    @Override
    public void CleanUp()
    {
        m_Game.RemoveObjectFromRenderer(m_InnerTube);
        m_Game.RemoveObjectFromRenderer(m_OuterTube);

        m_Game.RemoveObjectFromRenderer(m_FloorGrid);
    }

    @Override
    public void SetPosition(double x, double y, double z)
    {
        super.SetPosition(x, y, z);

        m_InnerTube.SetPosition(x, y, z);
        m_OuterTube.SetPosition(x, y, z);
        m_RayEmitter.SetPosition(x, y, z);
        m_RadialEmitter.SetPosition(x, y, z);
    }
}