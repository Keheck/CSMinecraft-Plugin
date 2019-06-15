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

    @Override
    public void run()
    {
        plugin.getServer().getPluginManager().callEvent(new EventBombExplode(plugin, map));
        plugin.getServer().getPluginManager().callEvent(new EventTWin(plugin, map));
    }
}
