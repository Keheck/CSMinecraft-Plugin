package io.github.keheck.csminecraft.timers;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class TimerBase extends BukkitRunnable
{
    JavaPlugin plugin;

    public TimerBase(JavaPlugin plugin, long delay)
    {
        runTaskLater(plugin, delay);
        this.plugin = plugin;
    }
}
