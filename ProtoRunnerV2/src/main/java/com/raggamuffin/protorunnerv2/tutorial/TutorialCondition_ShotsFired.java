package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.ai.VehicleInfo;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class TutorialCondition_ShotsFired extends TutorialCondition
{
    private int m_MaxAmount;
    private int m_Amount;
    private WeaponSlot m_WeaponSlot;

    public TutorialCondition_ShotsFired(GameLogic game, String message, int amount, WeaponSlot slot, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.ProgressBar, effects);

        m_MaxAmount = amount;
        m_Amount = 0;
        m_WeaponSlot = slot;
    }

    @Override
    public void Update(double deltaTime)
    {
        Runner player = m_Game.GetVehicleManager().GetPlayer();

        if(player == null)
            return;

        if(player.GetWeaponSlot() != m_WeaponSlot)
            return;

        if(player.GetPrimaryWeapon().IsFiring())
        {
            m_Amount ++;
        }

    }

    @Override
    public boolean ConditionComplete()
    {
        return m_Amount >= m_MaxAmount;
    }

    @Override
    public double GetProgress()
    {
        return MathsHelper.Normalise(m_Amount, 0, m_MaxAmount);
    }

    @Override
    public void Reset()
    {
        super.Reset();
        m_Amount = 0;
    }
}
