package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   25/06/2016

import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TentacleController extends GameObject
{
    private enum TentacleState
    {
        Idle,
        Preparing,
        Attaching,
        Attached,
        Detaching
    }

    private TentacleState m_TentacleState;

    private Vehicle m_Anchor;

    public TentacleController(Vehicle anchor)
    {
        super(null, null);

        m_TentacleState = TentacleState.Idle;
        m_Anchor = anchor;
        m_Position = new Vector3(m_Anchor.GetPosition());
    }

    public void Update(double deltaTime)
    {
        switch(m_TentacleState)
        {
            case Idle:
                UpdateIdleBehaviour(deltaTime);
                break;
            case Preparing:
                UpdatedPreparingBehaviour(deltaTime);
                break;
            case Attaching:
                UpdateAttachingBehaviour(deltaTime);
                break;
            case Attached:
                UpdateAttachedBehaviour(deltaTime);
                break;
            case Detaching:
                UpdatedDetachBehaviour(deltaTime);
                break;
        }
    }

    private void UpdateIdleBehaviour(double deltaTime)
    {

    }

    private void UpdatedPreparingBehaviour(double deltaTime)
    {

    }

    private void UpdateAttachingBehaviour(double deltaTime)
    {

    }

    private void UpdateAttachedBehaviour(double deltaTime)
    {

    }

    private void UpdatedDetachBehaviour(double deltaTime)
    {

    }

    @Override
    public boolean IsValid()
    {
        return true;
    }

    @Override
    public void CleanUp()
    {

    }

}
