package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   18/02/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GameObject_OrientationMarker extends GameObject
{
    private GameLogic m_Game;
    private GameObject m_Anchor;

    private GameObject_Marker m_Marker_Forward;
    private GameObject_Marker m_Marker_Up;
    private GameObject_Marker m_Marker_Right;

    public GameObject_OrientationMarker(GameObject anchor, GameLogic game)
    {
        super(ModelType.WeaponDrone, 1);

        m_Game = game;
        m_Anchor = anchor;

        m_Marker_Forward = new GameObject_Marker(Colours.Red);
        m_Marker_Up = new GameObject_Marker(Colours.Green);
        m_Marker_Right = new GameObject_Marker(Colours.Blue);

        m_Marker_Forward.SetScale(0.3);
        m_Marker_Up.SetScale(0.3);
        m_Marker_Right.SetScale(0.3);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Marker_Forward.SetPosition(m_Anchor.GetForward());
        m_Marker_Forward.GetPosition().Scale(3);
        m_Marker_Forward.Translate(m_Anchor.GetPosition());

        m_Marker_Up.SetPosition(m_Anchor.GetUp());
        m_Marker_Up.GetPosition().Scale(3);
        m_Marker_Up.Translate(m_Anchor.GetPosition());

        m_Marker_Right.SetPosition(m_Anchor.GetRight());
        m_Marker_Right.GetPosition().Scale(3);
        m_Marker_Right.Translate(m_Anchor.GetPosition());
    }

    @Override
    public boolean IsValid()
    {
        return true;
    }

    @Override
    public void CleanUp()
    {}
}
