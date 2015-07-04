package com.raggamuffin.protorunnerv2.data;

public class TutorialOfferedRow extends TableRow
{
    private boolean m_TutorialOffered;

    public TutorialOfferedRow(int id, boolean tutorialOffered)
    {
        super(id);

        m_TutorialOffered = tutorialOffered;
    }

    public boolean GetHasPlayed()
    {
        return m_TutorialOffered;
    }

    @Override
    public String GenerateValuesString()
    {
        return m_TutorialOffered ? "1" : "0";
    }
}
