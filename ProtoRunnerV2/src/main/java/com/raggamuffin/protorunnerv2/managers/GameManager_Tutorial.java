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
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Reboot;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_ShotsFired;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_SwitchWeapon;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Time;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_TurnAmount;
import com.raggamuffin.protorunnerv2.tutorial.TutorialEffect;
import com.raggamuffin.protorunnerv2.tutorial.TutorialEvent_Immortality;
import com.raggamuffin.protorunnerv2.tutorial.TutorialEvent_LockStrafing;
import com.raggamuffin.protorunnerv2.tutorial.TutorialEvent_LockWeapon;
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

    TutorialScreen m_TutorialScreen;

    private Publisher m_TutorialCompletePublisher;

    private TutorialEvent_Immortality m_Immortality;
    private TutorialEvent_LockWeapon m_LockWeaponLeft;
    private TutorialEvent_LockWeapon m_LockWeaponRight;
    private TutorialEvent_LockWeapon m_LockWeaponUp;
    private TutorialEvent_LockWeapon m_LockWeaponDown;
    private TutorialEvent_LockStrafing m_LockStrafing;

    public GameManager_Tutorial(GameLogic game)
    {
        super(game);

        m_TutorialState = TutorialState.Starting;

        m_TutorialIndex = 0;
        m_Conditions = new ArrayList<>();

        m_InbetweenTimer = new Timer(1.0);
        m_TutorialCompletePublisher = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.TutorialComplete);
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.NextTutorialButtonPressed, new NextButtonPressedSubscriber());
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Immortality.Update();
        m_LockStrafing.Update();

        switch (m_TutorialState)
        {
            case Starting:
            {
                m_TutorialState = TutorialState.Idle;
                m_ActiveCondition.Initialise();
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
                    ActivateTutorialEffects(m_ActiveCondition.GetEffects());
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

        m_Conditions.add(new TutorialCondition_Time(m_Game, context.getString(R.string.tutorial_start), 3.0, TutorialEffect.Immortality_On, TutorialEffect.LockWeapon_Right, TutorialEffect.LockWeapon_Up, TutorialEffect.LockWeapon_Down, TutorialEffect.LockStrafe));
        m_Conditions.add(new TutorialCondition_TurnAmount(m_Game, context.getString(R.string.tutorial_turning), 3));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_screen)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_left_side), TutorialEffect.LeftOverlay_On));
        m_Conditions.add(new TutorialCondition_ShotsFired(m_Game, context.getString(R.string.tutorial_fire_pulse), 16, WeaponSlot.Left));
        m_Conditions.add(new TutorialCondition_SwitchWeapon(m_Game, context.getString(R.string.tutorial_burst_equip), WeaponSlot.Right, TutorialEffect.LockWeapon_Left, TutorialEffect.UnlockWeapon_Right));
        m_Conditions.add(new TutorialCondition_ShotsFired(m_Game,context.getString(R.string.tutorial_fire_burst), 4, WeaponSlot.Right));
        m_Conditions.add(new TutorialCondition_SwitchWeapon(m_Game, context.getString(R.string.tutorial_rocket_equip), WeaponSlot.Up, TutorialEffect.LockWeapon_Right, TutorialEffect.UnlockWeapon_Up));
        m_Conditions.add(new TutorialCondition_ShotsFired(m_Game, context.getString(R.string.tutorial_rocket_fire), 8, WeaponSlot.Up));
        m_Conditions.add(new TutorialCondition_SwitchWeapon(m_Game, context.getString(R.string.tutorial_mines_equip), WeaponSlot.Down, TutorialEffect.LockWeapon_Up, TutorialEffect.UnlockWeapon_Down));
        m_Conditions.add(new TutorialCondition_ShotsFired(m_Game, context.getString(R.string.tutorial_mines_fire), 12, WeaponSlot.Down));
        m_Conditions.add(new TutorialCondition_SwitchWeapon(m_Game, context.getString(R.string.tutorial_re_equip_pulse), WeaponSlot.Left, TutorialEffect.UnlockWeapon_Left, TutorialEffect.UnlockWeapon_Right, TutorialEffect.UnlockWeapon_Up));
        m_Conditions.add(new TutorialCondition_Message(m_Game,context.getString(R.string.tutorial_right_side), TutorialEffect.LeftOverlay_Off, TutorialEffect.RightOverlay_On));
        m_Conditions.add(new TutorialCondition_Boost(m_Game, context.getString(R.string.tutorial_boost), 5));
        m_Conditions.add(new TutorialCondition_Dodge(m_Game, context.getString(R.string.tutorial_dodge), 1));
        m_Conditions.add(new TutorialCondition_Movement(m_Game, context.getString(R.string.tutorial_strafe_left), VehicleInfo.MovementStates.StrafeLeft, TutorialEffect.UnlockStrafe));
        m_Conditions.add(new TutorialCondition_Movement(m_Game, context.getString(R.string.tutorial_strafe_right), VehicleInfo.MovementStates.StrafeRight));
        m_Conditions.add(new TutorialCondition_Movement(m_Game, context.getString(R.string.tutorial_reverse), VehicleInfo.MovementStates.Reverse));
        m_Conditions.add(new TutorialCondition_Movement(m_Game, context.getString(R.string.tutorial_forward), VehicleInfo.MovementStates.Normal));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_1), TutorialEffect.RightOverlay_Off));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_2)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_3)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_4)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_5)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_radar_6)));
        m_Conditions.add(new TutorialCondition_Destroy(m_Game, context.getString(R.string.tutorial_destroy), 3));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_1), TutorialEffect.HealthBar_On));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_2)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_3)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_4)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_5)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_health_6)));
        m_Conditions.add(new TutorialCondition_Destroy(m_Game, context.getString(R.string.tutorial_destroy), 3, TutorialEffect.Immortality_Off));
        m_Conditions.add(new TutorialCondition_SwitchWeapon(m_Game, context.getString(R.string.tutorial_re_equip_pulse), WeaponSlot.Left, TutorialEffect.Immortality_On, TutorialEffect.HealthBar_Off, TutorialEffect.LockWeapon_Down, TutorialEffect.UnlockWeapon_Left, TutorialEffect.UnlockWeapon_Right, TutorialEffect.UnlockWeapon_Up));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_wingmen_1)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_wingmen_2), TutorialEffect.SpawnWingmen));
        m_Conditions.add(new TutorialCondition_Time(m_Game, context.getString(R.string.empty), 5.0));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_wingmen_3)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.tutorial_wingmen_4)));
        m_Conditions.add(new TutorialCondition_Destroy(m_Game, context.getString(R.string.tutorial_destroy), 6, TutorialEffect.Immortality_Off, TutorialEffect.HealthBar_On));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.reboot_1), TutorialEffect.Immortality_On, TutorialEffect.HealthBar_Off));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.reboot_2)));
        m_Conditions.add(new TutorialCondition_Message(m_Game, context.getString(R.string.reboot_3)));
        m_Conditions.add(new TutorialCondition_Time(m_Game, context.getString(R.string.reboot_4), 1, TutorialEffect.NoInbetween));
        m_Conditions.add(new TutorialCondition_Reboot(m_Game, context.getString(R.string.empty)));
        m_Conditions.add(new TutorialCondition_Time(m_Game, context.getString(R.string.tutorial_end), 3.0));

        m_TutorialIndex = 0;
        m_ActiveCondition = m_Conditions.get(m_TutorialIndex);

        m_TutorialScreen = (TutorialScreen)m_Game.GetUIManager().GetScreen(UIScreens.Tutorial);

        m_Immortality       = new TutorialEvent_Immortality(m_Game);
        m_LockWeaponLeft    = new TutorialEvent_LockWeapon(m_Game, WeaponSlot.Left);
        m_LockWeaponRight   = new TutorialEvent_LockWeapon(m_Game, WeaponSlot.Right);
        m_LockWeaponUp      = new TutorialEvent_LockWeapon(m_Game, WeaponSlot.Up);
        m_LockWeaponDown    = new TutorialEvent_LockWeapon(m_Game, WeaponSlot.Down);
        m_LockStrafing      = new TutorialEvent_LockStrafing(m_Game);

        ActivateTutorialEffects(m_ActiveCondition.GetEffects());
    }

    @Override
    public void CleanUp()
    {
        m_Conditions.clear();
        m_Immortality = null;
        m_LockWeaponLeft = null;
        m_LockWeaponRight = null;
        m_LockWeaponUp = null;
        m_LockWeaponDown = null;
        m_LockStrafing = null;
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
                    m_TutorialScreen.ShowHealthBar();
                    break;
                case HealthBar_Off:
                    m_TutorialScreen.HideHealthBar();
                    break;
                case SpawnWingmen:
                    m_Game.GetVehicleManager().SpawnWingmen(-1);
                    m_Game.GetVehicleManager().SpawnWingmen(1);
                    break;
                case LeftOverlay_On:
                    m_TutorialScreen.ShowLeftPanel();
                    break;
                case LeftOverlay_Off:
                    m_TutorialScreen.HideLeftPanel();
                    break;
                case RightOverlay_On:
                    m_TutorialScreen.ShowRightPanel();
                    break;
                case RightOverlay_Off:
                    m_TutorialScreen.HideRightPanel();
                    break;
                case LockStrafe:
                    m_LockStrafing.On();
                    break;
                case UnlockStrafe:
                    m_LockStrafing.Off();
                    break;
                case LockWeapon_Left:
                    m_LockWeaponLeft.On();
                    break;
                case LockWeapon_Right:
                    m_LockWeaponRight.On();
                    break;
                case LockWeapon_Up:
                    m_LockWeaponUp.On();
                    break;
                case LockWeapon_Down:
                    m_LockWeaponDown.On();
                    break;
                case UnlockWeapon_Left:
                    m_LockWeaponLeft.Off();
                    break;
                case UnlockWeapon_Right:
                    m_LockWeaponRight.Off();
                    break;
                case UnlockWeapon_Up:
                    m_LockWeaponUp.Off();
                    break;
                case UnlockWeapon_Down:
                    m_LockWeaponDown.Off();
                    break;
                case NoInbetween:
                    m_InbetweenTimer.MaxOutTimer();
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