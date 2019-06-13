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
        map.startCountdown();
        map.setInRound(true);
    }
}
