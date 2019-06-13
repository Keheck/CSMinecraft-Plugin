package io.github.keheck.csminecraft.util.filehandling;

import io.github.keheck.csminecraft.CSMinecraft;
import org.apache.commons.lang.ArrayUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class IndexFile
{
    public static void delete(String map)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(CSMinecraft.indexFile));
            StringBuilder builder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null)
            {
                if(!line.equals(map))
                    builder.append(line);
            }

            reader.close();

            Files.write(CSMinecraft.indexFile.toPath(), builder.toString().getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void add(String map)
    {
        try
        {
            byte[] lineBreak = System.lineSeparator().getBytes();
            byte[] bytes = ArrayUtils.addAll(lineBreak, map.getBytes());

            Files.write(CSMinecraft.indexFile.toPath(), bytes, StandardOpenOption.APPEND);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
