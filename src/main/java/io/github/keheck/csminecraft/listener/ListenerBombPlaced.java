package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class ListenerBombPlaced implements Listener
{
    @EventHandler
    public void tntPlaced(BlockPlaceEvent event)
    {
        Player placer = event.getPlayer();

        if(CSMinecraft.PLAYERS_IN_GAME.contains(placer) && event.getBlockPlaced().getType() == Material.TNT)
        {
            Map map = Map.getMapForPlayer(placer);

            ItemStack item = event.getItemInHand();
            boolean isBomb = item.getItemMeta().getDisplayName().equals(Map.getTColor() + "Bombe");

            if(map.isValidBombPlacement(event.getBlock().getLocation()) && isBomb)
            {
                map.setupBombPlaced();
                map.addMoneyToPlayer(placer, Constants.MONEY_PLANT, "Bombe platziert");
                map.setBombLoc(event.getBlock().getLocation());
                map.getVisual().cancel();
                map.getCountdown().cancel();
            }
            else
            {
                event.setCancelled(true);
            }
        }
    }
}
