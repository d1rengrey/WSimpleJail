package net.d1rengray.jail;

import net.d1rengray.jail.handler.JailHandler;
import net.d1rengray.jail.command.JailCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.io.File;
import java.io.IOException;

@Plugin(name = "Jail", version = "1.0")

@Commands(
        @Command(name = "net/d1rengray/jail", permission = "jail.use", usage = "/jail <player> [count] [reason]")
)

public class JailPlugin extends JavaPlugin {

    private FileConfiguration messages;

    @Override
    public void onEnable(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        Jail jail = new Jail(this);

        saveDefaultConfig();
        loadMessages();

        pluginManager.registerEvents(new JailHandler(jail, this), this);

        getCommand("net/d1rengray/jail").setExecutor(new JailCommand(jail, this));
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    private void loadMessages() {
        String language = getConfig().getString("jail_settings.language");

        File msg = new File(getDataFolder(), "messages_" + language + ".yml");

        if (!msg.exists()) {
            msg.getParentFile().mkdirs();
            try {
                saveResource("messages_" + language + ".yml", false);
            } catch (IllegalArgumentException ex){
                System.out.println("Can't load \"" + language + "\" message file, load default..");
                msg = new File(getDataFolder(), "messages_ru.yml");
            }
        }

        messages = new YamlConfiguration();
        try {
            messages.load(msg);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
