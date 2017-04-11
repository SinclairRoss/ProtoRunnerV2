package com.raggamuffin.protorunnerv2.utils;

public class SpawnHelper
{
    public static Vector3 FindRandomSpawnLocation(Vector3 playerPos, Vector3 playerForward, double spawnRange)
    {
        final double spawnArc = Math.PI * 0.25;

        Vector3 spawnPoint = new Vector3(playerForward);
        spawnPoint.RotateY(MathsHelper.RandomDouble(-spawnArc, spawnArc));
        spawnPoint.Scale(spawnRange);
        spawnPoint.Add(playerPos);

        return spawnPoint;
    }

    public static Vector3 FindRandomSpawnLocation(double spawnRange)
    {
        Vector3 spawnPoint = new Vector3();
        spawnPoint.X = MathsHelper.RandomDouble(-1, 1);
        spawnPoint.Z = MathsHelper.RandomDouble(-1, 1);
        spawnPoint.Normalise();
        spawnPoint.Scale(spawnRange);

        return spawnPoint;
    }

    public static Vector3 FindSpawnForward(Vector3 position, Vector3 targetPoint)
    {
        Vector3 forward = new Vector3();
        forward.SetAsDifference(position, targetPoint);
        forward.Normalise();

        return forward;
    }
}
