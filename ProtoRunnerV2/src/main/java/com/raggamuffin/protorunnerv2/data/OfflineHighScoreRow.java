package com.raggamuffin.protorunnerv2.data;

public class OfflineHighScoreRow extends TableRow
{
    private int m_Score;

    public OfflineHighScoreRow(int id, int score)
    {
        super(id);

        m_Score = score;
    }

    public int GetScore()
    {
        return m_Score;
    }

    @Override
    public String GenerateValuesString()
    {
        return Integer.toString(m_Score);
    }
}
