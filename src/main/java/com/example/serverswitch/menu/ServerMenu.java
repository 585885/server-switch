package com.example.serverswitch.menu;

import com.example.serverswitch.ServerSwitchPlugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.concurrent.CompletableFuture;

public class ServerMenu {

    private final ServerSwitchPlugin plugin;
    private final Player player;

    public ServerMenu(ServerSwitchPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void open() {
        var servers = plugin.getServer().getAllServers();

        if (servers.isEmpty()) {
            player.sendMessage(Component.text(plugin.getConfig().getMessage("message.no-servers")).color(NamedTextColor.RED));
            return;
        }

        Component title = Component.text(plugin.getConfig().getMessage("menu.title"))
            .color(TextColor.color(0xFFAA00))
            .decoration(TextDecoration.BOLD, true);

        player.sendMessage(title);
        player.sendMessage(Component.empty());

        for (RegisteredServer server : servers) {
            showServerInfo(server);
        }

        player.sendMessage(Component.empty());
        player.sendMessage(Component.text(plugin.getConfig().getMessage("menu.click"))
            .color(NamedTextColor.GRAY));
    }

    private void showServerInfo(RegisteredServer server) {
        String serverName = server.getServerInfo().getName();
        int playerCount = server.getPlayersConnected().size();

        server.ping().thenAccept(ping -> {
            boolean isOnline = ping != null;
            int pingValue = isOnline ? ping.getPlayers().map(ServerPing.Players::getOnline).orElse(playerCount) : 0;
            String statusText = isOnline ? plugin.getConfig().getMessage("menu.online") : plugin.getConfig().getMessage("menu.offline");
            NamedTextColor statusColor = isOnline ? NamedTextColor.GREEN : NamedTextColor.RED;

            Component serverButton = Component.text()
                .append(Component.text("▶ ").color(NamedTextColor.YELLOW))
                .append(Component.text(serverName)
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.BOLD, true)
                    .clickEvent(ClickEvent.runCommand("/stp " + serverName))
                    .hoverEvent(HoverEvent.showText(
                        Component.text(plugin.getConfig().getMessage("menu.hover"))
                            .color(NamedTextColor.GREEN)
                    )))
                .append(Component.text(" ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(statusText).color(statusColor))
                .append(Component.newline())
                .append(Component.text("  ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(plugin.getConfig().getMessage("menu.players"))
                    .color(NamedTextColor.YELLOW))
                .append(Component.text(String.valueOf(playerCount)).color(NamedTextColor.WHITE))
                .append(Component.text("  ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(plugin.getConfig().getMessage("menu.ping"))
                    .color(NamedTextColor.YELLOW))
                .append(Component.text(isOnline ? "< 50ms" : "离线").color(isOnline ? NamedTextColor.GREEN : NamedTextColor.RED))
                .build();

            player.sendMessage(serverButton);
        }).exceptionally(ex -> {
            Component serverButton = Component.text()
                .append(Component.text("▶ ").color(NamedTextColor.YELLOW))
                .append(Component.text(serverName)
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.BOLD, true)
                    .clickEvent(ClickEvent.runCommand("/stp " + serverName))
                    .hoverEvent(HoverEvent.showText(
                        Component.text(plugin.getConfig().getMessage("menu.hover"))
                            .color(NamedTextColor.GREEN)
                    )))
                .append(Component.text(" ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(plugin.getConfig().getMessage("menu.offline")).color(NamedTextColor.RED))
                .append(Component.newline())
                .append(Component.text("  ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(plugin.getConfig().getMessage("menu.players"))
                    .color(NamedTextColor.YELLOW))
                .append(Component.text(String.valueOf(playerCount)).color(NamedTextColor.WHITE))
                .build();

            player.sendMessage(serverButton);
            return null;
        });
    }
}