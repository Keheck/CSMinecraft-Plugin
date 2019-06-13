package io.github.keheck.csminecraft.util.filehandling;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.objectholder.BoundaryIndicators;
import org.bukkit.World;

import java.io.*;

public class MapsFile
{
    public static void addMap(World world, String name)
    {
        try
        {
            JsonFactory factory = new JsonFactory();
            JsonGenerator gen = factory.createGenerator(new FileWriter(new File(CSMinecraft.maps_dir, name + ".json"))).useDefaultPrettyPrinter();

            gen.writeStartObject();
                gen.writeFieldName("world");
                gen.writeString(world.getName());
                gen.writeFieldName("bounds");
                gen.writeStartObject();
                    gen.writeFieldName("main");
                    gen.writeArray(BoundaryIndicators.MapBounds.getBounds(), 0, 6);

                    gen.writeFieldName("bomba");
                    gen.writeArray(BoundaryIndicators.BombA.getBounds(), 0, 6);

                    gen.writeFieldName("bombb");
                    gen.writeArray(BoundaryIndicators.BombB.getBounds(), 0, 6);

                    gen.writeFieldName("tspawn");
                    gen.writeArray(BoundaryIndicators.TSpawnBounds.getBounds(), 0, 6);

                    gen.writeFieldName("ctspawn");
                    gen.writeArray(BoundaryIndicators.CTSpawnBounds.getBounds(), 0, 6);
                gen.writeEndObject();
            gen.writeEndObject();
            gen.flush();
            gen.close();

            IndexFile.add(name);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteMap(String name)
    {
        File[] maps = CSMinecraft.maps_dir.listFiles();

        if(maps != null)
        {
            for(File f : maps)
            {
                if(f.getName().startsWith(name))
                {
                    if(f.delete())
                        IndexFile.delete(name);
                }
            }
        }
    }

    public static boolean contains(String name)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(CSMinecraft.indexFile));
            String line;

            while((line = reader.readLine()) != null)
            {
                if(line.equals(name))
                {
                    reader.close();
                    return true;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
