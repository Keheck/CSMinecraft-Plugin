package io.github.keheck.csminecraft.util;

import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SameTeamKiller
{
    public static void killedSamePlayer(Map map, Player killer, Player died)
    {
        ArrayList<Player> team;
        boolean isCt;

        if(map.getCts().contains(died))
        {
            team = map.getCts();
            isCt = true;
        }
        else
        {
            team = map.getCts();
            isCt = false;
        }

        if(isCt)
        {
            for(Player p : team)
            {
                p.sendMessage(Map.getCtColor() + LangLoader.get("map.game.player.mate_killed", killer.getPlayerListName(), died.getPlayerListName()));
            }
        }
        else
        {
            for(Player p : team)
            {
                p.sendMessage(Map.getTColor() + LangLoader.get("map.game.player.mate_killed", killer.getPlayerListName(), died.getPlayerListName()));
            }
        }
    }
}
