package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.AudioEmitter;
import com.raggamuffin.protorunnerv2.audio.AudioEmitter_Point;
import com.raggamuffin.protorunnerv2.audio.EAudioRepeatBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class BeeperBot extends Vehicle
{
    Timer_Accumulation m_OnTimer;
    Timer_Accumulation m_OffTimer;

    boolean m_On;

    AudioEmitter m_Beeper;

    public BeeperBot(GameLogic game)
    {
        super(game, ModelType.Dummy, 1.0, 1, VehicleClass.StandardVehicle, true, PublishedTopics.EnemyDestroyed, AffiliationKey.RedTeam);

        SetColour(Colours.IndianRed);

        m_On = true;

        m_OnTimer = new Timer_Accumulation(5.0);
        m_OffTimer = new Timer_Accumulation(1.0);

        m_Beeper = new AudioEmitter_Point(this, game.GetGameAudioManager(), AudioClips.UI_Play, EAudioRepeatBehaviour.Manual);
        m_Beeper.Start();

        m_Engine = new Engine_Standard(this, game);
        SetEngineOutput(0.0);
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);

        if(m_On)
        {
            m_OnTimer.Update(deltaTime);

            if(m_OnTimer.HasElapsed())
            {
                m_On = false;
                m_Beeper.Stop();
                m_OnTimer.ResetTimer();
            }
        }
        else
        {
            m_OffTimer.Update(deltaTime);

            if(m_OffTimer.HasElapsed())
            {
                m_On = true;
                m_Beeper.Start();
                m_OffTimer.ResetTimer();
            }
        }
    }

    @Override
    public void CleanUp()
    {
        super.CleanUp();
        m_Beeper.Destroy();
    }
}
