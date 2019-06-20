package io.github.keheck.csminecraft.timers;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

abstract class TimerBase extends BukkitRunnable
{
    JavaPlugin plugin;

    TimerBase(JavaPlugin plugin, long delay)
    {
        runTaskLater(plugin, delay);
        this.plugin = plugin;
    }
}
