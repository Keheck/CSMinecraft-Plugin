package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import io.github.keheck.csminecraft.util.Numeric;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ListenerIllegalAction implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void illegalMovement(PlayerMoveEvent event)
    {
        if(CSMinecraft.PLAYERS_IN_GAME.contains(event.getPlayer()))
        {
            Player player = event.getPlayer();
            Map map = Map.getMapForPlayer(player);

            int[] mainBounds = map.getMainBounds();

            Location locPlayer = player.getLocation();
            Location edge1 = new Location(null, mainBounds[0], mainBounds[1], mainBounds[2]);
            Location edge2 = new Location(null, mainBounds[3], mainBounds[4], mainBounds[5]);

            double x1dist = Numeric.dist(locPlayer.getX(), edge1.getX());
            double z1dist = Numeric.dist(locPlayer.getZ(), edge1.getZ());

            double x2dist = Numeric.dist(locPlayer.getX(), edge2.getX());
            double z2dist = Numeric.dist(locPlayer.getZ(), edge2.getZ());

            if(!Numeric.between(edge1, locPlayer, edge2))
            {
                Location newLoc = locPlayer.clone();

                if(x1dist < 5)
                    newLoc.add(1.5, 0, 0);
                else if(x2dist < 5)
                    newLoc.add(-1.5, 0, 0);

                if(z1dist < 5)
                    newLoc.add(0, 0, 1.5);
                else if(z2dist < 5)
                    newLoc.add(0, 0, -1.5);

                newLoc.add(0, .2, 0);

                //Falls ein Spieler aus irgendeinem Grund unter die Erde teleportiert wird
                while(newLoc.getBlock().getType() != Material.AIR)
                    newLoc.add(0, 1, 0);

                player.teleport(newLoc);

                if(map.isCT(player))
                    player.sendMessage(Map.getCtColor() + "Nicht der richtige Zeitpunkt! Du must die Terroristen auf halten!");
                else
                    player.sendMessage(Map.getTColor() +  "Wir müssen noch den anderen zeigen, wer hier das Sagen hat!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void illegalBlockBreak(BlockBreakEvent event)
    {
        if(CSMinecraft.PLAYERS_IN_GAME.contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void illegalPlayerHurt(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            Player hurt = (Player)event.getEntity();
            Player damager = (Player)event.getDamager();

            if(CSMinecraft.PLAYERS_IN_GAME.contains(hurt) && CSMinecraft.PLAYERS_IN_GAME.contains(damager))
            {
                Map map = Map.getMapForPlayer(hurt);

                if(map.isSameTeam(hurt, damager) && map.isInRound())
                {
                    event.setDamage(event.getDamage()*Constants.TEAM_HURT_MULTIPLIER);
                }
            }
        }
    }

    @EventHandler
    public void illegalRegeneration(EntityRegainHealthEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();

            if(CSMinecraft.PLAYERS_IN_GAME.contains(player))
            {
                if(event.getRegainReason() != EntityRegainHealthEvent.RegainReason.CUSTOM)
                    event.setCancelled(true);
            }
        }
    }
}
