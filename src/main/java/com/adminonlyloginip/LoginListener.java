package com.adminonlyloginip;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class LoginListener implements Listener {

    private final AdminOnlyLoginIP plugin;

    public LoginListener(AdminOnlyLoginIP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!plugin.isCheckEnabled()) return;

        AdminStore.StoredEntry entry = plugin.getStore().getByUuid(event.getUniqueId());
        if (entry == null) return;

        java.net.InetAddress addr = event.getAddress();
        if (addr == null) {
            kick(event);
            return;
        }

        String playerIp = addr.getHostAddress();
        if (!playerIp.equals(entry.ip())) {
            plugin.getLogger().warning("KICKED " + event.getName() + " | stored IP: " + entry.ip() + " | connecting IP: " + playerIp);
            kick(event);
        }
    }

    private void kick(AsyncPlayerPreLoginEvent event) {
        String msg = plugin.getConfig().getString("kick-message", "Доступ запрещён");
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(msg);
    }
}
