package io.github.keheck.csminecraft.commands.mapcreating;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.objectholder.BoundaryIndicators;
import io.github.keheck.csminecraft.repeats.RepeatingBoundaryMarker;
import io.github.keheck.csminecraft.util.filehandling.MapsFile;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSFinishHandler extends CommandHandlerBase
{
    public CSFinishHandler(JavaPlugin plugin) { super(plugin); }

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
            sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.wrong_sender"));
            return true;
        }

        if(args.length != 1)
            return false;

        if(BoundaryIndicators.BombB.isCancelled() || BoundaryIndicators.BombA.isCancelled() || BoundaryIndicators.MapBounds.isCancelled() ||
            BoundaryIndicators.TSpawnBounds.isCancelled() || BoundaryIndicators.CTSpawnBounds.isCancelled())
        {
            StringBuilder builder = new StringBuilder(LangLoader.get("command.error.finish.bounds"));

            if(BoundaryIndicators.BombA.isCancelled())
                builder.append(LangLoader.get("command.error.finish.missingA"));
            if(BoundaryIndicators.BombB.isCancelled())
                builder.append(LangLoader.get("command.error.finish.missingB"));
            if(BoundaryIndicators.CTSpawnBounds.isCancelled())
                builder.append(LangLoader.get("command.error.finish.missingCT"));
            if(BoundaryIndicators.TSpawnBounds.isCancelled())
                builder.append(LangLoader.get("command.error.finish.missingT"));
            if(BoundaryIndicators.MapBounds.isCancelled())
                builder.append(LangLoader.get("command.error.finish.missingBounds"));

            builder.delete(builder.length()-2, builder.length());

            sender.sendMessage(ChatColor.RED + builder.toString());
            return true;
        }
        else
        {
            if(MapsFile.contains(args[0]))
            {
                sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.finish.map_named", args[0]));
                return true;
            }

            int[] mainBounds = BoundaryIndicators.MapBounds.getBounds();
            int[] BombABounds = BoundaryIndicators.BombA.getBounds();
            int[] BombBBounds = BoundaryIndicators.BombB.getBounds();
            int[] CTSpawnBounds = BoundaryIndicators.CTSpawnBounds.getBounds();
            int[] TSpawnBounds = BoundaryIndicators.TSpawnBounds.getBounds();

            RepeatingBoundaryMarker.lowToHigh(mainBounds);
            RepeatingBoundaryMarker.lowToHigh(BombABounds);
            RepeatingBoundaryMarker.lowToHigh(BombBBounds);
            RepeatingBoundaryMarker.lowToHigh(CTSpawnBounds);
            RepeatingBoundaryMarker.lowToHigh(TSpawnBounds);

            int[][] innerBounds = new int[][]{BombABounds, BombBBounds, CTSpawnBounds, TSpawnBounds};

            for (int[] innerBound : innerBounds)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (mainBounds[j] > innerBound[j])
                    {
                        sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.finish.outside"));
                        return false;
                    }
                }

                for (int j = 3; j < 6; j++)
                {
                    if (mainBounds[j] < innerBound[j])
                    {
                        sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.finish.outside"));
                        return false;
                    }
                }
            }

            MapsFile.addMap(((Player) sender).getWorld(), args[0]);
            sender.sendMessage(ChatColor.GREEN + "Map successfuly created!");

            plugin.getCommand("csreset").execute(plugin.getServer().getConsoleSender(), "csreset", new String[]{"Bounds"});
            plugin.getCommand("csreset").execute(plugin.getServer().getConsoleSender(), "csreset", new String[]{"TSpawn"});
            plugin.getCommand("csreset").execute(plugin.getServer().getConsoleSender(), "csreset", new String[]{"CTSpawn"});
            plugin.getCommand("csreset").execute(plugin.getServer().getConsoleSender(), "csreset", new String[]{"BombA"});
            plugin.getCommand("csreset").execute(plugin.getServer().getConsoleSender(), "csreset", new String[]{"BombB"});
        }

        return true;
    }
}
