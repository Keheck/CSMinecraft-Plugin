package io.github.keheck.csminecraft.repeats.beepstages;

import io.github.keheck.csminecraft.Map;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

public class RepeatingBeepStage3 extends RepeatingBeepStage
{
    private Map map;

    RepeatingBeepStage3(JavaPlugin plugin, Map map)
    {
        super(plugin, 0, 5, 20);
        this.map = map;
    }

    @Override
    public void run()
    {
        if(time < counter)
            map.getBombLoc().getWorld().playSound(map.getBombLoc(), Sound.BLOCK_NOTE_SNARE, 2, 2);
        else
        {
            map.beepStage = new RepeatingBeepStage4(plugin, map);
            this.cancel();
        }

        time++;
    }
}
