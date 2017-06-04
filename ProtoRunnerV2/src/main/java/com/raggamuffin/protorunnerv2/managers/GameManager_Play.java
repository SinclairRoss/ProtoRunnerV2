package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.HexPlosive;

public class GameManager_Play extends GameManager
{
    private VehicleManager m_VehicleManager;
    private EnemyWaveManager m_WaveManager;

    private Timer m_EmissionTimer;
    private Timer m_TestTimer;
    private ParticleEmitter_Burst m_TestEmitter;

    public GameManager_Play(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();
        m_WaveManager = new EnemyWaveManager(game);

        m_EmissionTimer = new Timer(6);
        m_TestTimer = new Timer(0.25);
    }

    @Override
    public void Initialise()
    {
        m_VehicleManager.SpawnPlayer();

        if(GameLogic.TEST_MODE)
        {
            m_EmissionTimer.StartElapsed();
            m_TestTimer.StartElapsed();
            //m_TestEmitter = new ParticleEmitter_Burst(m_Game, new Colour(Colours.VioletRed), new Colour(Colours.Cyan), 100);
          //  m_VehicleManager.SpawnVehicle(VehicleType.Bit, 0, 0, 15);
        }

        m_Game.GetPopperController().On();
    }

    static int i = 0;

    @Override
    public void Update(double deltaTime)
    {
        if (!GameLogic.TEST_MODE)
        {
            m_WaveManager.Update(deltaTime);
        }
        else
        {
            if(m_EmissionTimer.HasElapsed())
            {
                if(m_TestTimer.HasElapsed())
                {
                    //double spacing = 1;
                    //double deltaY = (HexPlosive.RADIUS * 0.86603) + spacing;
                    //double deltaX = (HexPlosive.RADIUS * 1.5) + spacing;
//
                    //if(i < 6)
                    //{
                    //    GameObject obj = new HexPlosive(m_Game);
                    //    obj.SetPosition(i % 2 == 0 ? 0 : deltaX, 0, deltaY * i);
                    //    m_Game.GetGameObjectManager().AddObject(obj);
//
                    //    m_TestTimer.Start();
                    //    ++i;
                    //}
                    //else
                    //{
                    //    m_EmissionTimer.Start();
                    //    i = 0;
                    //}
                }
            }

            if(m_VehicleManager.GetTeam(AffiliationKey.RedTeam).isEmpty())
            {
                m_VehicleManager.SpawnVehicle(VehicleType.Bit, 0, 0, 15);
               //m_VehicleManager.SpawnVehicle(VehicleType.Warlord, 0, 0, 15);
            }

           // if(m_EmissionTimer.HasElapsed())
           // {
           //     m_EmissionTimer.Start();
           //    // m_TestEmitter.Burst();
           // }
        }
    }

    @Override
    public void CleanUp()
    {
        m_Game.GetPopperController().Off();
    }
}
