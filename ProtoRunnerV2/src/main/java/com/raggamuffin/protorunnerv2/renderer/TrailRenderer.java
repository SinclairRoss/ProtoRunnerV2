package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.particles.TrailNode;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.concurrent.CopyOnWriteArrayList;

public class TrailRenderer
{
    private GLModel_Line m_Trail;

    public TrailRenderer()
    {
        m_Trail = null;
    }

    public void LoadAssets()
    {
        m_Trail = new GLModel_Line();
    }

    public void Initialise(float[] projMatrix, Vector3 eye, Trail trail)
    {
        m_Trail.InitialiseModel(projMatrix, eye, trail);
    }

    public void Draw()
    {
        m_Trail.Draw();
    }

    public void Clean()
    {
        m_Trail.CleanModel();
    }
}
