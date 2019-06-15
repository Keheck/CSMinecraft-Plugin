package io.github.keheck.csminecraft.timers;

import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.Constants;
import org.bukkit.plugin.java.JavaPlugin;

public class TimerDisableShop extends TimerBase
{
    private Map map;

    public TimerDisableShop(JavaPlugin plugin, Map map)
    {
        super(plugin, Constants.SHOP_PERSISTANCE);
        this.map = map;
    }

    @Override
    public void run() { map.setCanBuy(false); }
}
