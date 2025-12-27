package com.example.serverswitch;

import com.example.serverswitch.command.ServerCommand;
import com.example.serverswitch.config.Config;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
    id = "server-switch",
    name = "ServerSwitch",
    version = "1.0.0",
    description = "Velocity服务器切换插件",
    authors = {"Example"}
)
public class ServerSwitchPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private Config config;

    @Inject
    public ServerSwitchPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("正在加载 ServerSwitch 插件...");

        this.config = new Config(this, dataDirectory);
        config.load();

        server.getCommandManager().register(
            server.getCommandManager().metaBuilder("stp")
                .build(),
            new ServerCommand(this)
        );

        logger.info("ServerSwitch 插件已加载完成！");
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Config getConfig() {
        return config;
    }
}