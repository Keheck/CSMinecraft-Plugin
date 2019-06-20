package io.github.keheck.csminecraft.commands.joining;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSLeaveHandler extends CommandHandlerBase
{
    public CSLeaveHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
            return false;

        Map map = Map.getMapForPlayer((Player)sender);

        if(map != null)
        {
            map.leave((Player)sender);
        }

        return true;
    }
}
