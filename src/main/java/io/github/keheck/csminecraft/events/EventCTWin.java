package io.github.keheck.csminecraft.events;

import io.github.keheck.csminecraft.Map;
import org.bukkit.plugin.java.JavaPlugin;

public class EventCTWin extends EventBase
{
    private JavaPlugin plugin;
    private Map map;

    public EventCTWin(JavaPlugin plugin, Map map)
    {
        this.plugin = plugin;
        this.map = map;
    }

    public JavaPlugin getPlugin() { return plugin; }

    public Map getMap() { return map; }
}
