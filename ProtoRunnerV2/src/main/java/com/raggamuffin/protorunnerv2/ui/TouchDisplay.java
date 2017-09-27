package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   02/06/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.master.TouchPointer;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

import junit.framework.Assert;

import java.util.ArrayList;

public class TouchDisplay
{
    private GameLogic m_Game;

    private ArrayList<UIElement_TouchMarker> m_ActiveTouchMarkers;
    private ArrayList<UIElement_TouchMarker> m_InvalidTouchMarkers;

    public TouchDisplay(GameLogic game)
    {
        m_Game = game;

        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.OnPointerDown, new OnTouchSubscriber());

        m_ActiveTouchMarkers = new ArrayList<>();
        m_InvalidTouchMarkers = new ArrayList<>();
    }

    public void Update(double deltaTime)
    {
        int numActiveMarkers = m_ActiveTouchMarkers.size();
        for(int i = 0; i < numActiveMarkers; ++i)
        {
            UIElement_TouchMarker marker = m_ActiveTouchMarkers.get(i);
            if(marker.IsActive())
            {
                marker.Update(deltaTime);
            }
            else
            {
                RemoveTouchMarker(marker);

                --numActiveMarkers;
                --i;
            }
        }
    }

    private UIElement_TouchMarker CreateTouchMarker(TouchPointer pointer)
    {
        UIElement_TouchMarker touchMarker;

        if(m_InvalidTouchMarkers.isEmpty())
        {
           touchMarker = new UIElement_TouchMarker();
        }
        else
        {
            int arrayLength = m_InvalidTouchMarkers.size();
            touchMarker = m_InvalidTouchMarkers.remove(arrayLength - 1);
        }

        touchMarker.Initialise(pointer);
        m_ActiveTouchMarkers.add(touchMarker);

        return touchMarker;
    }

    private void RemoveTouchMarker(UIElement_TouchMarker marker)
    {
        marker.CleanUp();

        m_ActiveTouchMarkers.remove(marker);
        m_InvalidTouchMarkers.add(marker);
    }

    public UIElement_TouchMarker GetMarkerWithID(int id)
    {
        UIElement_TouchMarker marker = null;

        int markerCount = m_ActiveTouchMarkers.size();
        for(int i = 0; i < markerCount; ++i)
        {
            marker = m_ActiveTouchMarkers.get(i);
            if(marker.GetTouchPointerID() == id)
            {
                break;
            }
        }

        return marker;
    }

    private class OnTouchSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            TouchPointer pointer = (TouchPointer) args;
            Assert.assertNotNull(pointer);

            CreateTouchMarker((TouchPointer) args);
        }
    }
}
