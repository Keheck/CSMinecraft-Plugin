package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ListenerTntArrow implements Listener
{
    @EventHandler
    public void onShot(ProjectileLaunchEvent event)
    {
        if(event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof Arrow)
        {
            Player shooter = (Player)event.getEntity().getShooter();

            if(CSMinecraft.PLAYERS_IN_GAME.contains(shooter))
            {
                Map map = Map.getMapForPlayer(shooter);
                map.getTntShooter().add(shooter);
                shooter.getInventory().setItem(2, null);
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event)
    {
        if(event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof Arrow)
        {
            Player shooter = (Player)event.getEntity().getShooter();
            Arrow arrow = (Arrow)event.getEntity();

            if(CSMinecraft.PLAYERS_IN_GAME.contains(shooter))
            {
                Location loc = arrow.getLocation();
                Map map = Map.getMapForPlayer(shooter);
                map.getTntShooter().remove(shooter);
                map.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4, false, false);
                arrow.remove();
            }
        }
    }
}
