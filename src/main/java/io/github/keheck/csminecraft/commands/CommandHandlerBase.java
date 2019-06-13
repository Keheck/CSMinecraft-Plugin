package io.github.keheck.csminecraft.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandHandlerBase implements CommandExecutor
{
    protected JavaPlugin plugin;

    public CommandHandlerBase(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
}
