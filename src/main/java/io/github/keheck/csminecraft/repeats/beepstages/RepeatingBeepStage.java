package io.github.keheck.csminecraft.repeats.beepstages;

import io.github.keheck.csminecraft.repeats.RepeatingBase;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingBeepStage extends RepeatingBase
{
    RepeatingBeepStage(JavaPlugin plugin, long delay, long period, long terminate)
    {
        super(plugin, delay, period, terminate);
    }
}
