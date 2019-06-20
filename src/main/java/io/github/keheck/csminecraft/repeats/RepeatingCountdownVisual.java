package io.github.keheck.csminecraft.repeats;

import io.github.keheck.csminecraft.Map;
import org.bukkit.plugin.java.JavaPlugin;

public class RepeatingCountdownVisual extends RepeatingBase
{
    private double totalTime;
    private double time;
    private Map map;

    public RepeatingCountdownVisual(JavaPlugin plugin, Map map, int time)
    {
        super(plugin, 0, 1, time);
        this.map = map;
        this.totalTime = time;
        this.time = time;
    }

    @Override
    public void run()
    {
        map.getTimer().setProgress(time/totalTime);
        time--;

        if(time <= 0)
            this.cancel();
    }
}
