package com.example.serverswitch.config;

import com.example.serverswitch.ServerSwitchPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {

    private final ServerSwitchPlugin plugin;
    private final Path dataDirectory;
    private final Path configFile;
    private Properties messages;

    public Config(ServerSwitchPlugin plugin, Path dataDirectory) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
        this.configFile = dataDirectory.resolve("messages.properties");
        this.messages = new Properties();
    }

    public void load() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }

            if (!Files.exists(configFile)) {
                saveDefaultMessages();
            }

            try (InputStream input = new FileInputStream(configFile.toFile())) {
                messages.load(input);
            }
        } catch (IOException e) {
            plugin.getLogger().error("加载配置文件失败！", e);
        }
    }

    private void saveDefaultMessages() throws IOException {
        messages.setProperty("menu.title", "§6§l服务器列表");
        messages.setProperty("menu.online", "§a在线");
        messages.setProperty("menu.offline", "§c离线");
        messages.setProperty("menu.players", "§e玩家: §f");
        messages.setProperty("menu.ping", "§e延迟: §f");
        messages.setProperty("menu.click", "§7点击连接");
        messages.setProperty("menu.hover", "§a点击连接到此服务器");

        messages.setProperty("message.no-servers", "§c没有可用的服务器！");
        messages.setProperty("message.server-list", "§6§l可用服务器:");
        messages.setProperty("message.server-info", "  §e- §f{server} §7({players} 人在线, 延迟: {ping}ms)");
        messages.setProperty("message.connected", "§a正在连接到 §f{server}...");
        messages.setProperty("message.already-connected", "§c你已经在这个服务器上了！");
        messages.setProperty("message.server-not-found", "§c服务器 §f{server} §c不存在！");
        messages.setProperty("message.server-offline", "§c服务器 §f{server} §c当前离线！");

        try (OutputStream output = new FileOutputStream(configFile.toFile())) {
            messages.store(output, "ServerSwitch 消息配置文件");
        }
    }

    public String getMessage(String key) {
        return messages.getProperty(key, key);
    }

    public String getMessage(String key, Object... args) {
        String message = getMessage(key);
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return message;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }
}