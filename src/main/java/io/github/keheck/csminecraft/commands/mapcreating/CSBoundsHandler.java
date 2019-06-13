package io.github.keheck.csminecraft.commands.mapcreating;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.objectholder.BoundaryIndicators;
import io.github.keheck.csminecraft.repeats.RepeatingBoundaryMarker;
import io.github.keheck.csminecraft.util.logic.Numeric;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSBoundsHandler extends CommandHandlerBase
{
    public CSBoundsHandler(JavaPlugin plugin) { super(plugin); }

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
        if(!(sender instanceof Player))
        {
            System.out.println(ChatColor.RED + "Sender has to be a player!");
            return true;
        }

        if(args.length != 6)
            return false;

        for(String str : args)
        {
            try
            {
                int i = Integer.parseInt(str);
            }catch(NumberFormatException e)
            {
                sender.sendMessage(ChatColor.RED + "Arguments have to be a number!");
                return false;
            }
        }

        for(int i = 0; i < args.length; i++)
        {
            int j = Integer.parseInt(args[i]);

            if(i == 1 || i == 4)
            {
                if(!Numeric.between(0, j, 256))
                {
                    sender.sendMessage(ChatColor.RED + "Y Coordinates have to be between 1 and 255!");
                    return false;
                }
            }
            else
            {
                if(!Numeric.between(-29999985, j, 29999985))
                {
                    sender.sendMessage(ChatColor.RED + "X/Z Coordinates have to be between -29999984 and 29999984!");
                    return false;
                }
            }
        }

        int[] bounds = new int[6];

        for(int i = 0; i < 6; i++)
        {
            bounds[i] = Integer.parseInt(args[i]);
        }

        if(BoundaryIndicators.MapBounds.isCancelled())
            BoundaryIndicators.MapBounds = new RepeatingBoundaryMarker(plugin, Particle.VILLAGER_HAPPY, ((Player) sender).getWorld(), bounds);
        else
            sender.sendMessage(ChatColor.RED + "Boundary has to be reset first!");

        sender.sendMessage(ChatColor.GREEN + "Boundary created!");
        return true;
    }
}
