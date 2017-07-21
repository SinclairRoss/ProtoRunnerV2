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

    public GameManager_Play(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();
        m_WaveManager = new EnemyWaveManager(game);
    }

    @Override
    public void Initialise()
    {
        m_VehicleManager.SpawnPlayer();

        m_Game.GetPopperController().On();
    }

    static int i = 0;

    @Override
    public void Update(double deltaTime)
    {
        if (!GameLogic.TEST_MODE)
        {
            m_WaveManager.Update();
        }
        else
        {
            if(m_VehicleManager.GetTeam(AffiliationKey.RedTeam).isEmpty())
            {
                m_VehicleManager.SpawnVehicle(VehicleType.LaserStar, 0, 0, 15);
            }
        }
    }

    @Override
    public void CleanUp()
    {
        m_Game.GetPopperController().Off();
    }
}
