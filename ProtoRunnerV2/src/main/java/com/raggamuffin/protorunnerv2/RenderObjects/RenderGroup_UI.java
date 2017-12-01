package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   25/09/2017

import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public class RenderGroup_UI
{
    protected static int INSTANCE_CAP = 15;
    private int m_InstanceCount;

    private final float[] m_Positions;
    private final float[] m_Scales;
    private final float[] m_Colours;

    public RenderGroup_UI()
    {
        m_Positions = new float[INSTANCE_CAP * 2];
        m_Scales = new float[INSTANCE_CAP * 2];
        m_Colours = new float[INSTANCE_CAP * 4];
    }

    public void AddElement(UIElement element)
    {
        AddToArray(m_Positions, element.GetPosition().AsArray(), 2);
        AddToArray(m_Scales, element.GetScale().AsArray(), 2);
        AddToArray(m_Colours, element.GetColour().AsArray(), 4);

        ++m_InstanceCount;
    }

    protected void AddToArray(float[] array, float[] toAdd, int stride)
    {
        int initialIndex = m_InstanceCount * stride;
        int finalIndex = initialIndex + stride;
        int index = 0;

        for(int i = initialIndex; i < finalIndex; ++i)
        {
            array[i] = toAdd[index];
            ++index;
        }
    }

    public void Clean() { m_InstanceCount = 0; }

    public float[] GetPosition() { return m_Positions; }
    public float[] GetScales() { return m_Scales; }
    public float[] GetColours() { return m_Colours; }
    public int GetInstanceCount() { return m_InstanceCount; }
}
