package io.github.keheck.csminecraft.repeats;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class RepeatingBase extends BukkitRunnable
{
    protected JavaPlugin plugin;
    protected long counter;
    protected long time = 0;

    protected RepeatingBase(JavaPlugin plugin, long delay, long period, long terminate)
    {
        this.runTaskTimer(plugin, delay, period);
        this.plugin = plugin;
        this.counter = terminate;
    }
}
