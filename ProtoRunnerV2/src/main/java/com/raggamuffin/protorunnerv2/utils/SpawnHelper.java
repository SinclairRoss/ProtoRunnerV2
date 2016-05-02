package com.raggamuffin.protorunnerv2.utils;

public class SpawnHelper
{
    public static Vector3 FindRandomSpawnLocation(Vector3 playerPos, double spawnRange)
    {
        Vector3 spawnPoint = new Vector3();
        spawnPoint.I = MathsHelper.RandomDouble(-1, 1);
        spawnPoint.K = MathsHelper.RandomDouble(-1, 1);
        spawnPoint.Normalise();
        spawnPoint.Scale(spawnRange);
        spawnPoint.Add(playerPos);

        return spawnPoint;
    }

    public static Vector3 FindRandomSpawnLocation(double spawnRange)
    {
        return FindRandomSpawnLocation(new Vector3(), spawnRange);
    }

    public static Vector3 FindSpawnForward(Vector3 position, Vector3 targetPoint)
    {
        Vector3 forward = new Vector3();
        forward.SetVectorDifference(position, targetPoint);
        forward.Normalise();

        return forward;
    }
}
