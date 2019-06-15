package io.github.keheck.csminecraft.commands.games;

import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import io.github.keheck.csminecraft.Map;
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
            sender.sendMessage("Befehls sender muss ein Spieler sein");
            return true;
        }

        Map mapToStart = Map.getMapForPlayer((Player)sender);

        if(mapToStart != null)
        {
            mapToStart.start();
        }
        else
        {
            sender.sendMessage("Du bist keinem Spiel beigetreten!");
        }

        return true;
    }
}
