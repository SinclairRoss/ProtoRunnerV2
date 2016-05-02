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
import com.raggamuffin.protorunnerv2.utils.Timer;

public class BeeperBot extends Vehicle
{
    Timer m_OnTimer;
    Timer m_OffTimer;

    boolean m_On;

    AudioEmitter m_Beeper;

    public BeeperBot(GameLogic game)
    {
        super(game);

        m_Model = ModelType.Dummy;
        SetBaseColour(Colours.IndianRed);
        SetAffiliation(AffiliationKey.RedTeam);

        m_On = true;

        m_OnTimer = new Timer(5.0);
        m_OffTimer = new Timer(1.0);

        m_Beeper = new AudioEmitter_Point(this, game.GetGameAudioManager(), AudioClips.FlareEngaged, EAudioRepeatBehaviour.Manual);
        m_Beeper.Start();

        m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);

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

            if(m_OnTimer.TimedOut())
            {
                m_On = false;
                m_Beeper.Stop();
                m_OnTimer.ResetTimer();
            }
        }
        else
        {
            m_OffTimer.Update(deltaTime);

            if(m_OffTimer.TimedOut())
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
        m_Beeper.Destroy();
    }
}
