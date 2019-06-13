package io.github.keheck.csminecraft.repeats.beepstages;

import io.github.keheck.csminecraft.Map;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

public class RepeatingBeepStage1 extends RepeatingBeepStage
{
    private Map map;

    public RepeatingBeepStage1(JavaPlugin plugin, Map map)
    {
        super(plugin, 0, 20, 20);
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
        if(time < counter)
            map.getBombLoc().getWorld().playSound(map.getBombLoc(), Sound.BLOCK_NOTE_SNARE, 2, 2);
        else
        {
            map.beepStage = new RepeatingBeepStage2(plugin, map);
            this.cancel();
        }

        time++;
    }
}
