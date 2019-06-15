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

public class CSBoundsHandler extends CommandHandlerBase
{
    public CSBoundsHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            System.out.println(ChatColor.RED + "Sender muss ein Spieler sein!");
            return true;
        }

        if(args.length != 6)
            return false;

        for(String str : args)
        {
            try
            {
                Integer.parseInt(str);
            }catch(NumberFormatException e)
            {
                sender.sendMessage(ChatColor.RED + "Argumente müssen Zahlen sein!");
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
                    sender.sendMessage(ChatColor.RED + "Y Koordinate muss zwichen 1 und 255 liegen!");
                    return false;
                }
            }
            else
            {
                if(!Numeric.between(-29999985, j, 29999985))
                {
                    sender.sendMessage(ChatColor.RED + "X/Z Koordinaten müssen zwichen -29999984 und 29999984 liegen!");
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
            sender.sendMessage(ChatColor.RED + "Map Bereich muss erst zurückgesetzt werden!");

        sender.sendMessage(ChatColor.GREEN + "Map Bereich erstellt!");
        return true;
    }
}
