package io.github.keheck.csminecraft.timers;

import io.github.keheck.csminecraft.events.EventBombExplode;
import io.github.keheck.csminecraft.events.EventTWin;
import io.github.keheck.csminecraft.Map;
import org.bukkit.plugin.java.JavaPlugin;

public class TimerBombExplode extends TimerBase
{
    private Map map;

    public TimerBombExplode(JavaPlugin plugin, Map map)
    {
        super(plugin, 800);
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
        plugin.getServer().getPluginManager().callEvent(new EventBombExplode(plugin, map));
        plugin.getServer().getPluginManager().callEvent(new EventTWin(plugin, map));
    }
}
