package io.github.keheck.csminecraft.commands.games;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSForceStartHandler extends CommandHandlerBase
{
    public CSForceStartHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length > 0)
            return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.wrong_sender"));
            return true;
        }

        Map mapToStart = Map.getMapForPlayer((Player)sender);

        if(mapToStart != null)
        {
            mapToStart.start();
        }
        else
        {
            sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.not_joined"));
        }

        return true;
    }
}
