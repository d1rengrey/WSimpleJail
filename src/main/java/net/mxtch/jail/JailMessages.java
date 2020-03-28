package net.mxtch.jail;

import org.bukkit.configuration.file.FileConfiguration;

public class JailMessages {
    static FileConfiguration configuration;

    public static String getMessage(String messagePath){
        String message = configuration.getString(messagePath);
        if (message != null)
            return message;
        throw new NullPointerException("Can't find message path");
    }
    public static void setConfig(FileConfiguration config){
        configuration = config;
    }
}
