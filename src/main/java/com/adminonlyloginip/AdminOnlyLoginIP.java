package com.adminonlyloginip;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class AdminOnlyLoginIP extends JavaPlugin {

    private AdminStore store;
    private File dataFile;
    private YamlConfiguration dataConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        store = new AdminStore(this);
        loadDataFile();
        getCommand("aoli").setExecutor(new AdminCommand(this));
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        getLogger().info("AdminOnlyLoginIP enabled - " + store.getAll().size() + " entries loaded");
    }

    @Override
    public void onDisable() {
        if (store != null) store.save();
    }

    private void loadDataFile() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public boolean isCheckEnabled() {
        if (dataConfig != null && dataConfig.contains("enabled")) {
            return dataConfig.getBoolean("enabled", true);
        }
        return getConfig().getBoolean("enabled", true);
    }

    public void setData(String key, Object value) {
        if (dataConfig == null) loadDataFile();
        dataConfig.set(key, value);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            getLogger().severe("Failed to save data.yml: " + e.getMessage());
        }
    }

    public void reloadData() {
        reloadConfig();
        loadDataFile();
        store.reloadDataFile();
        store.load();
    }

    public AdminStore getStore() {
        return store;
    }
}
