package io.github.keheck.csminecraft.events;

import io.github.keheck.csminecraft.Map;

public class EventGameStart extends EventBase
{
    private Map map;

    public EventGameStart(Map map) { this.map = map; }

    public Map getMap() { return map; }
}
