package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   29/07/2017

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Matrix;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RenderObject
{
    private Matrix m_Transform;
    private Colour m_Colour;

    public RenderObject()
    {
        m_Transform = new Matrix();
        m_Colour = new Colour();
    }

    public void SetupForObject(GameObject object)
    {
        SetupForObject(object.GetPosition(), object.GetForward(), object.GetRight(), object.GetUp(), object.GetScale(), object.GetColour());
    }

    public void SetupForObject(Vector3 pos, Vector3 forward, Vector3 right, Vector3 up, Vector3 scale, Colour colour)
    {
        m_Transform.SetForward(forward);
        m_Transform.SetUp(up);
        m_Transform.SetRight(right);
        m_Transform.Scale(scale);
        m_Transform.SetPosition(pos);

        m_Colour.SetColour(colour);
    }

    public Matrix GetTransform() { return m_Transform; }

    public Vector3 GetPosition() { return null; }
    public Vector3 GetForward() { return null; }
    public Vector3 GetUp() { return null; }
    public Vector3 GetRight() { return null; }
    public Vector3 GetScale() { return null; }

    public Colour GetColour() { return m_Colour; }
}
