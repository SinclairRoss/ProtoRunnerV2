package com.raggamuffin.protorunnerv2.RenderObjects;


// Author: Sinclair Ross
// Date:   05/10/2017

import com.raggamuffin.protorunnerv2.ui.UIElement_Radial;

public class RenderGroup_Radial extends RenderGroup_UI
{
    private final float[] m_Progress;
    private final float[] m_Temp;

    RenderGroup_Radial()
    {
        m_Progress = new float[INSTANCE_CAP];
        m_Temp = new float[1];
    }

    public void AddRadial(UIElement_Radial radial)
    {
        m_Temp[0] = (float)radial.GetProgress();
        AddToArray(m_Progress, m_Temp, 1);
        AddElement(radial);
    }
}
