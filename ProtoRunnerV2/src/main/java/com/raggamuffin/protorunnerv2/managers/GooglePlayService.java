package com.raggamuffin.protorunnerv2.managers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.ui.UIScreens;

public class GooglePlayService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private final String TAG = "GooglePlayService";
    private final int RC_SIGN_IN = 9001;

    private GameLogic m_Game;
    private Context m_Context;
    private Activity m_Activity;
    private AchievementListener m_AchievementListener;

    private Publisher m_OnConnectionPublisher;
    private Publisher m_OnDisconnectPublisher;

    private GoogleApiClient m_GoogleApiClient;
    private boolean m_ResolvingConnectionFailure;

    public GooglePlayService(GameLogic game)
    {
        Log.e(TAG, "GooglePlayService");

        m_Game = game;
        m_Activity = m_Game.GetActivity();
        m_Context = m_Game.GetContext();
        m_AchievementListener = new AchievementListener(m_Game, this);

        PubSubHub pubSub = m_Game.GetPubSubHub();
        m_OnConnectionPublisher = pubSub.CreatePublisher(PublishedTopics.GooglePlayConnected);
        m_OnDisconnectPublisher = pubSub.CreatePublisher(PublishedTopics.GooglePlayDisconnected);

        m_GoogleApiClient = new GoogleApiClient.Builder(m_Activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        m_ResolvingConnectionFailure = false;

        if (m_Game.GetDatabaseManager().ShouldAutoSignIn())
            Connect();

    }

    public void Connect()
    {
        Log.e(TAG, "Connect");

        if (m_GoogleApiClient == null)
            return;

        m_GoogleApiClient.connect();
    }

    public void Disconnect()
    {
        Log.e(TAG, "LogOut");

        if (!m_GoogleApiClient.isConnected())
            return;

        m_OnDisconnectPublisher.Publish();

        m_GoogleApiClient.disconnect();
    }

    public boolean IsConnected()
    {
        return m_GoogleApiClient != null && m_GoogleApiClient.isConnected();
    }

    public void SubmitScore(int score)
    {
        if (IsConnected())
        {
            Games.Leaderboards.submitScore(m_GoogleApiClient, m_Context.getString(R.string.leaderboard_id), score);
        }
    }

    public void SubmitTime(int time)
    {
        if (IsConnected())
        {
            Games.Leaderboards.submitScore(m_GoogleApiClient, m_Context.getString(R.string.play_time_leaderboard_id), time);
        }
    }

    public void DisplayLeaderBoard()
    {
        if (IsConnected())
            m_Activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(m_GoogleApiClient, m_Context.getString(R.string.leaderboard_id)), 1);
        else
            m_Game.GetUIManager().ShowScreen(UIScreens.NotSignedIn);
    }

    public void DisplayHighTimeLeaderBoard()
    {
        if (IsConnected())
            m_Activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(m_GoogleApiClient, m_Context.getString(R.string.play_time_leaderboard_id)), 1);
        else
            m_Game.GetUIManager().ShowScreen(UIScreens.NotSignedIn);
    }

    public void DisplayAchievements()
    {
        if (IsConnected())
            m_Activity.startActivityForResult(Games.Achievements.getAchievementsIntent(m_GoogleApiClient), 2);
        else
            m_Game.GetUIManager().ShowScreen(UIScreens.NotSignedIn);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.e(TAG, "onConnected");
        m_OnConnectionPublisher.Publish();

        SubmitScore(m_Game.GetDatabaseManager().GetLocallySavedHighScore());
        SubmitTime(m_Game.GetDatabaseManager().GetLocallySavedHighestPlayTime());
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.e(TAG, "onConnectionSuspended");
        Connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.toString());

        if (m_ResolvingConnectionFailure)
            return;

        if (!connectionResult.hasResolution())
            return;

        if (!BaseGameUtils.resolveConnectionFailure(m_Activity, m_GoogleApiClient, connectionResult, RC_SIGN_IN, "ruin"))
            m_ResolvingConnectionFailure = false;

    }

    public void UpdateAchievement(String id)
    {
        Games.Achievements.unlock(m_GoogleApiClient, id);
    }

    public void UpdateAchievement(String id, int amount)
    {
        if(IsConnected())
            Games.Achievements.increment(m_GoogleApiClient, id, amount);
    }
}
