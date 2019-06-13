package io.github.keheck.csminecraft.commands.joining;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSJoinHandler extends CommandHandlerBase
{
    public CSJoinHandler(JavaPlugin plugin) { super(plugin); }

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
        if(args.length != 1)
            return false;

        if(!(sender instanceof Player))
        {
            CSMinecraft.LOGGER.warning("Command sender was not a player!");
            return true;
        }

        Map mapToJoin = CSMinecraft.MAPS.get(args[0]);
        boolean notJoined = Map.getMapForPlayer((Player) sender) == null;

        if(mapToJoin != null)
        {
            if(notJoined)
            {
                if(mapToJoin.join((Player)sender))
                {
                    mapToJoin.teleportPlayer();
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "The map you tried to join was full!");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "You already joined a game!");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "The map you tried to join was not available");
        }

        return true;
    }
}
