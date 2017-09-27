package com.raggamuffin.protorunnerv2.gameobjects;


// Author: Sinclair Ross
// Date:   08/03/2017


import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Bot_ParticleEmitter extends GameObject
{
    ParticleEmitter_Burst m_BurstEmitter;
    Timer m_EmissionTimer;

    public Bot_ParticleEmitter(GameLogic game)
    {
        super(ModelType.WeaponDrone, 1);

        m_BurstEmitter = new ParticleEmitter_Burst(game, new Colour(Colours.VioletRed), new Colour(Colours.Cyan));
        m_EmissionTimer = new Timer(3.0);
        m_EmissionTimer.Start();
    }

    @Override
    public void Update(double deltaTime)
    {
        if(m_EmissionTimer.HasElapsed())
        {
            m_EmissionTimer.Start();
            m_BurstEmitter.Burst();
        }
    }

    @Override
    public boolean IsValid()
    {
        return true;
    }

    @Override
    public void CleanUp()
    {}
}
