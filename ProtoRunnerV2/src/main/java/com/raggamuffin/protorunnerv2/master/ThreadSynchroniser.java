package com.raggamuffin.protorunnerv2.master;

// Author: Sinclair Ross
// Date:   26/07/2017

import android.util.Log;

import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class ThreadSynchroniser
{
    private static String TAG = "Thread Synchroniser";

    private PubSubHub m_PubSubHub;

    private GameThread m_GameThread;
    private GameView m_GameView;

    public ThreadSynchroniser(PubSubHub pubSub, GameThread gameThread, GameView gameView)
    {
        m_PubSubHub = pubSub;

        m_GameThread = gameThread;
        m_GameView = gameView;

        m_PubSubHub.SubscribeToTopic(PublishedTopics.LogicThreadComplete, new Subscriber_LogicThreadComplete());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.RenderThreadComplete, new Subscriber_RenderThreadComplete());

        m_GameThread.start();
    }

    private class Subscriber_LogicThreadComplete extends Subscriber
    {
        @Override
        public void Update(Object args)
        {}
    }

    private class Subscriber_RenderThreadComplete extends Subscriber
    {
        @Override
        public void Update(Object args)
        {}
    }

    private void RequestLogicUpdate()
    {
        m_GameThread.run();
    }

    private void RequestRender()
    {
        m_GameView.requestRender();
    }

    public void Pause()
    {
        m_GameView.onPause();
        m_GameThread.pauseThread();
    }

    public void Resume()
    {
        m_GameView.onResume();
        m_GameThread.resumeThread();
    }

    public void Destroy()
    {
        m_GameThread.DestroyThread();

        try
        {
            Log.e(TAG, "Thread Join");
            m_GameThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            Log.e(TAG, "Thread Interrupt");
            m_GameThread.interrupt();
        }
    }
}

