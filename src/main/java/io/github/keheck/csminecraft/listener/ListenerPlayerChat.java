package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ListenerPlayerChat implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void playerChat(AsyncPlayerChatEvent event)
    {
        if(CSMinecraft.PLAYERS_IN_GAME.contains(event.getPlayer()))
        {
            Player player = event.getPlayer();
            Map map = Map.getMapForPlayer(player);
            Set<Player> recipients = event.getRecipients();
            recipients.clear();

            if(map.isCT(player))
            {
                recipients.addAll(map.getCts());
                event.setFormat(Map.getCtColor() + "%s: " + ChatColor.RESET.toString() + "%s");
            }
            else
            {
                recipients.addAll(map.getTs());
                event.setFormat(Map.getTColor() + "%s: " + ChatColor.RESET.toString() + "%s");
            }
        }
    }
}
