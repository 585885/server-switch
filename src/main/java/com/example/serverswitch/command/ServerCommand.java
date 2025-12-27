package com.example.serverswitch.command;

import com.example.serverswitch.ServerSwitchPlugin;
import com.example.serverswitch.menu.ServerMenu;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ServerCommand implements SimpleCommand {

    private final ServerSwitchPlugin plugin;

    public ServerCommand(ServerSwitchPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player player)) {
            source.sendMessage(Component.text("此命令只能由玩家执行！").color(NamedTextColor.RED));
            return;
        }

        if (args.length == 0) {
            openMenu(player);
            return;
        }

        String serverName = args[0];
        connectToServer(player, serverName);
    }

    private void openMenu(Player player) {
        new ServerMenu(plugin, player).open();
    }

    private void connectToServer(Player player, String serverName) {
        plugin.getServer().getServer(serverName).ifPresentOrElse(
            server -> {
                Optional<ServerConnection> currentServer = player.getCurrentServer();
                if (currentServer.isPresent() && currentServer.get().getServer().equals(server)) {
                    player.sendMessage(Component.text(plugin.getConfig().getMessage("message.already-connected")).color(NamedTextColor.RED));
                    return;
                }

                player.sendMessage(Component.text(plugin.getConfig().getMessage("message.connected", serverName)).color(NamedTextColor.GREEN));
                player.createConnectionRequest(server).connect();
            },
            () -> player.sendMessage(Component.text(plugin.getConfig().getMessage("message.server-not-found", serverName)).color(NamedTextColor.RED))
        );
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return true;
    }
}