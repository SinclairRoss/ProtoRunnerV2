package com.raggamuffin.protorunnerv2.gameobjects;


// Author: Sinclair Ross
// Date:   19/03/2017


import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class Bot_Bashable extends Vehicle
{
    public Bot_Bashable(GameLogic game)
    {
        super(game, ModelType.Dummy, 2.0, 1, VehicleClass.StandardVehicle, true, PublishedTopics.EnemyDestroyed, AffiliationKey.RedTeam);
    }
}
