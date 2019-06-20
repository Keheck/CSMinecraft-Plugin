package io.github.keheck.csminecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CSHelpHandler extends CommandHandlerBase
{
    public CSHelpHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length > 1)
            return false;

        sender.sendMessage(ChatColor.AQUA + "==========CSMinecraft Commands==========");

        switch(args[0])
        {
            default:
            case "1":
                sender.sendMessage(ChatColor.AQUA + "/cshelp: " + ChatColor.GREEN + plugin.getCommand("cshelp").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/cslist: " + ChatColor.GREEN + plugin.getCommand("cslist").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csbounds <x1> <y1> <z1> <x2> <y2> <z2>: " + ChatColor.GREEN + plugin.getCommand("csbounds").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csspawn [t|ct] <x1> <z1> <x2> <z2> <y>: " + ChatColor.GREEN + plugin.getCommand("csspawn").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csbomb [a|b] <x1> <z1> <x2> <z2> <y>: " + ChatColor.GREEN + plugin.getCommand("csbomb").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csfinish <name>: " + ChatColor.GREEN + plugin.getCommand("csfinish").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csreset <name>: " + ChatColor.GREEN + plugin.getCommand("csreset").getDescription());
                break;
            case "2":
                sender.sendMessage(ChatColor.AQUA + "/csdelete <name>: " + ChatColor.GREEN + plugin.getCommand("csdelete").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csjoin <name>: " + ChatColor.GREEN + plugin.getCommand("csjoin").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csforcestart: " + ChatColor.GREEN + plugin.getCommand("csforcestart").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csforcestop: " + ChatColor.GREEN + plugin.getCommand("csforcestop").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csleave: " + ChatColor.GREEN + plugin.getCommand("csleave").getDescription());
                sender.sendMessage(ChatColor.AQUA + "/csreload: " + ChatColor.GREEN + plugin.getCommand("csreload").getDescription());
        }

        sender.sendMessage(ChatColor.YELLOW + "Legende: {}=Kann da sein, <>=Variable (braucht einen Wert), " +
                "[]=Wähle einen aufgelisteten Wert");
        return true;
    }
}
