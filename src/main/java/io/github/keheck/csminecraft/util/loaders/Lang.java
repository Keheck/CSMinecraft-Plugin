package io.github.keheck.csminecraft.util.loaders;

import io.github.keheck.csminecraft.CSMinecraft;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;

public class Lang
{
    public static void downloadFile(JavaPlugin plugin)
    {
        try
        {
            BufferedInputStream in = new BufferedInputStream(new URL("https://keheck.github.io/downloadables/messages.lang").openStream());
            FileOutputStream out = new FileOutputStream(new File(plugin.getDataFolder(), "messages.lang"));
            byte[] buffer = new byte[1024];
            int bytesRead;

            while((bytesRead = in.read(buffer, 0, 1024)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Failed to download messages.lang!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public static void loadFile(JavaPlugin plugin)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(CSMinecraft.messages));
            String line;

            while((line = reader.readLine()) != null && line.length() > 1)
            {
                String[] split = line.split("=");
                CSMinecraft.MESSAGES.put(split[0], split[1]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String get(String id) { return CSMinecraft.MESSAGES.get(id); }
}
