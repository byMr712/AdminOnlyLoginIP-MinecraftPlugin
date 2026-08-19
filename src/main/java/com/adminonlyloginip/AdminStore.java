package com.adminonlyloginip;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class AdminStore {

    private final AdminOnlyLoginIP plugin;
    private final Map<UUID, StoredEntry> byUuid = new ConcurrentHashMap<>();
    private final Map<String, UUID> byNickname = new ConcurrentHashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public record StoredEntry(UUID uuid, String nickname, String ip) {}

    public AdminStore(AdminOnlyLoginIP plugin) {
        this.plugin = plugin;
        reloadDataFile();
        load();
    }

    public void reloadDataFile() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void load() {
        byUuid.clear();
        byNickname.clear();
        ConfigurationSection sec = dataConfig.getConfigurationSection("entries");
        if (sec == null) return;
        for (String key : sec.getKeys(false)) {
            ConfigurationSection e = sec.getConfigurationSection(key);
            if (e == null) continue;
            String uuidStr = e.getString("uuid", "");
            if (uuidStr.isEmpty()) continue;
            UUID uuid;
            try {
                uuid = UUID.fromString(uuidStr);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().log(Level.WARNING, "Invalid UUID in data.yml entry {0}: {1}", new Object[]{key, uuidStr});
                continue;
            }
            String nick = e.getString("nickname", "");
            String ip = e.getString("ip", "");
            StoredEntry entry = new StoredEntry(uuid, nick, ip);
            byUuid.put(uuid, entry);
            byNickname.put(nick.toLowerCase(), uuid);
        }
    }

    public void save() {
        dataConfig.set("entries", null);
        int i = 0;
        for (StoredEntry entry : byUuid.values()) {
            String path = "entries." + i;
            dataConfig.set(path + ".uuid", entry.uuid().toString());
            dataConfig.set(path + ".nickname", entry.nickname());
            dataConfig.set(path + ".ip", entry.ip());
            i++;
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml: " + e.getMessage());
        }
    }

    public boolean add(UUID uuid, String nickname, String ip) {
        String key = nickname.toLowerCase();

        UUID oldUuid = byNickname.get(key);
        if (oldUuid != null && !oldUuid.equals(uuid)) {
            byUuid.remove(oldUuid);
        }

        StoredEntry old = byUuid.get(uuid);
        if (old != null && !old.nickname().equalsIgnoreCase(nickname)) {
            byNickname.remove(old.nickname().toLowerCase());
        }

        StoredEntry entry = new StoredEntry(uuid, nickname, ip);
        byUuid.put(uuid, entry);
        byNickname.put(key, uuid);
        save();
        return true;
    }

    public boolean delete(String nickname) {
        UUID uuid = byNickname.remove(nickname.toLowerCase());
        if (uuid == null) return false;
        byUuid.remove(uuid);
        save();
        return true;
    }

    public StoredEntry getByUuid(UUID uuid) {
        return byUuid.get(uuid);
    }

    public Collection<StoredEntry> getAll() {
        return Collections.unmodifiableCollection(byUuid.values());
    }
}
