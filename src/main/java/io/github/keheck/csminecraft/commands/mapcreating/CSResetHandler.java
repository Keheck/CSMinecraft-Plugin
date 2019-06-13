package io.github.keheck.csminecraft.commands.mapcreating;

import io.github.keheck.csminecraft.objectholder.MapCoordinateHolder;
import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.objectholder.BoundaryIndicators;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CSResetHandler extends CommandHandlerBase
{

    public CSResetHandler(JavaPlugin plugin) { super(plugin); }

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

        try
        {
            switch(args[0])
            {
                case "Bounds":
                    BoundaryIndicators.MapBounds.cancel();
                    Arrays.fill(MapCoordinateHolder.get("bounds"), 0);
                    break;
                case "TSpawn":
                    BoundaryIndicators.TSpawnBounds.cancel();
                    Arrays.fill(MapCoordinateHolder.get("TSpawn"), 0);
                    break;
                case "CTSpawn":
                    BoundaryIndicators.CTSpawnBounds.cancel();
                    Arrays.fill(MapCoordinateHolder.get("CTSpawn"), 0);
                    break;
                case "BombA":
                    BoundaryIndicators.BombA.cancel();
                    Arrays.fill(MapCoordinateHolder.get("bombA"), 0);
                    break;
                case "BombB":
                    BoundaryIndicators.BombB.cancel();
                    Arrays.fill(MapCoordinateHolder.get("bombB"), 0);
                    break;
                default:
                    return false;
            }
        }
        catch (IllegalStateException | NullPointerException ignored)
        {
            sender.sendMessage(ChatColor.RED + "Boundary was already reset!");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + args[0] + " has been reset!");
        return true;
    }
}
