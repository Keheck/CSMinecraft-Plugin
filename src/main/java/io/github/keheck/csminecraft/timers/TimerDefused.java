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

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        map.getWorld().getBlockAt(map.getBombLoc()).setType(Material.AIR);
        plugin.getServer().getPluginManager().callEvent(new EventCTWin(plugin, map));
    }
}
