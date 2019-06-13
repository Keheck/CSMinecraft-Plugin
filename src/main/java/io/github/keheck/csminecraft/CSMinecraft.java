package io.github.keheck.csminecraft;

import io.github.keheck.csminecraft.commands.CSHelpHandler;
import io.github.keheck.csminecraft.commands.CSReloadHandler;
import io.github.keheck.csminecraft.commands.mapcreating.*;
import io.github.keheck.csminecraft.commands.games.CSForceStartHandler;
import io.github.keheck.csminecraft.commands.games.CSForceStopHandler;
import io.github.keheck.csminecraft.commands.joining.CSJoinHandler;
import io.github.keheck.csminecraft.commands.joining.CSLeaveHandler;
import io.github.keheck.csminecraft.objectholder.BoundaryIndicators;
import io.github.keheck.csminecraft.listener.*;
import io.github.keheck.csminecraft.repeats.RepeatingBoundaryMarker;
import io.github.keheck.csminecraft.util.ConfigValues;
import io.github.keheck.csminecraft.util.loaders.MapLoader;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

public final class CSMinecraft extends JavaPlugin
{
    public static Logger LOGGER;
    public static File maps_dir;
    public static File extConfig;
    public static File indexFile;
    public static FileConfiguration localConfig;
    public static HashMap<String, Map> MAPS;
    public static ArrayList<Player> PLAYERS_IN_GAME;
    private static final int[] defaultBounds = new int[] {0, 0, 0, 0, 0, 0};

    @Override
    public void onEnable()
    {
        LOGGER = getLogger();
        LOGGER.info("Resetting any data leftover...");
        maps_dir = null;
        extConfig = null;
        indexFile = null;
        localConfig = null;
        MAPS = new HashMap<>();
        PLAYERS_IN_GAME = new ArrayList<>();

        LOGGER.info("Setting up commands...");
        getCommand("cshelp").setExecutor(new CSHelpHandler(this));
        getCommand("csbounds").setExecutor(new CSBoundsHandler(this));
        getCommand("csspawn").setExecutor(new CSSpawnHandler(this));
        getCommand("csbomb").setExecutor(new CSBombHandler(this));
        getCommand("csreset").setExecutor(new CSResetHandler(this));
        getCommand("csfinish").setExecutor(new CSFinishHandler(this));
        getCommand("csdelete").setExecutor(new CSDeleteHandler(this));
        getCommand("csjoin").setExecutor(new CSJoinHandler(this));
        getCommand("csleave").setExecutor(new CSLeaveHandler(this));
        getCommand("csforcestart").setExecutor(new CSForceStartHandler(this));
        getCommand("csforcestop").setExecutor(new CSForceStopHandler(this));
        getCommand("csreload").setExecutor(new CSReloadHandler(this));

        LOGGER.info("Setting up listeners...");
        getServer().getPluginManager().registerEvents(new ListenerTeamWin(), this);
        getServer().getPluginManager().registerEvents(new ListenerBombExplode(), this);
        getServer().getPluginManager().registerEvents(new ListenerBombPlaced(), this);
        getServer().getPluginManager().registerEvents(new ListenerDefuseStart(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerKilled(), this);
        getServer().getPluginManager().registerEvents(new ListenerIllegalAction(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new ListenerShopActions(), this);
        getServer().getPluginManager().registerEvents(new ListenerItemPickup(), this);
        getServer().getPluginManager().registerEvents(new ListenerTntArrow(), this);

        LOGGER.info("Defaulting boundaries...");
        BoundaryIndicators.MapBounds = new RepeatingBoundaryMarker(this, Particle.VILLAGER_HAPPY, getServer().getWorlds().get(0), defaultBounds);
        BoundaryIndicators.CTSpawnBounds = new RepeatingBoundaryMarker(this, Particle.WATER_SPLASH, getServer().getWorlds().get(0), defaultBounds);
        BoundaryIndicators.TSpawnBounds = new RepeatingBoundaryMarker(this, Particle.WATER_SPLASH, getServer().getWorlds().get(0), defaultBounds);
        BoundaryIndicators.BombA = new RepeatingBoundaryMarker(this, Particle.SPELL_WITCH, getServer().getWorlds().get(0), defaultBounds);
        BoundaryIndicators.BombB = new RepeatingBoundaryMarker(this, Particle.SPELL_WITCH, getServer().getWorlds().get(0), defaultBounds);

        BoundaryIndicators.CTSpawnBounds.cancel();
        BoundaryIndicators.TSpawnBounds.cancel();
        BoundaryIndicators.MapBounds.cancel();
        BoundaryIndicators.BombA.cancel();
        BoundaryIndicators.BombB.cancel();

        LOGGER.info("Setting up files...");
        File dir = getDataFolder();
        maps_dir = new File(getDataFolder(), "maps");
        extConfig = new File(getDataFolder(), "config.yml");
        indexFile = new File(getDataFolder(), "index.txt");
        localConfig = getConfig();

        dir.mkdirs();

        try
        {
            maps_dir.mkdirs();

            if(!indexFile.exists())
                indexFile.createNewFile();

            if(!extConfig.exists())
            {
                extConfig.createNewFile();
                localConfig.set("damageFalloff", -1.565217391);
                localConfig.set("highestDamage", 51.3);
                localConfig.set("csHub.world", getServer().getWorlds().get(0).getName());
                localConfig.set("csHub.x", 0);
                localConfig.set("csHub.y", 0);
                localConfig.set("csHub.z", 0);
            }

            localConfig.save(extConfig);
            localConfig.load(extConfig);

            ConfigValues.dmgFalloff = localConfig.getDouble("damageFalloff", -1.565217391);
            ConfigValues.dmgHighest = localConfig.getDouble("highestDamage", 51.3);
            ConfigValues.hubLoc = new Location(getServer().getWorld(localConfig.getString("csHub.world", "world")),
                    localConfig.getDouble("csHub.x", 0), localConfig.getDouble("csHub.y", 0), localConfig.getDouble("csHub.z",  0));
        }
        catch (IOException | InvalidConfigurationException e)
        {
            LOGGER.severe("Failed to load files! Committing suicide...");
            getServer().getPluginManager().disablePlugin(this);
        }

        LOGGER.info("Handling the rest...");
        MapLoader.loadMaps(this);
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        Set<String> mapKeys = MAPS.keySet();

        for(String key : mapKeys)
            MAPS.get(key).getTimer().setVisible(false);
    }
}
