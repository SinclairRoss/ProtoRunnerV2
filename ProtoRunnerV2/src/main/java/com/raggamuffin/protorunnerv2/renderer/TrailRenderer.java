package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.particles.TrailPoint;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TrailRenderer
{
    private GLModel_Trail m_Trail;

    public TrailRenderer()
    {
        m_Trail = null;
    }

    public void LoadAssets()
    {
        m_Trail = new GLModel_Trail();
    }

    public void Initialise(float[] projMatrix, Vector3 eye)
    {
        m_Trail.InitialiseModel(projMatrix, eye);
    }

    public void Draw(TrailPoint headNode)
    {
    //    DrawTest();

    //    if(true)
     //       return;

        TrailPoint node = headNode;

        while (node != null)
        {
            m_Trail.AddPoint(node);
            node = node.GetChild();
        }

        m_Trail.Draw();
    }

    private void DrawTest()
    {
       /* TrailPoint nodeA = new TrailPoint(new Vector3(-30, 0, 0), 10000, null);
        TrailPoint nodeB = new TrailPoint(new Vector3(-30, 0, 0), 10000, nodeA);

        m_Trail.AddPoint(nodeA);
        m_Trail.AddPoint(nodeB);

        m_Trail.Draw();*/
    }

    public void Clean()
    {
        m_Trail.CleanModel();
    }
}
