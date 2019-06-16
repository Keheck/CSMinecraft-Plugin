package io.github.keheck.csminecraft.events;

import io.github.keheck.csminecraft.Map;

public class GameStartEvent extends EventBase
{
    private Map map;

    public GameStartEvent(Map map)
    {
        this.map = map;
    }

    public Map getMap() { return map; }
}
