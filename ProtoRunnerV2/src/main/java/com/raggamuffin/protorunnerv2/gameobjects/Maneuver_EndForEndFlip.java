package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   22/04/2017

import com.raggamuffin.protorunnerv2.utils.Quaternion;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Maneuver_EndForEndFlip
{
    private final double ManeuverDuration = 5;

    private Vehicle m_Anchor;
    private Timer m_DurationTimer;

    private double m_PitchTurnRate;

    private Vector3 m_Axis;
    private Quaternion m_Rotor;
    private Quaternion m_Origin;
    private Quaternion m_Destination;

    private boolean m_Active;

    public Maneuver_EndForEndFlip(Vehicle anchor)
    {
        m_Anchor = anchor;
        m_DurationTimer = new Timer(ManeuverDuration);

        m_PitchTurnRate = Math.PI / ManeuverDuration;

        m_Axis = new Vector3();
        m_Rotor = new Quaternion();
        m_Origin = new Quaternion();
        m_Destination = new Quaternion();
    }

    public void Update(double deltaTime)
    {
        if(m_Active)
        {
            m_Rotor.Slerp(m_Origin, m_Destination, m_DurationTimer.GetProgress());
            m_Anchor.SetRotation(m_Rotor);

            if(m_DurationTimer.HasElapsed())
            {
                EndManeuver();
            }
        }
    }

    public void StartManeuver()
    {
        if(!m_Active)
        {
            Vector3 anchorRight = m_Anchor.GetRight();
            Vector3 anchorForward = m_Anchor.GetForward();

            double radians = Vector3.RadiansBetween(Vector3.FORWARD, anchorForward);
            double determinant = Vector3.Determinant(Vector3.FORWARD, anchorForward);

            if(determinant > 0)
            {
                radians = (Math.PI * 2) - radians;
            }

            m_Origin.SetQuaternion(Vector3.UP, radians);
            m_Destination.SetQuaternion(Vector3.UP, radians + Math.PI);

            m_Anchor.LockSteering();
            m_DurationTimer.Start();
            m_Active = true;
        }
    }

    private void EndManeuver()
    {
        m_Anchor.Reverse();
        m_Active = false;

     //   Vector3 forward = m_Anchor.GetForward();
     //   forward.Y = 0;
     //   forward.Normalise();
    //    m_Anchor.SetForward(forward);
    }
}
