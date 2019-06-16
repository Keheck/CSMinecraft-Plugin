package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class ListenerTntArrow implements Listener
{
    @EventHandler
    public void onShot(ProjectileLaunchEvent event)
    {
        if(event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof Arrow)
        {
            Player shooter = (Player)event.getEntity().getShooter();
            ItemStack tnt = shooter.getInventory().getItem(2);

            if(CSMinecraft.PLAYERS_IN_GAME.contains(shooter) && tnt != null && tnt.getType() == Material.TNT)
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
        Projectile pro = event.getEntity();
        LivingEntity ent = (LivingEntity) pro.getShooter();

        if (pro instanceof Arrow && ent instanceof Player)
        {
            Arrow arrow = (Arrow)event.getEntity();
            Player player = (Player)arrow.getShooter();

            if(Map.getMapForPlayer(player).getTntShooter().contains(player))
            {
                Location aloc = arrow.getLocation();

                aloc.getWorld().spawn(arrow.getLocation(), TNTPrimed.class).setFuseTicks(1);
                arrow.remove();
                Map.getMapForPlayer(player).getTntShooter().remove(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ensureDamage(ExplosionPrimeEvent event)
    {
        System.out.println("hello");

        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();
            if(CSMinecraft.PLAYERS_IN_GAME.contains(player))
                event.setCancelled(false);
        }
    }
}
