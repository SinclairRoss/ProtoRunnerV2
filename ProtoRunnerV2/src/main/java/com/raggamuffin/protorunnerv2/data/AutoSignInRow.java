package com.raggamuffin.protorunnerv2.data;

public class AutoSignInRow extends TableRow
{
    private boolean m_AutoSignIn;

    public AutoSignInRow(int id, boolean autoSignIn)
    {
        super(id);

        m_AutoSignIn = autoSignIn;
    }

    public boolean GetShouldAutoSignIn()
    {
        return m_AutoSignIn;
    }

    @Override
    public String GenerateValuesString()
    {
        return m_AutoSignIn ? "1" : "0";
    }
}
