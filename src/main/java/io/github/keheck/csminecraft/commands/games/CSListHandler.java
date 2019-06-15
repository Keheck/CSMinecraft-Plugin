package io.github.keheck.csminecraft.commands.games;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.commands.CommandHandlerBase;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class CSListHandler extends CommandHandlerBase
{
    public CSListHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length > 1)
            return false;

        if(args.length == 0)
        {
            Set<String> keySet = CSMinecraft.MAPS.keySet();

            for(String key : keySet)
            {
                Map map = CSMinecraft.MAPS.get(key);
                sender.sendMessage(ChatColor.GREEN + key);
                sender.sendMessage(ChatColor.RED + "Welt: " + map.getWorld().getName());
                sender.sendMessage(ChatColor.GOLD + "Zentrum: " + map.getCenter());
            }
        }
        else
        {
            Map map = CSMinecraft.MAPS.get(args[0]);

            if(map != null)
            {
                sender.sendMessage(ChatColor.GREEN + args[0]);
                sender.sendMessage(ChatColor.RED + "Welt: " + map.getWorld().getName());
                sender.sendMessage(ChatColor.GOLD + "Zentrum: " + map.getCenter());
            }
            else
            {
                sender.sendMessage("Konnte Map nicht finden!");
            }
        }

        return true;
    }
}
