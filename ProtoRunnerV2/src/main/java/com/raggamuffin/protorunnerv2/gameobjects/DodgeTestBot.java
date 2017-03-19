package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class DodgeTestBot extends Vehicle
{
    private Vector3 m_Target;

    public DodgeTestBot(GameLogic game)
    {
        super(game, ModelType.WeaponDrone, 1);

        SetAffiliation(AffiliationKey.RedTeam);

        SetColour(Colours.CalvinOrange);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(0);//2
        m_Engine.SetMaxEngineOutput(0);
        m_Engine.SetAfterBurnerOutput(0);

        m_Target = new Vector3(0, -1, 1);
        m_Target.Normalise();

       SelectWeapon(new Weapon_None(this, game));
    }

    @Override
    public boolean IsValid()
    {
        return true;
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);
    }
}
