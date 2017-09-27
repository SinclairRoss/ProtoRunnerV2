package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject_Trail;
import com.raggamuffin.protorunnerv2.utils.Vector3;

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

    public void Initialise(float[] projMatrix)
    {
        m_Trail.InitialiseModel(projMatrix);
    }

    public void Draw(RenderObject_Trail trail, Vector3 eye)
    {
        m_Trail.Draw(trail, eye);
    }

    public void Clean()
    {
        m_Trail.CleanModel();
    }
}
