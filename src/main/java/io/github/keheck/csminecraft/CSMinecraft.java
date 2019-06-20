package io.github.keheck.csminecraft;

import io.github.keheck.csminecraft.commands.CSHelpHandler;
import io.github.keheck.csminecraft.commands.CSReloadHandler;
import io.github.keheck.csminecraft.commands.games.CSListHandler;
import io.github.keheck.csminecraft.commands.mapcreating.*;
import io.github.keheck.csminecraft.commands.games.CSForceStartHandler;
import io.github.keheck.csminecraft.commands.games.CSForceStopHandler;
import io.github.keheck.csminecraft.commands.joining.CSJoinHandler;
import io.github.keheck.csminecraft.commands.joining.CSLeaveHandler;
import io.github.keheck.csminecraft.objectholder.BoundaryIndicators;
import io.github.keheck.csminecraft.listener.*;
import io.github.keheck.csminecraft.repeats.RepeatingBoundaryMarker;
import io.github.keheck.csminecraft.util.ConfigValues;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
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

public final class CSMinecraft extends JavaPlugin
{
    public static File maps_dir;
    public static File extConfig;
    public static File indexFile;
    public static File messages;
    public static FileConfiguration localConfig;
    public static HashMap<String, Map> MAPS;
    public static HashMap<String, String> MESSAGES;
    public static ArrayList<Player> PLAYERS_IN_GAME;
    private static final int[] defaultBounds = new int[] {0, 0, 0, 0, 0, 0};

    @Override
    public void onEnable()
    {
        maps_dir = null;
        extConfig = null;
        indexFile = null;
        localConfig = null;
        messages = null;
        MAPS = new HashMap<>();
        MESSAGES = new HashMap<>();
        PLAYERS_IN_GAME = new ArrayList<>();

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
        getCommand("cslist").setExecutor(new CSListHandler(this));

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

        File dir = getDataFolder();
        maps_dir = new File(getDataFolder(), "maps");
        extConfig = new File(getDataFolder(), "config.yml");
        indexFile = new File(getDataFolder(), "index.txt");
        messages = new File(getDataFolder(), "messages.lang");
        localConfig = getConfig();

        dir.mkdirs();

        try
        {
            maps_dir.mkdirs();

            if(!indexFile.exists())
                indexFile.createNewFile();

            if(!messages.exists())
                messages.createNewFile();

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
            getServer().getPluginManager().disablePlugin(this);
        }

        LangLoader.downloadLang(this);
        MapLoader.loadMaps(this);
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        Set<String> mapKeys = MAPS.keySet();

        for(String key : mapKeys)
            MAPS.get(key).stopGame();
    }
}
