package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.events.EventCTWin;
import io.github.keheck.csminecraft.events.EventTWin;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ListenerTeamWin implements Listener
{
    @EventHandler
    public void onCTWin(EventCTWin event)
    {
        Map map = event.getMap();

        map.addToCtScore();

        for(Player player : map.getCts())
        {
            map.addMoneyToPlayer(player, Constants.MONEY_TEAMWIN, "Runde gewonnen");
        }

        for(Player player : map.getTs())
        {
            map.addMoneyToPlayer(player, Constants.MONEY_TEAMLOSE, "Runde verloren");
        }

        map.initAfterRound();
    }

    @EventHandler
    public void onTWin(EventTWin event)
    {
        Map map = event.getMap();

        map.addToTScore();

        for(Player player : map.getTs())
        {
            map.addMoneyToPlayer(player, Constants.MONEY_TEAMWIN, "Runde gewonnen");
        }

        for(Player player : map.getCts())
        {
            map.addMoneyToPlayer(player, Constants.MONEY_TEAMLOSE, "Runde verloren");
        }

        map.initAfterRound();
    }
}
