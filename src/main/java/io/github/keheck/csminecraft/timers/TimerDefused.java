package io.github.keheck.csminecraft.timers;

import io.github.keheck.csminecraft.events.EventCTWin;
import io.github.keheck.csminecraft.Map;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class TimerDefused extends TimerBase
{
    private Map map;

    public TimerDefused(JavaPlugin plugin, long delay, Map map)
    {
        super(plugin, delay);
        this.map = map;
    }

    @Override
    public void run()
    {
        map.getWorld().getBlockAt(map.getBombLoc()).setType(Material.AIR);
        plugin.getServer().getPluginManager().callEvent(new EventCTWin(plugin, map));
    }
}
