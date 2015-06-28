package com.raggamuffin.protorunnerv2.ui;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.ui.Font.Alignment;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public class CharPicker 
{
	private final double CHAR_SPACING = 0.2;
	
	private final ArrayList<Character> m_Chars;
	
	private Vector2 m_Position;
	
	private GameAudioManager m_AudioManager;
	private UIManager m_UIManager;
	private ControlScheme m_ControlScheme;
	
	private UILabel m_CharLabel;
	private UIButton m_UpButton;
	private UIButton m_DownButton;
	
	private final int MAX_INDEX;
	private int m_CharIndex;
	private char m_Char;
	
	public CharPicker(double x, double y, GameAudioManager audio, UIManager uiManager, ControlScheme controlScheme)
	{
		m_Position = new Vector2(x,y);
		
		m_AudioManager = audio;
		m_UIManager = uiManager;
		m_ControlScheme = controlScheme;
		
		m_CharLabel = null;
		m_UpButton = null;
		m_DownButton = null;
		
		m_Chars = new ArrayList<Character>();
		
		m_Chars.add('A');
		m_Chars.add('B');
		m_Chars.add('C');
		m_Chars.add('D');
		m_Chars.add('E');
		m_Chars.add('F');
		m_Chars.add('G');
		m_Chars.add('H');
		m_Chars.add('I');
		m_Chars.add('J');
		m_Chars.add('K');
		m_Chars.add('L');
		m_Chars.add('M');
		m_Chars.add('N');
		m_Chars.add('O');
		m_Chars.add('P');		
		m_Chars.add('Q');
		m_Chars.add('R');
		m_Chars.add('S');
		m_Chars.add('T');
		m_Chars.add('U');
		m_Chars.add('V');
		m_Chars.add('W');
		m_Chars.add('X');
		m_Chars.add('Y');
		m_Chars.add('Z');
		
		m_Chars.add('0');
		m_Chars.add('1');
		m_Chars.add('2');
		m_Chars.add('3');
		m_Chars.add('4');
		m_Chars.add('5');
		m_Chars.add('6');
		m_Chars.add('7');
		m_Chars.add('8');
		m_Chars.add('9');
		
		m_Chars.add('.');
		m_Chars.add(' ');
		
		MAX_INDEX = m_Chars.size();
		m_CharIndex = 0;
		m_Char = m_Chars.get(m_CharIndex);
	}
	
	public void Create()
	{
		Create(0.0);
	}
	
	public void Create(double delay)
	{
		m_CharLabel = new UILabel(m_AudioManager);
		m_CharLabel.SetText("A");
		m_CharLabel.SetPosition(m_Position.I, m_Position.J);
		m_CharLabel.GetFont().SetAlignment(Font.Alignment.Center);
		m_UIManager.AddUIElement(m_CharLabel);
		
		m_UpButton = new UIButton(m_AudioManager);
		m_UpButton.SetText(" £ ");
		m_UpButton.SetPosition(m_Position.I, m_Position.J + CHAR_SPACING);
		m_UpButton.GetFont().SetAlignment(Alignment.Center);
		m_UIManager.AddUIElement(m_UpButton);
			
		m_DownButton = new UIButton(m_AudioManager);
		m_DownButton.SetText(" $ ");
		m_DownButton.SetPosition(m_Position.I, m_Position.J - CHAR_SPACING);
		m_DownButton.GetFont().SetAlignment(Alignment.Center);
		m_UIManager.AddUIElement(m_DownButton);

		m_CharLabel.Hide();
		m_CharLabel.Show(delay);

		m_UpButton.Hide();
		m_UpButton.Show(delay);

		m_DownButton.Hide();
		m_DownButton.Show(delay);
		
		m_CharIndex = 0;
		m_Char = m_Chars.get(m_CharIndex);
	}
	
	public void Update(double deltaTime)
	{
		if(CollisionDetection.UIElementInteraction(m_ControlScheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_UpButton))
		{
			m_UpButton.Pressed();
			m_ControlScheme.ResetTouchCoordinates();
			Up();
			UpdateChar();
			
			return;
		}
		
		if(CollisionDetection.UIElementInteraction(m_ControlScheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_DownButton))
		{
			m_DownButton.Pressed();
			m_ControlScheme.ResetTouchCoordinates();
			Down();
			UpdateChar();
			
			return;
		}
	}
	
	private void Up()
	{
		m_CharIndex ++;
		
		if(m_CharIndex >= MAX_INDEX)
			m_CharIndex = 0;
	}
	
	private void Down()
	{
		m_CharIndex --;
		
		if(m_CharIndex <= 0)
			m_CharIndex = MAX_INDEX - 1;
	}
	
	private void UpdateChar()
	{
		m_Char = m_Chars.get(m_CharIndex);
		m_CharLabel.SetText(Character.toString(m_Char));
	}
	
	public void Remove()
	{
		m_UIManager.RemoveUIElement(m_CharLabel);
		m_CharLabel = null;
		
		m_UIManager.RemoveUIElement(m_UpButton);
		m_UpButton = null;
		
		m_UIManager.RemoveUIElement(m_DownButton);
		m_DownButton = null;
	}
	
	public String GetChar()
	{
		return Character.toString(m_Char);
	}
}
