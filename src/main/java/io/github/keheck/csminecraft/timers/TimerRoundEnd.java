package io.github.keheck.csminecraft.timers;

import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import org.bukkit.plugin.java.JavaPlugin;

public class TimerRoundEnd extends TimerBase
{
    private Map map;

    public TimerRoundEnd(JavaPlugin plugin, Map map)
    {
        super(plugin, Constants.ROUND_END);
        this.map = map;
    }

    @Override
    public void run() { map.setupRound(); }
}
