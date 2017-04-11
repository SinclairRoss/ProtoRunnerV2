package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
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
    private ParticleEmitter_Burst m_TestEmitter;

    public GameManager_Play(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();
        m_WaveManager = new EnemyWaveManager(game);

        m_EmissionTimer = new Timer(10);
    }

    @Override
    public void Initialise()
    {
        m_VehicleManager.SpawnPlayer();

        if(GameLogic.TEST_MODE)
        {
            m_EmissionTimer.Start();
            //m_TestEmitter = new ParticleEmitter_Burst(m_Game, new Colour(Colours.VioletRed), new Colour(Colours.Cyan), 100);
           // m_VehicleManager.SpawnVehicle(VehicleType.Warlord, 0, 0, 15);
        }
        else
        {
            m_VehicleManager.SpawnVehicle(VehicleType.Wingman, 5, 0, 0);
            m_VehicleManager.SpawnVehicle(VehicleType.Wingman, -5, 0, 0);
        }

        m_Game.GetPopperController().On();
    }

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
                m_Game.GetGameObjectManager().AddObject(new HexPlosive(m_Game));
                m_EmissionTimer.Start();
            }

           // if(m_VehicleManager.GetTeam(AffiliationKey.RedTeam).isEmpty())
           // {
           //     m_VehicleManager.SpawnVehicle(VehicleType.Warlord, 0, 0, 15);
           // }

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
