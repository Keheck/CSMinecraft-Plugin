package io.github.keheck.csminecraft.commands.mapcreating;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.objectholder.BoundaryIndicators;
import io.github.keheck.csminecraft.repeats.RepeatingBoundaryMarker;
import io.github.keheck.csminecraft.util.Numeric;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSSpawnHandler extends CommandHandlerBase
{
    public CSSpawnHandler(JavaPlugin plugin) { super(plugin); }

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
            System.out.println(ChatColor.RED + "Command sender has to be a player!");
            return true;
        }

        if (args.length != 6 || !args[0].equals("t") && !args[0].equals("ct"))
        {
            return false;
        }

        for(int i = 1; i < args.length; i++)
        {
            try
            {
                int j = Integer.parseInt(args[i]);
            }catch (NumberFormatException e)
            {
                sender.sendMessage(ChatColor.RED + "Last 5 arguments have to be numbers!");
                return false;
            }

            int j = Integer.parseInt(args[i]);

            switch(i)
            {
                case 1:
                case 2:
                case 3:
                case 4:
                    if(!Numeric.between(-29999985, j, 29999985))
                    {
                        sender.sendMessage(ChatColor.RED + "X/Z Coordinates have to be between -29999984 and 29999984!");
                        return true;
                    }
                    break;
                case 5:
                    if(!Numeric.between(0, j, 256))
                    {
                        sender.sendMessage(ChatColor.RED + "Y Coordinate has to be between 1 and 255!");
                        return true;
                    }
                    break;
            }
        }

        int[] bounds = new int[6];

        bounds[0] = Integer.parseInt(args[1]);
        bounds[1] = Integer.parseInt(args[5]);
        bounds[2] = Integer.parseInt(args[2]);
        bounds[3] = Integer.parseInt(args[3]);
        bounds[4] = Integer.parseInt(args[5]) + 3;
        bounds[5] = Integer.parseInt(args[4]);

        switch (args[0])
        {
            case "t":
                if(BoundaryIndicators.TSpawnBounds.isCancelled())
                    BoundaryIndicators.TSpawnBounds = new RepeatingBoundaryMarker(plugin, Particle.CRIT_MAGIC, ((Player) sender).getWorld(), bounds);
                else
                    sender.sendMessage(ChatColor.RED + "T-Spawn has to be reset first!");
                break;
            case "ct":
                if(BoundaryIndicators.CTSpawnBounds.isCancelled())
                    BoundaryIndicators.CTSpawnBounds = new RepeatingBoundaryMarker(plugin, Particle.WATER_SPLASH, ((Player) sender).getWorld(), bounds);
                else
                    sender.sendMessage(ChatColor.RED + "CT-Spawn has to be reset first!");
                break;
        }

        sender.sendMessage(ChatColor.GREEN + "Boundary created!");
        return true;
    }
}
