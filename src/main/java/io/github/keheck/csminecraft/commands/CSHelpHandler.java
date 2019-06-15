package io.github.keheck.csminecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CSHelpHandler extends CommandHandlerBase
{
    public CSHelpHandler(JavaPlugin plugin) { super(plugin); }

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
        if(args.length != 0)
            return false;

        sender.sendMessage(ChatColor.AQUA + "==========CSMinecraft Commands==========");
        sender.sendMessage(ChatColor.AQUA + "/cshelp: " + ChatColor.GREEN + plugin.getCommand("cshelp").getDescription());
        sender.sendMessage(ChatColor.AQUA + "/cslist: " + ChatColor.GREEN + plugin.getCommand("cslist").getDescription());
        sender.sendMessage(ChatColor.AQUA + "/csbounds <x1> <y1> <z1> <x2> <y2> <z2>: " + ChatColor.GREEN + plugin.getCommand("csbounds").getDescription());
        sender.sendMessage(ChatColor.AQUA + "/csspawn [t|ct] <x1> <z1> <x2> <z2> <y>: " + ChatColor.GREEN + plugin.getCommand("csspawn").getDescription());
        sender.sendMessage(ChatColor.AQUA + "/csbomb [a|b] <x1> <z1> <x2> <z2> <y>: " + ChatColor.GREEN + plugin.getCommand("csbomb").getDescription());
        sender.sendMessage(ChatColor.AQUA + "/csfinish <name>: " + ChatColor.GREEN + plugin.getCommand("csfinish").getDescription());

        return true;
    }
}
