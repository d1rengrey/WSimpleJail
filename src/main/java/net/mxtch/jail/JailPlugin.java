package net.mxtch.jail;

import net.mxtch.jail.command.JailCommand;
import net.mxtch.jail.handler.JailHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

@Plugin(name = "Jail", version = "1.0")
@Command(name = "jail", permission = "jail.use", usage = "/jail <player> [count] [reason]")

public class JailPlugin extends JavaPlugin {

    @Override
    public void onEnable(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        Jail jail = new Jail(this);

        saveDefaultConfig();

        pluginManager.registerEvents(new JailHandler(jail, this), this);

        getCommand("jail").setExecutor(new JailCommand(jail, this));

        JailConfig.setConfig(getConfig());


    }

}
