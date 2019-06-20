package io.github.keheck.csminecraft.commands;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.util.ConfigValues;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static io.github.keheck.csminecraft.CSMinecraft.localConfig;

public class CSReloadHandler extends CommandHandlerBase
{
    public CSReloadHandler(JavaPlugin plugin) { super(plugin); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        try
        {
            localConfig.load(CSMinecraft.extConfig);

            ConfigValues.dmgHighest = localConfig.getDouble("highestDamage", 51.3);
            ConfigValues.dmgFalloff = localConfig.getDouble("damageFalloff", -1.565217391);

            ConfigValues.hubLoc = new Location(plugin.getServer().getWorld(localConfig.getString("csHub.world", "world")),
                    localConfig.getDouble("csHub.x", 0), localConfig.getDouble("csHub.y", 0), localConfig.getDouble("csHub.z",  0));

            LangLoader.loadLang(plugin);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        return true;
    }
}
