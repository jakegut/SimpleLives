package com.borgdude.lives;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.borgdude.lives.commands.LivesCommand;
import com.borgdude.lives.events.LivesEvents;
import com.borgdude.lives.managers.LanguageManager;

/**
 * Hello world!
 *
 */
public class SimpleLives extends JavaPlugin {

    private SimpleLives plugin;
    
    private LanguageManager languageManager;
    
    public static Metrics metrics;
    
    private File livesCounterFile;
    private FileConfiguration livesCounterConfig;
    
    @Override
    public void onEnable() {
        plugin = this;
        metrics = new Metrics(this);
        try {
            createCustomConfigs();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            getServer().getConsoleSender()
                    .sendMessage(ChatColor.RED + "SimpleLives ran to an error ... disabling");
            return;
        }
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        matchConfig();
        languageManager = new LanguageManager(this);
        getCommand("lives").setExecutor(new LivesCommand(this));
        getServer().getPluginManager().registerEvents(new LivesEvents(this), this);
    }
    
    public FileConfiguration getLivesConfig() {
        return this.livesCounterConfig;
    }
    
    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }
    
    public void saveLivesConfig() throws IOException {
        livesCounterConfig.save(livesCounterFile);
    }
    
    private void createCustomConfigs() throws FileNotFoundException, IOException, InvalidConfigurationException {
        livesCounterFile = new File(getDataFolder(), "lives.yml");
        if (!livesCounterFile.exists()) {
            livesCounterFile.getParentFile().mkdirs();
            saveResource("lives.yml", false);
        }

        livesCounterConfig = YamlConfiguration.loadConfiguration(livesCounterFile);
        livesCounterConfig.addDefault("players", " ");
        livesCounterConfig.options().copyDefaults(true);

    }

    // From
    // https://bukkit.org/threads/make-your-custom-config-update-and-copy-delete-the-defaults.373956/
    private void matchConfig() {
        try {
            File file = new File(plugin.getDataFolder().getAbsolutePath(), "config.yml");
            if (file != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
                for (String key : defConfig.getConfigurationSection("").getKeys(false))
                    if (!getConfig().contains(key))
                        getConfig().set(key, defConfig.getConfigurationSection(key));

                saveConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "SimpleLives Disabled");
    }

}
