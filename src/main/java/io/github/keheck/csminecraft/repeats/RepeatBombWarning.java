package io.github.keheck.csminecraft.repeats;

import io.github.keheck.csminecraft.Map;
import org.bukkit.boss.BarColor;
import org.bukkit.plugin.java.JavaPlugin;

public class RepeatBombWarning extends RepeatingBase
{
    private Map map;
    private boolean warn;

    public RepeatBombWarning(JavaPlugin plugin, Map map)
    {
        super(plugin, 0, 20, 40);
        this.map = map;
        warn = false;
        map.setupTimer(BarColor.RED);
    }

    @Override
    public void run()
    {
        if(time < counter)
            map.getTimer().setProgress(warn ? 1 : 0);
        else
        {
            this.cancel();
            return;
        }

        warn = !warn;

        time++;
    }
}
