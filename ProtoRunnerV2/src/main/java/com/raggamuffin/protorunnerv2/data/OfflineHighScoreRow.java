package com.raggamuffin.protorunnerv2.data;

public class OfflineHighScoreRow extends TableRow
{
    private int m_Score;
    private int m_HighSurvivalTime;

    public OfflineHighScoreRow(int id, int score, int highSurvivalTime)
    {
        super(id);

        m_Score = score;
        m_HighSurvivalTime = highSurvivalTime;
    }

    public int GetScore()
    {
        return m_Score;
    }

    public int GetHighestSurvivalTime()
    {
        return m_HighSurvivalTime;
    }

    @Override
    public String GenerateValuesString()
    {
        String valueString = Integer.toString(m_Score) + ", " +
                             Integer.toString(m_HighSurvivalTime);

        return valueString;
    }
}
