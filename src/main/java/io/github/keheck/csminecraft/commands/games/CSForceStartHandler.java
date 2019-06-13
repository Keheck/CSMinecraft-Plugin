package io.github.keheck.csminecraft.commands.games;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSForceStartHandler extends CommandHandlerBase
{
    public CSForceStartHandler(JavaPlugin plugin)
    {
        super(plugin);
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length > 0)
            return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("Sender has to be a player");
            return true;
        }

        Map mapToStart = Map.getMapForPlayer((Player)sender);

        if(mapToStart != null)
        {
            mapToStart.start();
        }
        else
        {
            sender.sendMessage("You didn't join a game!");
        }

        return true;
    }
}
