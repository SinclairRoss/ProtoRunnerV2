package com.raggamuffin.protorunnerv2.ai;

public class AIPersonalityAttributes
{
    private double m_SelfWorth;
    private double m_FearOfEnemy;
    private double m_ConfidenceInTeam;
    private double m_HitPanicRate;
    public double m_PanicRecoveryRate;

    public AIPersonalityAttributes(double selfWorth, double fear, double confidence, double panic, double panicRecovery)
    {
        m_SelfWorth = selfWorth;
        m_FearOfEnemy = fear;
        m_ConfidenceInTeam = confidence;
        m_HitPanicRate = panic;
        m_PanicRecoveryRate = panicRecovery;
    }

    public double GetSelfWorth()
    {
        return m_SelfWorth;
    }

    public double GetFearOfEnemy()
    {
        return m_FearOfEnemy;
    }

    public double GetConfidenceInTeam()
    {
        return m_ConfidenceInTeam;
    }

    public double GetHitPanicRate()
    {
        return m_HitPanicRate;
    }

    public double GetPanicRecoveryRate()
    {
        return m_PanicRecoveryRate;
    }
}
