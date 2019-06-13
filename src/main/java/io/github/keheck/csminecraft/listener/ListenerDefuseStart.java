package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ListenerDefuseStart implements Listener
{
    @EventHandler
    public void onBombDefuse(PlayerInteractEvent event)
    {
        if(CSMinecraft.PLAYERS_IN_GAME.contains(event.getPlayer()) && event.getHand() == EquipmentSlot.HAND)
        {
            Player p = event.getPlayer();
            Map map = Map.getMapForPlayer(p);

            Block clicked = event.getClickedBlock();

            if (map != null && clicked != null && clicked.getType() == Material.TNT)
            {
                PlayerInventory inventory = p.getInventory();

                if (map.isCT(p) && !map.isDefusing() && map.getDefuser() != p)
                {
                    if (inventory.getItem(8) != null && inventory.getItem(8).getType() == Material.IRON_HOE)
                    {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Constants.DEFUSE_KIT, 10, false, false), true);
                        map.setDefuser(p, true);
                    }
                    else
                    {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Constants.DEFUSE_NOKIT, 10, false, false), true);
                        map.setDefuser(p, false);
                    }

                    for (Player ct : map.getCts())
                    {
                        ct.sendMessage(p.getDisplayName() + ": " + ChatColor.AQUA.toString() + "Ich entschärfe das TNT!");
                    }
                }
                else if(map.isDefusing() && map.getDefuser() == p)
                {
                    p.removePotionEffect(PotionEffectType.SLOW);
                    map.setDefuser(null, null);
                }
            }
        }
    }
}
