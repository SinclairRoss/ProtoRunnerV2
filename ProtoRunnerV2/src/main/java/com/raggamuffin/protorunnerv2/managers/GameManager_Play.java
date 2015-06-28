package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

public class GameManager_Play extends GameManager
{
    public GameManager_Play(GameLogic game)
    {
        super(game);
    }

    @Override
    public void Update(double deltaTime)
    {

    }

    @Override
    public void Initialise()
    {
        VehicleManager vManager = m_Game.GetVehicleManager();
        vManager.SpawnWingmen();
        vManager.SpawnWingmen();
        vManager.SpawnPlayer();
    }
}
