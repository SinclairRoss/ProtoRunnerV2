package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   05/08/2017

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RenderCamera
{
    private Vector3 m_Position;
    private Vector3 m_Up;
    private Vector3 m_LookAt;

    private float[] m_PosArray;
    private float[] m_UpArray;
    private float[] m_LookAtArray;

    public RenderCamera()
    {
        m_Position = new Vector3();
        m_LookAt = new Vector3();
        m_Up = new Vector3();

        m_PosArray = new float[3];
        m_UpArray = new float[3];
        m_LookAtArray = new float[3];
    }

    public void SetupForCamera(ChaseCamera camera)
    {
        m_Position.SetVector(camera.GetPosition());
        m_LookAt.SetVector(camera.GetLookAt());
        m_Up.SetVector(camera.GetUp());
    }

    public float[] GetPosition()
    {
        m_PosArray[0] = (float)m_Position.X;
        m_PosArray[1] = (float)m_Position.Y;
        m_PosArray[2] = (float)m_Position.Z;

        return m_PosArray;
    }

    public float[] GetLookAt()
    {
        m_LookAtArray[0] = (float)m_LookAt.X;
        m_LookAtArray[1] = (float)m_LookAt.Y;
        m_LookAtArray[2] = (float)m_LookAt.Z;

        return m_LookAtArray;
    }

    public float[] GetUp()
    {
        m_UpArray[0] = (float)m_Up.X;
        m_UpArray[1] = (float)m_Up.Y;
        m_UpArray[2] = (float)m_Up.Z;

        return m_UpArray;
    }
}