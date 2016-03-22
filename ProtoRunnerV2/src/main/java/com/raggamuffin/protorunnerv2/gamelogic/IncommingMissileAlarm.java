package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.ui.InGameMessageHandler;
import com.raggamuffin.protorunnerv2.ui.MessageOrientation;
import com.raggamuffin.protorunnerv2.ui.UIScreens;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.ProjectileType;
import com.raggamuffin.protorunnerv2.weapons.Projectile_Missile;

public class IncommingMissileAlarm
{
    private InGameMessageHandler m_MessageHandler;
    private BulletManager m_BulletManager;
    private VehicleManager m_VehicleManager;

    public IncommingMissileAlarm(GameLogic game)
    {
        m_MessageHandler = game.GetUIManager().GetScreen(UIScreens.Play).GetMessageHandler();
        m_BulletManager = game.GetBulletManager();
        m_VehicleManager = game.GetVehicleManager();
    }

    public void Update()
    {
        //double distanceToClosestMissile = CalculateDistanceToClosestMissile();

  //      m_MessageHandler.DisplayMessage("Incomming Missile", MessageOrientation.Center, 1.0, 1, 5, 0);
    }

    private double CalculateDistanceToClosestMissile()
    {
        Vehicle player = m_VehicleManager.GetPlayer();
        double distanceToClosestMissileSqr = Double.MAX_VALUE;

        if (player != null)
        {
            Vector3 playerPos = player.GetPosition();

            for (Projectile bullet : m_BulletManager.GetActiveBullets())
            {
                if (bullet.GetProjectileType() == ProjectileType.Missile)
                {
                    Vector3 bulletPos = bullet.GetPosition();
                    double i = bulletPos.I - playerPos.I;
                    double j = bulletPos.J - playerPos.J;
                    double k = bulletPos.K - playerPos.K;

                    double distanceToMissileSqr = i*i + j*j + k*k;

                    if(distanceToMissileSqr < distanceToClosestMissileSqr)
                    {
                        distanceToClosestMissileSqr = distanceToMissileSqr;
                    }
                }
            }

            return distanceToClosestMissileSqr;
        }
        else
        {
            return -1;
        }
    }
}