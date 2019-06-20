package io.github.keheck.csminecraft.commands.games;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSForceStopHandler extends CommandHandlerBase
{
    public CSForceStopHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            Map map = Map.getMapForPlayer((Player)sender);

            if(map != null)
            {
                map.stopGame();
            }
            else
            {
                sender.sendMessage(ChatColor.RED + LangLoader.get("command.error.not_joined"));
            }
        }

        return true;
    }
}
