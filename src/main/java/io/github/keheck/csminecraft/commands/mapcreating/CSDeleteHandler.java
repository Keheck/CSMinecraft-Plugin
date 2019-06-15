package io.github.keheck.csminecraft.commands.mapcreating;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.util.filehandling.MapsFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CSDeleteHandler extends CommandHandlerBase
{
    public CSDeleteHandler(JavaPlugin plugin)
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
        if(args.length != 1)
            return false;

        if(MapsFile.contains(args[0]))
        {
            MapsFile.deleteMap(args[0]);
            sender.sendMessage("");
        }
        else
            sender.sendMessage(ChatColor.RED + "Couldn't find the map!");

        return true;
    }
}
