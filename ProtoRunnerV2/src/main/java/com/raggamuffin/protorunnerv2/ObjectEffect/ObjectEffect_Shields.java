package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   26/06/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.StatusEffect;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ObjectEffect_Shields extends ObjectEffect
{
    private ChaseCamera m_Camera;
    private Vector3 m_ToCamera;

    public ObjectEffect_Shields(GameLogic gameLogic)
    {
        super(ModelType.Shield, ObjectEffectType.Shields);

        m_Camera = gameLogic.GetCamera();
        m_ToCamera = new Vector3();
    }

    @Override
    public void Initialise(Vehicle anchor)
    {
        super.Initialise(anchor);
        SetScale(anchor.GetBoundingRadius() + 1.0);
        SetColour(anchor.GetColour());
    }

    @Override
    public void Update(double deltaTime)
    {
        SetPosition(GetAnchor().GetPosition());
        m_ToCamera.SetVectorAsDifference(GetPosition(), m_Camera.GetPosition());
        m_ToCamera.Normalise();

        SetForward(m_ToCamera);
    }

    @Override
    public boolean IsValid()
    {
        Vehicle anchor = GetAnchor();

        return anchor.HasStatusEffect(StatusEffect.Shielded) && anchor.IsValid();
    }
}
