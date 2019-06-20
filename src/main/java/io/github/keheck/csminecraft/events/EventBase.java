package io.github.keheck.csminecraft.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventBase extends Event
{
    private static final HandlerList list = new HandlerList();

    @Override
    public HandlerList getHandlers() { return list; }

    public static HandlerList getHandlerList() { return list; }
}
