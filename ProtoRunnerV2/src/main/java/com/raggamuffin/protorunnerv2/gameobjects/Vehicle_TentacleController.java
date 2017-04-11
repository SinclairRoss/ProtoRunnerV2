package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   25/06/2016

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.ai.NavigationalBehaviourInfo;
import com.raggamuffin.protorunnerv2.ai.TargetingBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Vehicle_TentacleController extends Vehicle
{
    private static final double LATCHING_DISTANCE = 3.0;
    private final double ANCHOR_ATTACK_RANGE;

    private enum LatchState
    {
        Free,
        Latched,
        SnapingOff
    }

    private Vehicle m_Anchor;
    private AIController m_AIController;

    private LatchState m_LatchState;
    private Vehicle m_LatchTarget;

    private Shield m_Shield;

    private Vector3 m_ClampingVector;

    public Vehicle_TentacleController(GameLogic game, Vehicle anchor, double anchorAttackRange)
    {
        super(game, ModelType.Nothing, 0);

        m_Anchor = anchor;
        SetPosition(m_Anchor.GetPosition());
        ANCHOR_ATTACK_RANGE = anchorAttackRange;

        m_LatchState = LatchState.Free;
        m_LatchTarget = null;

        m_ClampingVector = new Vector3();

        m_Engine = new Engine(game, this);
        m_Engine.SetMaxTurnRate(15.0);
        m_Engine.SetMaxEngineOutput(80);
        m_Engine.SetDodgeOutput(0);

        SetAffiliation(AffiliationKey.RedTeam);

        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.0, 0.6);
        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), navInfo, AIBehaviours.TentacleSnare, FireControlBehaviour.None, TargetingBehaviour.Tentacle);
        m_AIController.SetLeader(anchor);

        m_CanBeTargeted = false;
        m_StatusEffectManager.ApplyStatusEffect(StatusEffect.Shielded);

        m_VehicleClass = VehicleClass.Drone;

        m_Shield = new Shield(this);
        game.GetGameObjectManager().AddObject(m_Shield);
    }

    public void Update(double deltaTime)
    {
        m_AIController.Update(deltaTime);

        UpdateLatchBehaviour();
        ConfineTentacleWithinAttackRange();

        super.Update(deltaTime);
    }

    private void UpdateLatchBehaviour()
    {
        switch(m_LatchState)
        {
            case Free:
            {
                FreeBehaviour();
                break;
            }
            case Latched:
            {
                LatchedBehaviour();
                break;
            }
            case SnapingOff:
            {
                SnappingOffBehaviour();
                break;
            }
        }
    }

    private void FreeBehaviour()
    {
        Vehicle target = m_AIController.GetSituationalAwareness().GetTargetSensor().GetTarget();

        if(target != null)
        {
            if (IsTargetWithinAnchorAttackRange(target))
            {
                if (IsTargetWithinLatchingRange(target))
                {
                    m_LatchTarget = target;
                    m_Engine.SetEngineOutput(0);
                    m_Shield.AttachToObject(m_LatchTarget);

                    m_LatchState = LatchState.Latched;
                }
            }
        }
    }

    private void LatchedBehaviour()
    {
        if(IsTargetWithinAnchorAttackRange(m_LatchTarget) && m_Anchor.IsValid())
        {
            SetPosition(m_LatchTarget.GetPosition());
        }
        else
        {
            m_Engine.SetEngineOutput(1);
            m_Shield.DetachFromObject();

            m_LatchState = LatchState.SnapingOff;
        }
    }

    private void SnappingOffBehaviour()
    {
        LookAt(m_Anchor.GetPosition());
        ApplyForce(GetForward(), 50);

        m_LatchTarget = null;
        m_LatchState = LatchState.Free;
    }

    private void ConfineTentacleWithinAttackRange()
    {
        double distanceToAnchorSqr = Vector3.GetDistanceBetweenSqr(m_Anchor.GetPosition(), GetPosition());

        if(distanceToAnchorSqr >= ANCHOR_ATTACK_RANGE * ANCHOR_ATTACK_RANGE)
        {
            m_ClampingVector.SetAsDifference(m_Anchor.GetPosition(), GetPosition());
            m_ClampingVector.Normalise();
            m_ClampingVector.Scale(ANCHOR_ATTACK_RANGE);
            m_ClampingVector.Add(m_Anchor.GetPosition());
            SetPosition(m_ClampingVector);
        }
    }

    private boolean IsTargetWithinAnchorAttackRange(Vehicle target)
    {
        double anchorDistanceToTargetSqr = Vector3.GetDistanceBetweenSqr(m_Anchor.GetPosition(), target.GetPosition());
        return (anchorDistanceToTargetSqr <= ANCHOR_ATTACK_RANGE * ANCHOR_ATTACK_RANGE);
    }

    private boolean IsTargetWithinLatchingRange(Vehicle target)
    {
        double distanceToTargetSqr = Vector3.GetDistanceBetweenSqr(GetPosition(), target.GetPosition());
        return (distanceToTargetSqr <= LATCHING_DISTANCE * LATCHING_DISTANCE);
    }

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid();
    }

    @Override
    public void CleanUp()
    {
        m_Shield.DetachFromObject();
        SendDeathMessage();
    }
}
