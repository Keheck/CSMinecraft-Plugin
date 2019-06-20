package io.github.keheck.csminecraft.timers;

import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class TimerBombPlanted extends TimerBase
{
    private Map map;

    public TimerBombPlanted(JavaPlugin plugin, Map map)
    {
        super(plugin, 50);
        this.map = map;
    }

    @Override
    public void run()
    {
        map.setupBombPlaced();
        map.addMoneyToPlayer(map.getPlanter(), Constants.MONEY_PLANT, "Bombe platziert");
        map.setBombLoc(map.getPendingLoc());
        map.getVisual().cancel();
        map.getCountdown().cancel();
        map.getWorld().getBlockAt(map.getBombLoc()).setType(Material.TNT);
        map.getPlanter().getInventory().setItem(8, null);
    }
}
