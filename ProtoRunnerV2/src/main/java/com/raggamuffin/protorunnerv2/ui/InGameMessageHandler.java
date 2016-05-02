package com.raggamuffin.protorunnerv2.ui;

import android.util.Log;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;

public class InGameMessageHandler
{
    private final double LINE_HEIGHT = 0.15;

    private GameLogic m_Game;
    private UIManager m_UIManager;
    private GameAudioManager m_Audio;

	private boolean m_Active;
	private boolean m_Running;

	private ArrayList<UILabel> m_Labels;
	private String m_OriginalMessageText;
	private Timer m_MessageTimer;
	private int m_Priority;
	
	public InGameMessageHandler(GameLogic game, UIManager uiManager)
	{
        m_Game = game;
        m_UIManager = uiManager;
        m_Audio = m_Game.GetGameAudioManager();

        m_Active = false;
		m_Running = false;

        m_Labels = new ArrayList<>();

		m_OriginalMessageText = "";
		m_MessageTimer = new Timer(0.0);
        m_Priority = Integer.MIN_VALUE;
	}

	public void DisplayMessage(String text, MessageOrientation orientation, double width, int priority, double duration, double delay)
	{
		if(m_Active)
        {
            // If the current message is of higher priority than the new incoming message.
            if (priority > m_Priority)
            {
                Clear();

                if (duration < 0)
                {
                    m_MessageTimer.SetLimit(Double.MAX_VALUE);
                }
                else
                {
                    m_MessageTimer.SetLimit(duration);
                }

                m_Running = true;
                m_Priority = priority;

                CreateLabels(text, width * m_UIManager.GetScreenRatio() * 2, orientation);
                ShowLabels(delay);

                m_MessageTimer.ResetTimer();
            }
        }
	}

	public void Update(double deltaTime)
	{
		if(m_Running)
        {
            m_MessageTimer.Update(deltaTime);

            if (m_MessageTimer.TimedOut())
            {
                Clear();
            }
        }
	}

    private void CreateLabels(String text, double width, MessageOrientation orientation)
    {
        String[] words = text.split(" ");

        int index = 0;
        int numWords = words.length;

        int lineIndex = 0;

        while(index < numWords)
        {
            UILabel label = new UILabel(m_Audio, m_UIManager);

            ArrayList<String> lineArray = new ArrayList<String>();
            double fontSize = label.GetFont().GetSize();

            double remainingSpace = width;

            while(true)
            {
                if(index >= numWords)
                    break;

                String word = words[index];
                double wordSize = (word.length() + 1) * fontSize;

                if(remainingSpace - wordSize < 0)
                    break;

                lineArray.add(word);
                remainingSpace -= wordSize;

                index++;
            }

            if(lineArray.size() == 0)
            {
                lineArray.add(words[index]);
                index++;
            }

            String line = "";

            for(String word : lineArray)
                line += word + " ";

            label.SetText(line);
            SetUpLabel(label, orientation, lineIndex);
            m_UIManager.AddUIElement(label, false);
            m_Labels.add(label);

            lineIndex ++;
        }
    }

	private void SetUpLabel(UILabel message, MessageOrientation orientation, int lineIndex)
	{
        double lineOffset = LINE_HEIGHT * lineIndex;

		switch(orientation)
		{
			case Center:
                message.SetPosition(0, 0 - lineOffset);
                message.CentreHorizontal();
				break;
				
			case Left:
                message.SetPosition(-1, 0 - lineOffset);
                message.AlignLeft();
				break;
				
			case Right:
                message.SetPosition(1, 0 - lineOffset);
                message.AlignRight();
				break;

            case Top:
                message.SetPosition(0, 0.7 - lineOffset);
                message.CentreHorizontal();
                break;
		}
	}

	public void Activate()
	{
		m_Active = true;
	}
	
	public void Deactivate()
	{
		m_Active = false;
	}
	
	public void Clear()
	{
        for(Iterator<UILabel> Iter = m_Labels.iterator(); Iter.hasNext();)
        {
            m_UIManager.RemoveUIElement(Iter.next());
        }

        m_Labels.clear();

        m_Running = false;
        m_Priority = Integer.MIN_VALUE;
	}

    private void ShowLabels(double delay)
    {
        for(Iterator<UILabel> Iter = m_Labels.iterator(); Iter.hasNext();)
        {
            Iter.next().Show(delay);
        }
    }
}