package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import io.github.keheck.csminecraft.util.SameTeamKiller;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

public class ListenerPlayerKilled implements Listener
{
    @EventHandler
    public void onKill(PlayerDeathEvent event)
    {
        Player died = event.getEntity();
        Player killer = died.getKiller();

        if(CSMinecraft.PLAYERS_IN_GAME.contains(died) && CSMinecraft.PLAYERS_IN_GAME.contains(killer))
        {
            Map map = Map.getMapForPlayer(died);
            Map kMap = Map.getMapForPlayer(killer);

            if(map == kMap)
            {
                if(map.isSameTeam(died, killer))
                {
                    map.addMoneyToPlayer(killer, Constants.MONEY_TEAMKILL, LangLoader.get("map.game.money.killed.mate"));
                    SameTeamKiller.killedSamePlayer(map, killer, died);
                }
                else
                {
                    map.addMoneyToPlayer(killer, Constants.MONEY_KILL, LangLoader.get("map.game.money.killed.mate"));
                }

                died.setHealth(20);

                if(map.isWarmup())
                {
                    map.resetPlayer(died);
                }
                else
                {
                    died.setGameMode(GameMode.SPECTATOR);
                    died.setVelocity(new Vector());

                    if(!map.isWarmup())
                        map.onPlayerKill(died, killer);
                }
            }
        }
        else if(CSMinecraft.PLAYERS_IN_GAME.contains(died) && killer == null)
        {
            died.setHealth(20);
            died.setGameMode(GameMode.SPECTATOR);
            died.setVelocity(new Vector());

            Map.getMapForPlayer(died).onPlayerKill(died, null);
        }
    }

    @EventHandler
    public void defuserDamaged(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            Player damager = (Player)event.getDamager();
            Player player = (Player)event.getEntity();

            Map map = Map.getMapForPlayer(damager);
            Map map1 = Map.getMapForPlayer(player);

            if(map == map1 && map.getDefuser() == player)
            {
                map.setDefuser(null, null);
            }
        }
    }

    @EventHandler
    public void planterDamaged(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            Player damager = (Player)event.getDamager();
            Player player = (Player)event.getEntity();

            Map map = Map.getMapForPlayer(damager);
            Map map1 = Map.getMapForPlayer(player);

            if(map == map1 && map.getPlanter() == player)
            {
                map.setPlanter(null);
            }
        }
    }
}
