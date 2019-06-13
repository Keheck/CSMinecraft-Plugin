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
    public void run() { plugin.getServer().getPluginManager().callEvent(new EventCTWin(plugin, map)); }
}
