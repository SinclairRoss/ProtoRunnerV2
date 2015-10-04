package com.raggamuffin.protorunnerv2.renderer;


import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class BulletRenderer
{
    private GLPulseLaser m_Laser;

    public BulletRenderer()
    {
        m_Laser = null;
    }

    public void LoadAssets()
    {
        m_Laser = new GLPulseLaser();
    }

    public void Initialise(float[] projMatrix, Vector3 eye)
    {
        m_Laser.InitialiseModel(projMatrix, eye);
    }

    public void Draw(Vector3 pos, Colour colour, float size)
    {
        m_Laser.AddPoint(pos, colour, size);
    }

    public void Clean()
    {
        m_Laser.Draw();
        m_Laser.CleanModel();
    }
}
