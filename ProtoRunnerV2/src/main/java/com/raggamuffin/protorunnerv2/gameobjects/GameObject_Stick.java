package com.raggamuffin.protorunnerv2.gameobjects;


// Author: Sinclair Ross
// Date:   18/04/2017


import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GameObject_Stick extends GameObject
{
    private Vector3 m_EndPoint;
    private Timer m_LifeTimer;

    public GameObject_Stick(Vector3 startPoint, Vector3 endPoint, Colour colour)
    {
        super(ModelType.ParticleLaser, 0);

        SetColour(colour);

        SetPositionByRef(startPoint);
        m_EndPoint = new Vector3(endPoint);

        m_LifeTimer = new Timer(1);
        m_LifeTimer.Start();
    }

    @Override
    public void Update(double deltaTime)
    {
        LookAt(m_EndPoint);
        SetScale(0.1, 0.1, Vector3.DistanceBetween(GetPosition(), m_EndPoint));

        SetAlpha(m_LifeTimer.GetInverseProgress());
    }

    @Override
    public boolean IsValid() { return !m_LifeTimer.HasElapsed(); }
}
