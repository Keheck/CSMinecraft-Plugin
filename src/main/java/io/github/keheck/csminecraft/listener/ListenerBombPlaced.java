package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
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
                if(map.getPlanter() == null)
                {
                    map.setPendingLoc(event.getBlock().getLocation());
                    map.setPlanter(placer);

                    for(Player player : map.getTs())
                        player.sendMessage(Map.getTColor() + "Ich platziere die Bombe!");
                }
                else
                {
                    map.setPendingLoc(null);
                    map.setPlanter(null);
                }
            }

            event.setCancelled(true);
        }
    }
}
