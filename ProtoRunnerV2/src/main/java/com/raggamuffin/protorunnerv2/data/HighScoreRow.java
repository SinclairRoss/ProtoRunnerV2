package com.raggamuffin.protorunnerv2.data;

public class HighScoreRow extends TableRow
{
	private String m_Name;
	private int m_Score;
	private float m_PlayTime;
	private int m_NumReboots;
	private int m_ShotsFired;
	private int m_ShotsLanded;
	private float m_WingmanALife;
	private float m_WingmanBLife;
	
	public HighScoreRow(int id, String name, int score, float playTime, int reboots, int shotsFired, int shotsLanded, float aLife, float bLife)
	{
		super(id);
		
		m_Name = name;
		m_Score = score;
		m_PlayTime = playTime;
		m_NumReboots = reboots;
		m_ShotsFired = shotsFired;
		m_ShotsLanded = shotsLanded;
		m_WingmanALife = aLife;
		m_WingmanBLife = bLife;
	}
	
	public String GetName()
	{
		return m_Name;
	}
	
	public int GetScore()
	{
		return m_Score;
	}
	
	public float GetPlayTime()
	{
		return m_PlayTime;
	}

	public int GetNumReboots()
	{
		return m_NumReboots;
	}
	
	public int GetShotsFired()
	{
		return m_ShotsFired;
	}
	
	public int GetShotsLanded()
	{
		return m_ShotsLanded;
	}
	
	public float GetWingmanALife()
	{
		return m_WingmanALife;
	}
	
	public float GetWingmanBLife()
	{
		return m_WingmanBLife;
	}
	
	@Override
	public String GenerateValuesString() 
	{
		return "'" + m_Name + "'" + ", " + m_Score + ", " + m_PlayTime + ", " + m_NumReboots + ", " +
				m_ShotsFired + ", " + m_ShotsLanded + ", " + m_WingmanALife + ", " + m_WingmanBLife;
	}	
}














