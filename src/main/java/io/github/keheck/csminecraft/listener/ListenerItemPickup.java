package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

public class ListenerItemPickup implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void tntPicked(EntityPickupItemEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();

            if (CSMinecraft.PLAYERS_IN_GAME.contains(player))
            {
                Item item = event.getItem();
                Map map = Map.getMapForPlayer(player);

                if(map.isCT(player) && Map.getItemType(item.getItemStack()) == 2)
                    event.setCancelled(true);
                else if(!map.isCT(player) && Map.getItemType(item.getItemStack()) == 2)
                {
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(LangLoader.get("map.game.item.bomb.lore1"));
                    lore.add(LangLoader.get("map.game.item.bomb.lore2"));
                    lore.add(LangLoader.get("map.game.item.bomb.lore3"));
                    lore.add(LangLoader.get("map.game.item.bomb.lore4"));

                    ItemStack tnt = new ItemStack(Material.TNT);
                    ItemMeta meta = tnt.getItemMeta();
                    meta.setDisplayName(Map.getTColor() + LangLoader.get("map.game.item.bomb"));
                    meta.setLore(lore);
                    tnt.setItemMeta(meta);

                    PlayerInventory invPlayer = player.getInventory();
                    invPlayer.setItem(8, tnt);
                    item.remove();
                    float pitch = new Random().nextFloat()/3+1;
                    map.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, pitch);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void weaponPicked(EntityPickupItemEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();
            ItemStack item = player.getInventory().getItem(0);

            if(CSMinecraft.PLAYERS_IN_GAME.contains(player))
            {
                if(item != null && Map.getItemType(item) == 1)
                    event.setCancelled(true);
            }
        }
    }
}
