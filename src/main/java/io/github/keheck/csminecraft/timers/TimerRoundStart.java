package io.github.keheck.csminecraft.timers;

import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import org.bukkit.plugin.java.JavaPlugin;

public class TimerRoundStart extends TimerBase
{
    private Map map;

    public TimerRoundStart(JavaPlugin plugin, Map map)
    {
        super(plugin, Constants.ROUND_START);
        this.map = map;
    }

    @Override
    public void run()
    {
        map.startCountdown();
        map.setInRound(true);
    }
}
