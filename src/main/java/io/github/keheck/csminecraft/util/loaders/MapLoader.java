package io.github.keheck.csminecraft.util.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

public class MapLoader
{
    private static final String[] boundFields = {"main", "bomba", "bombb", "tspawn", "ctspawn"};

    public static void loadMaps(JavaPlugin plugin)
    {
        String[] names = new String[32];

        try
        {
            BufferedReader indexReader = new BufferedReader(new FileReader(CSMinecraft.indexFile));
            String line;
            int i = 0;

            while((line = indexReader.readLine()) != null)
            {
                names[i] = line;
                i++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        try
        {
            for(String fileName : names)
            {
                if(fileName != null)
                {
                    int[][] bounds = new int[5][6];


                    File mapFile = new File(CSMinecraft.maps_dir, fileName + ".json");
                    byte[] bytes = Files.readAllBytes(mapFile.toPath());

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(bytes);

                    World world = plugin.getServer().getWorld(root.get("world").textValue());

                    ObjectNode boundsNode = (ObjectNode)root.get("bounds");

                    for(int i = 0; i < boundFields.length; i++)
                    {
                        ArrayNode boundsArray = (ArrayNode)boundsNode.get(boundFields[i]);
                        Iterator<JsonNode> nodeIterator = boundsArray.iterator();
                        int j = 0;

                        while(nodeIterator.hasNext())
                        {
                            JsonNode node = nodeIterator.next();
                            int value = node.intValue();
                            bounds[i][j] = value;
                            j++;
                        }
                    }

                    CSMinecraft.MAPS.put(fileName, new Map(plugin, fileName, world, bounds));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
}
