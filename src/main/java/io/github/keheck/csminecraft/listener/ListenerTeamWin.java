package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.events.EventCTWin;
import io.github.keheck.csminecraft.events.EventTWin;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
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
            map.addMoneyToPlayer(player, Constants.MONEY_TEAMWIN, LangLoader.get("map.game.money.win"));
        }

        for(Player player : map.getTs())
        {
            map.addMoneyToPlayer(player, Constants.MONEY_TEAMLOSE, LangLoader.get("map.game.money.lose"));
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
            map.addMoneyToPlayer(player, Constants.MONEY_TEAMWIN, LangLoader.get("map.game.money.win"));
        }

        for(Player player : map.getCts())
        {
            map.addMoneyToPlayer(player, Constants.MONEY_TEAMLOSE, LangLoader.get("map.game.money.lose"));
        }

        map.initAfterRound();
    }
}
