package io.github.keheck.csminecraft.timers;

import io.github.keheck.csminecraft.events.EventCTWin;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import org.bukkit.plugin.java.JavaPlugin;

public class TimerRoundCountdown extends TimerBase
{
    private Map map;

    public TimerRoundCountdown(JavaPlugin plugin, Map map)
    {
        super(plugin, Constants.ROUND_LENGTH);
        this.map = map;
    }

    @Override
    public void run() { plugin.getServer().getPluginManager().callEvent(new EventCTWin(plugin, map)); }
}
