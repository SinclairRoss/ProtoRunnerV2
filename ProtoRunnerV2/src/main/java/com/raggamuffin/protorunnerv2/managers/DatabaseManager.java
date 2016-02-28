package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.data.AutoSignInRow;
import com.raggamuffin.protorunnerv2.data.AutoSignInTable;
import com.raggamuffin.protorunnerv2.data.OfflineHighScoreRow;
import com.raggamuffin.protorunnerv2.data.OfflineHighScoreTable;
import com.raggamuffin.protorunnerv2.data.TableRow;
import com.raggamuffin.protorunnerv2.data.TutorialOfferedRow;
import com.raggamuffin.protorunnerv2.data.TutorialOfferedTable;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseManager 
{
	private final String DATABASE_NAME = "protodb";
	private GameLogic m_Game;

	private TutorialOfferedTable m_TutorialOfferedTable;
    private OfflineHighScoreTable m_OfflineHighScoreTable;
    private AutoSignInTable m_AutoSignInTable;

	public DatabaseManager(GameLogic game)
	{
		m_Game = game;

        SQLiteDatabase db = m_Game.GetContext().openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        m_TutorialOfferedTable  = new TutorialOfferedTable(db);
        m_OfflineHighScoreTable = new OfflineHighScoreTable(db);
        m_AutoSignInTable       = new AutoSignInTable(db);
    }
	
	public boolean HasTheTutorialBeenOffered()
    {
        return m_TutorialOfferedTable.Read().size() > 0;
    }

    public void TutorialOffered()
    {
        if(HasTheTutorialBeenOffered())
            return;

        m_TutorialOfferedTable.Insert(new TutorialOfferedRow(0, true));
    }

    public int GetLocallySavedHighScore()
    {
        ArrayList<TableRow> tableRows = m_OfflineHighScoreTable.Read();

        if(tableRows.size() == 0)
            return 0;

        OfflineHighScoreRow row = (OfflineHighScoreRow)tableRows.get(0);
        return row.GetScore();
    }

    public void SaveHighScoreLocally(int score, int time)
    {
        int highScore = GetLocallySavedHighScore();
        if(score > highScore)
        {
            highScore = score;
        }

        int longestTimePlayed = GetLocallySavedHighestPlayTime();
        if(time > longestTimePlayed)
        {
            longestTimePlayed = time;
        }

        m_OfflineHighScoreTable.ClearTable();
        m_OfflineHighScoreTable.Insert(new OfflineHighScoreRow(0, highScore, longestTimePlayed));
    }

    public int GetLocallySavedHighestPlayTime()
    {
        ArrayList<TableRow> tableRows = m_OfflineHighScoreTable.Read();

        if(tableRows.size() == 0)
            return 0;

        OfflineHighScoreRow row = (OfflineHighScoreRow)tableRows.get(0);
        return row.GetHighestSurvivalTime();
    }

    public boolean ShouldAutoSignIn()
    {
        ArrayList<TableRow> tableRows = m_AutoSignInTable.Read();

        if(tableRows.size() == 0)
            return false;

        AutoSignInRow row = (AutoSignInRow)tableRows.get(0);
        return row.GetShouldAutoSignIn();
    }

    public void SetAutoSignIn(boolean autoSignIn)
    {
        m_AutoSignInTable.ClearTable();
        m_AutoSignInTable.Insert(new AutoSignInRow(0, autoSignIn));
    }
}
