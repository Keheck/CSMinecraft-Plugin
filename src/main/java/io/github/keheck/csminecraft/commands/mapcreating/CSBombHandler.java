package io.github.keheck.csminecraft.commands.mapcreating;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.objectholder.BoundaryIndicators;
import io.github.keheck.csminecraft.repeats.RepeatingBoundaryMarker;
import io.github.keheck.csminecraft.util.Numeric;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSBombHandler extends CommandHandlerBase
{
    public CSBombHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            System.out.println(ChatColor.RED + LangLoader.get("command.error.wrong_sender"));
            return true;
        }

        if (args.length != 6 || !args[0].equals("a") && !args[0].equals("b"))
        {
            return false;
        }

        for(int i = 1; i < args.length; i++)
        {
            try
            {
                Integer.parseInt(args[i]);
            }
            catch(NumberFormatException e)
            {
                sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.args.not_nums"));
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
                        sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.args.boundsxz"));
                        return false;
                    }
                    break;
                case 5:
                    if(!Numeric.between(0, j, 256))
                    {
                        sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.args.boundsy"));
                        return false;
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
            case "a":
                if(BoundaryIndicators.BombA.isCancelled())
                    BoundaryIndicators.BombA = new RepeatingBoundaryMarker(plugin, Particle.FLAME, ((Player) sender).getWorld(), bounds);
                else
                    sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.bounds.bomba"));
                break;
            case "b":
                if(BoundaryIndicators.BombB.isCancelled())
                    BoundaryIndicators.BombB = new RepeatingBoundaryMarker(plugin, Particle.SMOKE_NORMAL, ((Player) sender).getWorld(), bounds);
                else
                    sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.bounds.bombb"));
                break;
        }

        sender.sendMessage(ChatColor.GREEN + LangLoader.get("command.success.bounds"));
        return true;
    }
}
