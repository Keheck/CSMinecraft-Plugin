package io.github.keheck.csminecraft.repeats.beepstages;

import io.github.keheck.csminecraft.Map;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

public class RepeatingBeepStage4 extends RepeatingBeepStage
{
    private Map map;

    RepeatingBeepStage4(JavaPlugin plugin, Map map)
    {
        super(plugin, 0, 2, 50);
        this.map = map;
    }

    @Override
    public void run()
    {
        if(time < counter)
            map.getBombLoc().getWorld().playSound(map.getBombLoc(), Sound.BLOCK_NOTE_SNARE, 2, 2);
        else
        {
            this.cancel();
            map.beepStage.cancel();
            map.beepStage = null;
        }

        time++;
    }
}
