package com.adminonlyloginip;

import org.bukkit.plugin.java.JavaPlugin;

public class AdminOnlyLoginIP extends JavaPlugin {

    private AdminStore store;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        store = new AdminStore(this);
        getCommand("adminonlyloginip").setExecutor(new AdminCommand(this));
        getServer().getPluginManager().registerEvents(new LoginListener(this), getLogger());
        getLogger().info("AdminOnlyLoginIP enabled - " + store.getAll().size() + " entries loaded");
    }

    @Override
    public void onDisable() {
        if (store != null) store.save();
    }

    public AdminStore getStore() {
        return store;
    }
}
