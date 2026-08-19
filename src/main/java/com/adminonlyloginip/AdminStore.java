package com.adminonlyloginip;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class AdminStore {

    private final AdminOnlyLoginIP plugin;
    private final Map<UUID, StoredEntry> byUuid = new HashMap<>();
    private final Map<String, UUID> byNickname = new HashMap<>();

    public record StoredEntry(UUID uuid, String nickname, String ip) {}

    public AdminStore(AdminOnlyLoginIP plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        byUuid.clear();
        byNickname.clear();
        FileConfiguration cfg = plugin.getConfig();
        ConfigurationSection sec = cfg.getConfigurationSection("entries");
        if (sec == null) return;
        for (String key : sec.getKeys(false)) {
            ConfigurationSection e = sec.getConfigurationSection(key);
            if (e == null) continue;
            UUID uuid = UUID.fromString(e.getString("uuid", ""));
            String nick = e.getString("nickname", "");
            String ip = e.getString("ip", "");
            StoredEntry entry = new StoredEntry(uuid, nick, ip);
            byUuid.put(uuid, entry);
            byNickname.put(nick.toLowerCase(), uuid);
        }
    }

    public void save() {
        FileConfiguration cfg = plugin.getConfig();
        cfg.set("entries", null);
        int i = 0;
        for (StoredEntry entry : byUuid.values()) {
            String path = "entries." + i;
            cfg.set(path + ".uuid", entry.uuid().toString());
            cfg.set(path + ".nickname", entry.nickname());
            cfg.set(path + ".ip", entry.ip());
            i++;
        }
        plugin.saveConfig();
    }

    public boolean add(UUID uuid, String nickname, String ip) {
        StoredEntry entry = new StoredEntry(uuid, nickname, ip);
        byUuid.put(uuid, entry);
        byNickname.put(nickname.toLowerCase(), uuid);
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
