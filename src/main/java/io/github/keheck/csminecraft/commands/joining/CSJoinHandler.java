package io.github.keheck.csminecraft.commands.joining;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSJoinHandler extends CommandHandlerBase
{
    public CSJoinHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length != 1)
            return false;

        if(sender instanceof Player)
        {
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
                        sender.sendMessage(ChatColor.RED + LangLoader.get("map.join.full"));
                    }
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + LangLoader.get("map.join.already"));
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + LangLoader.get("map.join.not_available"));
            }
        }

        return true;
    }
}
