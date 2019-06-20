package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.events.EventBombExplode;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.ConfigValues;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ListenerBombExplode implements Listener
{
    @EventHandler
    public void explodeBomb(EventBombExplode event)
    {
        Map map = event.getMap();
        Location bombLocation = map.getBombLoc();

        double maxDamage = ConfigValues.dmgHighest;
        double falloff = ConfigValues.dmgFalloff;

        bombLocation.getWorld().playSound(bombLocation, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
        bombLocation.getWorld().getBlockAt(bombLocation).setType(Material.AIR);
        bombLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, bombLocation, 100);

        for(Player p : map.getPlayers())
        {
            double dist = bombLocation.distance(p.getLocation());
            double damage = dist*falloff+maxDamage;

            p.damage(damage);
        }
    }
}
