package com.adminonlyloginip;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.net.InetSocketAddress;
import java.time.Duration;

public class LoginListener implements Listener {

    private final AdminOnlyLoginIP plugin;

    public LoginListener(AdminOnlyLoginIP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        AdminStore.StoredEntry entry = plugin.getStore().getByUuid(event.getUniqueId());
        if (entry == null) return;

        InetSocketAddress addr = event.getAddress();
        if (addr == null || addr.getAddress() == null) {
            kick(event, "Cannot determine your IP.");
            return;
        }

        String playerIp = addr.getAddress().getHostAddress();
        if (!playerIp.equals(entry.ip())) {
            kick(event, "IP mismatch");
        }
    }

    private void kick(AsyncPlayerPreLoginEvent event, String reason) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        event.setKickMessage("Access denied");
        Title title = Title.title(
                Component.text("No no no mister fish"),
                Component.empty(),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(5), Duration.ofMillis(500))
        );
        event.kickMessage(Component.text("No no no mister fish"));
    }
}
