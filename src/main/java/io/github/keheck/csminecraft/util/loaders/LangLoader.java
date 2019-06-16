package io.github.keheck.csminecraft.util.loaders;

import io.github.keheck.csminecraft.CSMinecraft;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class LangLoader
{
    public static void downloadLang(JavaPlugin plugin)
    {
        try
        {
            File file = new File(plugin.getDataFolder(), "messages.lang");
            if(!file.exists()) file.createNewFile();

            URL url = new URL("https://keheck.github.io/downloadables/messages.lang");
            ReadableByteChannel channel = Channels.newChannel(url.openStream());
            FileOutputStream out = new FileOutputStream(file);
            out.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);

            loadLang(plugin);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadLang(JavaPlugin plugin)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File(plugin.getDataFolder(), "messages.lang")));

            String line;
            int i = 0;

            while((line = reader.readLine()) != null)
            {
                if(line.matches("(((\\S)+)\\.)+(\\S)+=(.)+(\\s)*"))
                {
                    String[] keyValPair = line.split("=");
                    CSMinecraft.MESSAGES.put(keyValPair[0], keyValPair[1]);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String get(String lang)
    {
        return CSMinecraft.MESSAGES.get(lang) != null ? CSMinecraft.MESSAGES.get(lang) : lang;
    }

    public static String get(String lang, Object... format)
    {
        return CSMinecraft.MESSAGES.get(lang) != null ? String.format(CSMinecraft.MESSAGES.get(lang), format) : lang;
    }
}
