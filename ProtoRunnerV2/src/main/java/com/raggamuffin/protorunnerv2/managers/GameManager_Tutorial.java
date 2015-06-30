package com.raggamuffin.protorunnerv2.managers;

import android.content.Context;
import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.ai.VehicleInfo;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Boost;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Destroy;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Dodge;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Message;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Movement;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_ShotsFired;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_SwitchWeapon;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Time;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_TurnAmount;
import com.raggamuffin.protorunnerv2.tutorial.TutorialEffect;
import com.raggamuffin.protorunnerv2.tutorial.TutorialEvent;
import com.raggamuffin.protorunnerv2.tutorial.TutorialEvent_HealthBar;
import com.raggamuffin.protorunnerv2.tutorial.TutorialEvent_Immortality;
import com.raggamuffin.protorunnerv2.ui.TutorialScreen;
import com.raggamuffin.protorunnerv2.ui.UIScreens;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

import java.util.ArrayList;

public class GameManager_Tutorial extends GameManager
{
    private enum TutorialState
    {
        Starting,
        Idle,
        Inbetween,
        Complete
    }

    private TutorialState m_TutorialState;

    private ArrayList<TutorialCondition> m_Conditions;
    private TutorialCondition m_ActiveCondition;
    private Timer m_InbetweenTimer;
    private int m_TutorialIndex;

    private Publisher m_TutorialCompletePublisher;

    private TutorialEvent m_Immortality;
    private TutorialEvent m_HealthBar;

    public GameManager_Tutorial(GameLogic game)
    {
        super(game);

        m_TutorialState = TutorialState.Starting;

        m_TutorialIndex = 0;
        m_Conditions = new ArrayList<TutorialCondition>();

        m_InbetweenTimer = new Timer(1.0);
        m_TutorialCompletePublisher = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.TutorialComplete);
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.NextTutorialButtonPressed, new NextButtonPressedSubscriber());
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Immortality.Update();

        switch (m_TutorialState)
        {
            case Starting:
            {
                m_TutorialState = TutorialState.Idle;
                m_ActiveCondition.Initialise();
                ActivateTutorialEffects(m_ActiveCondition.GetEffects());
                break;
            }
            case Idle:
            {
                m_ActiveCondition.Update(deltaTime);

                if (m_ActiveCondition.ConditionComplete())
                {
                    m_TutorialState = TutorialState.Inbetween;
                    m_ActiveCondition.Reset();
                }
                break;
            }
            case Inbetween:
            {
                m_InbetweenTimer.Update(deltaTime);

                if (m_InbetweenTimer.TimedOut())
                {
                    m_TutorialState = TutorialState.Starting;
                    m_InbetweenTimer.ResetTimer();
                    NextState();
                }

                break;
            }
        }
    }

    @Override
    public void Initialise()
    {
        m_TutorialState = TutorialState.Starting;

        VehicleManager vManager = m_Game.GetVehicleManager();
        vManager.SpawnPlayer();

        Context context = m_Game.GetContext();


        m_Conditions.add(new TutorialCondition_Time(m_Game, context.getString(R.string.tutorial_start), 3.0, TutorialEffect.Immortality_On));
                /*
        m_Conditions.add(new TutorialCondition_TurnAmount(m_Game, context.getString(R.string.tutorial_turning), 3));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_screen)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_left_side)));
        m_Conditions.add(new TutorialCondition_ShotsFired(m_Game, context.getString(R.string.tutorial_firing), 16));
        m_Conditions.add(new TutorialCondition_SwitchWeapon(m_Game, context.getString(R.string.tutorial_burst_equip), WeaponSlot.Right));
        m_Conditions.add(new TutorialCondition_ShotsFired(m_Game,context.getString(R.string.tutorial_firing), 4));
        m_Conditions.add(new TutorialCondition_SwitchWeapon(m_Game, context.getString(R.string.tutorial_rocket_equip), WeaponSlot.Up));
        m_Conditions.add(new TutorialCondition_ShotsFired(m_Game, context.getString(R.string.tutorial_rocket_fire), 8));
        m_Conditions.add(new TutorialCondition_SwitchWeapon(m_Game, context.getString(R.string.tutorial_re_equip_pulse), WeaponSlot.Left));
        m_Conditions.add(new TutorialCondition_Message(m_Game,context.getString(R.string.tutorial_right_side)));
        m_Conditions.add(new TutorialCondition_Boost(m_Game, context.getString(R.string.tutorial_boost), 5));
        m_Conditions.add(new TutorialCondition_Dodge(m_Game, context.getString(R.string.tutorial_dodge), 1));
        m_Conditions.add(new TutorialCondition_Movement(m_Game, context.getString(R.string.tutorial_strafe_left), VehicleInfo.MovementStates.StrafeLeft));
        m_Conditions.add(new TutorialCondition_Movement(m_Game, context.getString(R.string.tutorial_strafe_right), VehicleInfo.MovementStates.StrafeRight));
        m_Conditions.add(new TutorialCondition_Movement(m_Game, context.getString(R.string.tutorial_reverse), VehicleInfo.MovementStates.Reverse));
        m_Conditions.add(new TutorialCondition_Movement(m_Game, context.getString(R.string.tutorial_forward), VehicleInfo.MovementStates.Normal));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_1)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_2)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_3)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_4)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_5)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_6)));
        m_Conditions.add(new TutorialCondition_Destroy(m_Game, context.getString(R.string.tutorial_radar_7), 3));
        */
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_1), TutorialEffect.HealthBar_On));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_2)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_3)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_4)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_5)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_6)));
        m_Conditions.add(new TutorialCondition_Destroy(m_Game, context.getString(R.string.tutorial_destroy), 3, TutorialEffect.Immortality_Off));
        m_Conditions.add(new TutorialCondition_Time(m_Game, m_Game.GetContext().getString(R.string.tutorial_end), 3.0));

        m_ActiveCondition = m_Conditions.get(0);

        m_TutorialIndex = 0;

        TutorialScreen tutorialScreen = (TutorialScreen)m_Game.GetUIManager().GetScreen(UIScreens.Tutorial);

        m_Immortality = new TutorialEvent_Immortality(m_Game);
        m_HealthBar = new TutorialEvent_HealthBar(m_Game, tutorialScreen);
    }

    @Override
    public void CleanUp()
    {
        m_Conditions.clear();
        m_Immortality = null;
        m_HealthBar = null;
    }

    private void NextState()
    {
        m_TutorialIndex ++;

        int numConditions = m_Conditions.size();

        if(m_TutorialIndex < numConditions)
        {
            m_ActiveCondition = m_Conditions.get(m_TutorialIndex);
        }
        else
        {
            m_TutorialState = TutorialState.Complete;
            m_TutorialCompletePublisher.Publish();
        }
    }

    public double GetConditionProgress()
    {
        if(m_ActiveCondition == null)
            return 1.0;

        return m_ActiveCondition.GetProgress();
    }

    private void ActivateTutorialEffects(TutorialEffect... effects)
    {
        for(TutorialEffect effect : effects)
        {
            switch(effect)
            {
                case Immortality_On:
                    m_Immortality.On();
                    break;
                case Immortality_Off:
                    m_Immortality.Off();
                    break;
                case HealthBar_On:
                    m_HealthBar.On();
                    break;
                case HealthBar_Off:
                    m_HealthBar.Off();
                    break;
            }
        }
    }

    private class NextButtonPressedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_TutorialState = TutorialState.Inbetween;
            m_ActiveCondition.Reset();
        }
    }
}
